package com.darkshandev.freshcam.presentation.settings.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding?.apply {
            cvHistory.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_historyFragment)
            }
            cvAbout.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_aboutFragment)
            }
            cvGuide.setOnClickListener {

            }
        }
        return _binding?.root
    }

}