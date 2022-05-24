package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.freshcam.data.models.FruitsTips
import com.darkshandev.freshcam.databinding.FragmentHomeFruitsBinding
import com.darkshandev.freshcam.presentation.fruits.FruitsAdapter
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel


class HomeFruitsFragment : Fragment() {

    private  var binding: FragmentHomeFruitsBinding? = null
    private val fruitsViewmodel: FruitsViewmodel by viewModels()
    private val adapter = FruitsAdapter()
    override fun onDestroy() {
        binding=null
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
        binding?.apply {
            rvFruitsTips.adapter = adapter
            rvFruitsTips.layoutManager =LinearLayoutManager(requireContext())
        }
        val dummyList=List(10){
            FruitsTips(
                title = "Fruits Tips #$it",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                photoUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=800&q=60"
            )
        }
        adapter.updateList(dummyList)
        return binding?.root
    }
}