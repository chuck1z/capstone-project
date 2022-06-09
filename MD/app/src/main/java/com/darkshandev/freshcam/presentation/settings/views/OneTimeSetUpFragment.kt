package com.darkshandev.freshcam.presentation.settings.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.databinding.FragmentHistoryBinding
import com.darkshandev.freshcam.databinding.FragmentOneTimeSetUpBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.presentation.fruits.HistoryAdapter
import kotlinx.coroutines.flow.collect

class OneTimeSetUpFragment : Fragment() {

    private val classifierViewmodel: ClassifierViewmodel by activityViewModels()
    private var _binding: FragmentOneTimeSetUpBinding? = null
    private var isSuccess=false
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = FragmentOneTimeSetUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding?.apply {
            closeButton.setOnClickListener {
                findNavController().navigate(R.id.action_oneTimeSetUpFragment_to_homeFruitsFragment)
            }
            retry.setOnClickListener {
                classifierViewmodel.getLatestModel()
                classifierViewmodel.getLatestLabel()
            }
            goToGuide.setOnClickListener {
                findNavController().navigate(R.id.action_oneTimeSetUpFragment_to_userGuideFragment)
            }
        }
        lifecycleScope.launchWhenResumed {
            classifierViewmodel.isConnectionAvailable.collect{
                _binding?.apply {
                    if(it){
                        onInternetLost.visibility=View.GONE
                        if (isSuccess){
                            onSuccess.visibility=View.VISIBLE
                        }else{
                            preparing.visibility=View.VISIBLE
                        }
                    }else{
                        onInternetLost.visibility=View.VISIBLE
                        onSuccess.visibility=View.GONE
                        preparing.visibility=View.GONE
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
classifierViewmodel.downloadStatus.collect{
    _binding?.apply {
        when(it){
            is AppState.Error -> {
                onInternetLost.visibility=View.VISIBLE
                onSuccess.visibility=View.GONE
                preparing.visibility=View.GONE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            is AppState.Initial -> {
                classifierViewmodel.getLatestModel()
                classifierViewmodel.getLatestLabel()
            }
            is AppState.Loading -> {
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                onInternetLost.visibility=View.GONE
                onSuccess.visibility=View.GONE
                preparing.visibility=View.VISIBLE
            }
            is AppState.Success -> {
                classifierViewmodel.markAsLaunched()
                onInternetLost.visibility=View.GONE
                onSuccess.visibility=View.VISIBLE
                preparing.visibility=View.GONE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }
}
        }
        return _binding?.root
    }
}