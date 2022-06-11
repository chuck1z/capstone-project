package com.darkshandev.freshcam.presentation.fruits

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.freshcam.databinding.SimpleTipsItemBinding
import com.darkshandev.freshcam.utils.SimpleTipsDiffUtils

class SimpleTipsAdapter : RecyclerView.Adapter<SimpleTipsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: SimpleTipsItemBinding
    private var currentList: List<String> = ArrayList()
    fun updateList(newList: List<String>) {
        //update currentList with newList using SimpleTipsDiffUtils
        val diffaUtils = SimpleTipsDiffUtils(currentList, newList)
        this.currentList = newList
        DiffUtil.calculateDiff(diffaUtils).dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = SimpleTipsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruitsTips = currentList[position]
        Log.d(TAG, "onBindViewHolder: $fruitsTips")
        with(binding) {
            textView.text = fruitsTips
        }

    }

    override fun getItemCount(): Int = currentList.size

}