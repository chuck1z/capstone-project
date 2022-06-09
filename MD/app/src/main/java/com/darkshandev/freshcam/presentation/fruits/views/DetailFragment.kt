package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.databinding.FragmentDetailBinding
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import com.darkshandev.freshcam.utils.loadCircleImage
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private val fruitsViewmodel by activityViewModels<FruitsViewmodel>()
    private var binding: FragmentDetailBinding? = null
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        lifecycleScope.launch {
            fruitsViewmodel.fruitsDetail.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view

                    }
                    is AppState.Success -> {
                        binding?.apply {
                           state.data?.let {
                               tvFruitTitle.text=it.name
                               tvAboutDesc.text=it.about
                               tvScientificName.text=it.binom
                               itemVitamin.text = it.vitamin.toString()
                               tvTipsDescritions.text = it.tips.toString()
                               ivFruits.loadCircleImage("https://media.istockphoto.com/photos/whole-cross-section-and-quarter-of-fresh-organic-navel-orange-with-picture-id1227301369?b=1&k=20&m=1227301369&s=170667a&w=0&h=7WdK1RQTLuCn5tuNe25Z999THYzj8yijmk0MaRE-SD0=")
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