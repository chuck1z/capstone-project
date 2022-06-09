package com.darkshandev.freshcam.presentation.classifier.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.getFreshness
import com.darkshandev.freshcam.data.models.getName
import com.darkshandev.freshcam.databinding.FragmentScanResultBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import com.darkshandev.freshcam.utils.asPercentage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class ScanResultFragment : BottomSheetDialogFragment() {
    private val classifierViewmodel: ClassifierViewmodel by activityViewModels()
    private val fruitsViewmodel: FruitsViewmodel by activityViewModels()
    private var binding: FragmentScanResultBinding? = null
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentScanResultBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupCollector()
        setupView()
        return binding?.root
    }

    private fun setupView() {
        binding?.apply {
            button.setOnClickListener {
                fruitsViewmodel.setSelectedFruitsId("name")
                findNavController().navigate(R.id.action_scanResultFragment_to_detailFragment)
            }
        }
    }

    private fun setupCollector() {
        lifecycleScope.launch {
            classifierViewmodel.image.flowWithLifecycle(lifecycle).collect {
                it?.let {
                    binding?.imageView3?.setImageURI(it.toUri())
                }
            }
        }
        lifecycleScope.launch {
            classifierViewmodel.result.flowWithLifecycle(lifecycle).collect { state ->
                when (state) {
                    is AppState.Success -> {
                        showContent()

                        binding?.apply {
                            progressBar.visibility = View.GONE
                            errorMessage.visibility = View.GONE
                            state.data?.let {
                                if (it.getFreshness().lowercase().contains("fresh")) {
                                    label.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.fresh
                                        )
                                    )
                                    confidence.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.fresh
                                        )
                                    )
                                } else {
                                    label.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.rotten
                                        )
                                    )
                                    confidence.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.rotten
                                        )
                                    )
                                }
                                label.text = it.getFreshness()
                                confidence.text = it.confidence.asPercentage()
                                descInfo.text = it.description
                                name.text = it.getName()

                            }
                        }
                    }
                    is AppState.Loading -> {
                        hideContent()
                        binding?.apply {
                            progressBar.visibility = View.VISIBLE
                            errorMessage.visibility = View.GONE
                        }
                    }
                    is AppState.Error -> {
                        hideContent()
                        binding?.apply {
                            progressBar.visibility = View.GONE
                            errorMessage.text = state.message
                            errorMessage.visibility = View.VISIBLE

                        }
                    }
                    else -> {
                        hideContent()
                    }
                }

            }
        }
    }

    private fun hideContent() {
        binding?.apply {
            button.visibility = View.GONE
            imageView3.visibility = View.GONE
            label.visibility = View.GONE
            confidence.visibility = View.GONE
            descInfo.visibility = View.GONE
            name.visibility = View.GONE
        }
    }

    private fun showContent() {
        binding?.apply {
            button.visibility = View.VISIBLE
            imageView3.visibility = View.VISIBLE
            label.visibility = View.VISIBLE
            confidence.visibility = View.VISIBLE
            descInfo.visibility = View.VISIBLE
            name.visibility = View.VISIBLE
        }
    }

}