package com.darkshandev.freshcam.presentation.fruits

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.data.FruitsTips
import com.darkshandev.freshcam.databinding.ItemFruitsTipsBinding

class FruitsAdapter(private val listFruitsTips: List<FruitsTips>) : RecyclerView.Adapter<FruitsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemFruitsTipsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsAdapter.ViewHolder {
        binding = ItemFruitsTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruitsTips = listFruitsTips[position]
        with(binding){
            Glide.with(binding.root.context)
                .load(fruitsTips.photoUrl)
                .into(ivFruitTips)
            tvTitleTips.text = fruitsTips.title
            tvDescTips.text = fruitsTips.description
        }
    }

    override fun getItemCount(): Int = listFruitsTips.size

}