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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.Tips
import com.darkshandev.freshcam.databinding.FragmentHomeFruitsBinding
import com.darkshandev.freshcam.presentation.fruits.FruitsAdapter
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import com.darkshandev.freshcam.utils.loadCircleImage
import kotlinx.coroutines.launch


class HomeFruitsFragment : Fragment(), FruitsAdapter.Listener {

    private val fruitsViewmodel by activityViewModels<FruitsViewmodel>()
    private var binding: FragmentHomeFruitsBinding? = null
    private val fruitsAdapter = FruitsAdapter(this)
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeFruitsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupView()
        setupCollector()

        return binding?.root
    }

    private fun setupCollector() {
        lifecycleScope.launch {
            fruitsViewmodel.fruitsOfTheDay.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view

                    }
                    is AppState.Success -> {
                        binding?.apply {
                            state.data?.let { fotd ->
                                tvFruitOfTheDay.text = fotd.name
                                tvDesc.text = fotd.short_desc
                                tvFruitTitle.text = fotd.name
                                ivFruitOfTheDay.loadCircleImage(fotd.image)
                                btnInfo.setOnClickListener {
                                    fruitsViewmodel.setSelectedFruitsId(fotd.fruits_id)
                                    findNavController().navigate(R.id.action_homeFruitsFragment_to_detailFragment)
                                }
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

        lifecycleScope.launch {
            fruitsViewmodel.tips.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        binding?.progressBar2?.visibility = View.VISIBLE
                    }
                    is AppState.Success -> {
                        binding?.progressBar2?.visibility = View.GONE
                        binding?.apply {
                            state.data?.let {
                                fruitsAdapter.updateList(it)
                            }
                        }
                    }
                    is AppState.Error -> {
                        binding?.progressBar2?.visibility = View.GONE
                        //activate error view
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is AppState.Initial -> {

                    }
                }
            }
        }

    }

    private fun setupView() {
        binding?.rvFruitsTips?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvFruitsTips?.adapter = fruitsAdapter
    }

    override fun onClickListener(tips: Tips) {
        fruitsViewmodel.setSelectedTipsId(tips.tips_id)
        findNavController().navigate(R.id.action_homeFruitsFragment_to_tipsDetailFragment)

    }
}



