package com.darkshandev.freshcam.presentation.classifier.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentScanFruitsBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.utils.createFile
import com.darkshandev.freshcam.utils.uriToFile
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanFruitsFragment : Fragment() {
    private var binding: FragmentScanFruitsBinding? = null
    private var cameraExecutor: ExecutorService? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private val classifierViewmodel: ClassifierViewmodel by activityViewModels()
    private var flashMode: Boolean = false
    var timestamp = ""
    override fun onDestroyView() {
        binding = null
        cameraExecutor?.shutdown()
        cameraExecutor = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timestamp = System.currentTimeMillis().toString()
        binding= FragmentScanFruitsBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val backStackLabel = findNavController().previousBackStackEntry?.destination?.label
        val isUserGuideFragment = backStackLabel == requireContext().getString(R.string.user_guide)


        prepareCamera()
        setupView()
        binding?.apply {
            if (isUserGuideFragment) {
                val config = ShowcaseConfig()
                config.delay = 500 // half second between each showcase view
                //get current timestamp

                val sequence =
                    MaterialShowcaseSequence(requireActivity(), timestamp)

                sequence.setConfig(config)
                sequence.addSequenceItem(
                    textView3,
                    "This is camera preview, put your fruits on center frame then capture the image",
                    "GOT IT"
                )
                sequence.addSequenceItem(
                    flashButton,
                    "This is flash button, control your flash to on or off", "GOT IT"
                )
                sequence.addSequenceItem(
                    captureImage,
                    "Capture Your Fruits,this is the capture button, tap to capture your fruits and wait the results",
                    "GOT IT"
                )
                sequence.addSequenceItem(
                    galleryButton,
                    "Pick Your Fruits image,his is the gallery button, tap to pick your fruits image from gallery and wait the results",
                    "GOT IT"
                )

                sequence.start()


            }
        }
        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        startCamera()

    }

    private fun prepareCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
//        imageCapture = ImageCapture.Builder()
//            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .setTargetRotation(binding?.root?.display?.rotation)
//            .build()

    }

    private fun startCamera() {

        binding?.apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(preview.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder().build()
                try {
                    cameraProvider.unbindAll()
                    val cam = cameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                    flashButton.setOnClickListener {
                        if (cam.cameraInfo.hasFlashUnit()) {
                            if (flashMode) {
                                cam.cameraControl.enableTorch(false);
                                flashMode = false
                                flashButton.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_flash_off_24
                                    )
                                )
                            } else {
                                cam.cameraControl.enableTorch(true);
                                flashMode = true
                                flashButton.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_flash_on_24
                                    )
                                )
                            }
                        }

                    }
                } catch (exc: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_get_camera),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, ContextCompat.getMainExecutor(requireContext()))

        }
    }

    private fun setupView() {
        binding?.apply {
            backButton.setOnClickListener {
                activity?.onBackPressed()
            }
            captureImage.setOnClickListener {
                captureImage()
            }

            galleryButton.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, getString(R.string.pick_photo))
                launcherIntentGallery.launch(chooser)
            }
        }
    }

    private fun captureImage() {
        try {
            val imageCapture = imageCapture ?: return
            val photoFile = createFile(requireActivity().application)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.failed_pick_photo),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        output.savedUri
                        classifyFruitsByThis(image = photoFile)
                    }
                }
            )
        } catch (e: java.lang.Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            classifyFruitsByThis(image = myFile)
        }
    }

    private fun classifyFruitsByThis(image: File) {
        try {
            classifierViewmodel.setImage(image)
            classifierViewmodel.classifyImage()
            findNavController().navigate(R.id.action_scanFruitsFragment_to_scanResultFragment)
        } catch (e: java.lang.Exception) {

            e.message?.let { Log.e("Error", it) }
        }


    }
}