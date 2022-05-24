package com.darkshandev.freshcam.presentation.fruits

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.data.FruitsTips
import com.darkshandev.freshcam.databinding.ItemFruitsTipsBinding
import com.darkshandev.freshcam.utils.FruitTipsDiffUtils

class FruitsAdapter() : RecyclerView.Adapter<FruitsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemFruitsTipsBinding
    private var currentList= emptyList<FruitsTips>();
    fun updateList(newList: List<FruitsTips>){
       //update currentList with newList using FruitTipsDiffUtils
        val diff = DiffUtil
            .calculateDiff(FruitTipsDiffUtils(currentList, newList))
        this.currentList = newList

        diff.dispatchUpdatesTo(this)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsAdapter.ViewHolder {
        binding = ItemFruitsTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruitsTips = currentList[position]
        with(binding){
            Glide.with(binding.root.context)
                .load(fruitsTips.photoUrl)
                .into(ivFruitTips)
            tvTitleTips.text = fruitsTips.title
            tvDescTips.text = fruitsTips.description
        }
    }

    override fun getItemCount(): Int = currentList.size

}