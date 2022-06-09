package com.darkshandev.freshcam.presentation.classifier.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentHistoryBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.presentation.fruits.HistoryAdapter

class HistoryFragment : Fragment() {
    private val classifierViewmodel:ClassifierViewmodel by activityViewModels()
    private var _binding:FragmentHistoryBinding? = null
    private val historyAdapter: HistoryAdapter = HistoryAdapter()

    override fun onDestroy() {
        _binding=null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding=FragmentHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpView()
        lifecycleScope.launchWhenResumed {
            classifierViewmodel.classificationHistory.collect{
                historyAdapter.updateList(it)
            }
        }
        return _binding?.root
    }

    private fun setUpView() {
        _binding?.apply {
            rvHistory.apply {
                layoutManager=LinearLayoutManager(requireActivity())
                adapter=historyAdapter
            }
        }
    }

}