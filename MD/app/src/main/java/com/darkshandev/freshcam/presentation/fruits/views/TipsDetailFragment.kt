package com.darkshandev.freshcam.presentation.fruits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.data.models.AppState
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
        binding?.apply {
            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        lifecycleScope.launch {
            fruitsViewmodel.tipssDetail.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Loading -> {
                        //activate loading view
                        binding?.apply {
                            progressTipsDetail.visibility = View.VISIBLE
                            detailView.visibility = View.GONE
                            errorTipsMessage.visibility = View.GONE
                        }
                    }
                    is AppState.Success -> {

                        binding?.apply {
                            progressTipsDetail.visibility = View.GONE
                            detailView.visibility = View.VISIBLE
                            errorTipsMessage.visibility = View.GONE

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
                        binding?.apply {
                            progressTipsDetail.visibility = View.GONE
                            detailView.visibility = View.GONE
                            errorTipsMessage.text = state.message
                            errorTipsMessage.visibility = View.VISIBLE
                        }
                    }
                    is AppState.Initial -> {

                    }
                }
            }
        }
        return binding?.root
    }

}