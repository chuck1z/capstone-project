package com.darkshandev.freshcam.presentation.classifier.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.getFreshness
import com.darkshandev.freshcam.data.models.getName
import com.darkshandev.freshcam.databinding.FragmentScanResultBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class ScanResultFragment : BottomSheetDialogFragment() {
    private val classifierViewmodel: ClassifierViewmodel by activityViewModels()
    private var binding: FragmentScanResultBinding? = null
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentScanResultBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
            classifierViewmodel.image.flowWithLifecycle(lifecycle).collect {
                it?.let {
                    binding?.imageView3?.setImageURI(it.toUri())
                }
            }
        }
        lifecycleScope.launch {
            classifierViewmodel.result.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Success -> {
                        binding?.apply {
                            state.data?.let {
                                label.text = it.getFreshness()
                                confidence.text = "${it.confidence}%"
                                descInfo.text = it.description
                                name.text = it.getName()

                            }
                        }
                    }
                    else -> {}
                }

            }
        }
        return binding?.root
    }


}