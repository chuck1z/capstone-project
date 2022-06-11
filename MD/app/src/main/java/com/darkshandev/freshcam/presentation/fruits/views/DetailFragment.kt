package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.databinding.FragmentDetailBinding
import com.darkshandev.freshcam.presentation.fruits.SimpleTipsAdapter
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import com.darkshandev.freshcam.utils.loadCircleImage
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private val fruitsViewmodel by activityViewModels<FruitsViewmodel>()
    private var binding: FragmentDetailBinding? = null
    private val simpleTipsAdapter = SimpleTipsAdapter()
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
        binding?.apply {
            rvTips.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = simpleTipsAdapter
            }
            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        // Inflate the layout for this fragment
        lifecycleScope.launch {
            fruitsViewmodel.fruitsDetail.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view
                        binding?.progressBar3?.visibility = View.GONE

                    }
                    is AppState.Success -> {
                        binding?.apply {
                            progressBar3.visibility = View.GONE
                            state.data?.let {
                                tvFruitTitle.text = it.name
                                tvAboutDesc.text = it.about
                                tvScientificName.text = it.binom
                                it.vitamin.forEach { vit ->
                                    val chip = Chip(requireContext())
                                    chip.text = vit
                                    chips.addView(chip)
                                }
                                simpleTipsAdapter.updateList(it.tips)
                                nutritions = it.nutrition
                                ivFruits.loadCircleImage(it.image)
                            }
                        }
                    }
                    is AppState.Error -> {
                        binding?.progressBar3?.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
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