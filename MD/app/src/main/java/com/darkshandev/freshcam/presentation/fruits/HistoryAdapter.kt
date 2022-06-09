package com.darkshandev.freshcam.presentation.fruits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.data.models.HistoryClassificationEntity
import com.darkshandev.freshcam.databinding.ItemHistoryBinding
import com.darkshandev.freshcam.utils.HistoryClassificationDiffUtils
import com.darkshandev.freshcam.utils.asPercentage
import java.io.File

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemHistoryBinding

    private var currentList = emptyList<HistoryClassificationEntity>()
    fun updateList(newList: List<HistoryClassificationEntity>) {
        //update currentList with newList using HistoryClassificationDiffUtils
        val diff = DiffUtil
            .calculateDiff(HistoryClassificationDiffUtils(currentList, newList))
        this.currentList = newList
        diff.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val history = currentList[position]
        with(binding) {
            Glide.with(binding.root.context)
                .load(File(history.photo))
                .into(ivFruitsHistory)
            val text =
                "${history.confidence.asPercentage()} ${if (history.freshness) "Fresh" else "Rotten"}"
            tvFruitName.text = history.fruitsName
            tvDetection.text = text
        }


    }

    override fun getItemCount(): Int = currentList.size

}