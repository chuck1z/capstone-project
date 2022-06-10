package com.darkshandev.freshcam.presentation.fruits.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.FruitsTips
import com.darkshandev.freshcam.databinding.FragmentHomeFruitsBinding
import com.darkshandev.freshcam.presentation.fruits.FruitsAdapter
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import com.darkshandev.freshcam.utils.loadCircleImage
import kotlinx.coroutines.launch


class HomeFruitsFragment : Fragment() {

    private val fruitsViewmodel by activityViewModels<FruitsViewmodel>()
    private var binding: FragmentHomeFruitsBinding? = null
    private val fruitsAdapter = FruitsAdapter()
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
        lifecycleScope.launch {
            fruitsViewmodel.fruitsOfTheDay.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view

                    }
                    is AppState.Success -> {
                        binding?.apply {
                            state.data?.let {
                                tvFruitOfTheDay.text = it.name
                                tvDesc.text = it.short_desc
                                tvFruitTitle.text = it.name
                                ivFruitOfTheDay.loadCircleImage("https://media.istockphoto.com/photos/whole-cross-section-and-quarter-of-fresh-organic-navel-orange-with-picture-id1227301369?b=1&k=20&m=1227301369&s=170667a&w=0&h=7WdK1RQTLuCn5tuNe25Z999THYzj8yijmk0MaRE-SD0=")
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
                        //activate loading view

                    }
                    is AppState.Success -> {
                        binding?.apply {
                            state.data?.let {
                                fruitsAdapter.updateList(it)
                                binding?.rvFruitsTips?.layoutManager = LinearLayoutManager(requireContext())
                                fruitsAdapter.notifyDataSetChanged()
                                binding?.rvFruitsTips?.adapter = fruitsAdapter

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


//        binding?.apply {
//            rvFruitsTips.adapter = fruitsAdapter
//            rvFruitsTips.layoutManager = LinearLayoutManager(requireContext())
//        }
//        val dummyList = List(10) {
//            FruitsTips(
//                title = "Fruits Tips #$it",
//                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                photoUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=800&q=60"
//            )
//        }
//        fruitsAdapter.updateList(dummyList)

        return binding?.root
    }
}



