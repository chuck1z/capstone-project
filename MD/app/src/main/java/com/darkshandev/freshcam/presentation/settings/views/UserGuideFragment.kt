package com.darkshandev.freshcam.presentation.settings.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentUserGuideBinding

class UserGuideFragment : Fragment() {

    private var binding: FragmentUserGuideBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserGuideBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding?.apply {
            btnGuide.setOnClickListener {
                findNavController().navigate(R.id.action_userGuideFragment_to_scanFruitsFragment)
            }
            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        // Inflate the layout for this fragment
        return binding?.root
    }

}