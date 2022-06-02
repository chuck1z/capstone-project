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
<<<<<<< HEAD
                            state.data?.let {
                                tvFruitTitle.text = it.name
                                tvAboutDesc.text = it.about
                                tvScientificName.text = it.binom
                            }
=======
                           state.data?.let {
                               tvFruitTitle.text=it.name
                               tvAboutDesc.text=it.about
                               tvScientificName.text=it.binom
                               itemVitamin.text = it.vitamin.toString()
                               tvTipsDescritions.text = it.tips.toString()
                           }
>>>>>>> eae2c95052c38d42865be5537857e7cceb06d792
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