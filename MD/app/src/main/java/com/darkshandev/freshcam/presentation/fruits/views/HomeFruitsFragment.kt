package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentHomeFruitsBinding
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel


class HomeFruitsFragment : Fragment() {

    private lateinit var binding: FragmentHomeFruitsBinding
    private val fruitsViewmodel: FruitsViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeFruitsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fruits, container, false)
    }
}