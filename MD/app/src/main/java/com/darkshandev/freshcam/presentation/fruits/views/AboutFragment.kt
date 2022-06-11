package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.darkshandev.freshcam.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private var binding: FragmentAboutBinding? = null
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding?.apply {
            backButton.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        return binding?.root
    }


}