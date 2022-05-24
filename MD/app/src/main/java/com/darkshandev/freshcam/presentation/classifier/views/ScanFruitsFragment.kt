package com.darkshandev.freshcam.presentation.classifier.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentScanFruitsBinding
import java.util.concurrent.ExecutorService

class ScanFruitsFragment : Fragment() {
private var binding: FragmentScanFruitsBinding? = null
    private var cameraExecutor: ExecutorService? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    override fun onDestroyView() {
        binding=null
        cameraExecutor?.shutdown()
        cameraExecutor = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
  binding = FragmentScanFruitsBinding.inflate(inflater, container, false)
        setupView()
        return binding?.root
    }

    private fun setupView() {
        binding?.apply {
         backButton.setOnClickListener {
            activity?.onBackPressed()
         }
            captureImage.setOnClickListener {

            }
            flashButton.setOnClickListener {

            }
            galleryButton.setOnClickListener {

            }
        }
    }


}