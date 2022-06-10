package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.databinding.FragmentHomeFruitsBinding
import com.darkshandev.freshcam.databinding.FragmentTipsDetailBinding
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import kotlinx.coroutines.launch


class TipsDetailFragment : Fragment() {
    private val fruitsViewmodel by activityViewModels<FruitsViewmodel>()
    private var binding: FragmentTipsDetailBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTipsDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        lifecycleScope.launch {
            fruitsViewmodel.tipssDetail.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view

                    }
                    is AppState.Success -> {
                        binding?.apply {
                            state.data?.let {
                                titleTipsDetail.text = it.title
                                tvDate.text = it.date_posted
                                tvShortDesc.text = it.short_desc
                                tvFullDesc.text = it.full_desc
                                Glide.with(root.context)
                                    .load(it.image)
                                    .into(ivFruitsTipsDetail)
                            }

                        }
                    }
                    is AppState.Error -> {
                        //activate error view
                    }
                    is AppState.Initial -> {

                    }
                }
            }
        }
        return binding?.root
    }

}