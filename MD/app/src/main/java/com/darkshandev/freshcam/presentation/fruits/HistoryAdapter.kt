package com.darkshandev.freshcam.presentation.fruits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemHistoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        with(binding) {
            Glide.with(binding.root.context)
//                .load()
//                .into(ivFruitsHistory)
//            tvFruitName.text =
//            tvDetection.text =
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}