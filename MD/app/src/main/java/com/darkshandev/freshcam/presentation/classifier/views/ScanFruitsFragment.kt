package com.darkshandev.freshcam.presentation.classifier.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentScanFruitsBinding

class ScanFruitsFragment : Fragment() {
private var binding: FragmentScanFruitsBinding? = null
    override fun onDestroyView() {
        binding=null
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