package com.darkshandev.freshcam.presentation.fruits

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.FruitsTips
import com.darkshandev.freshcam.data.models.Tips
import com.darkshandev.freshcam.databinding.ItemFruitsTipsBinding
import com.darkshandev.freshcam.utils.FruitTipsDiffUtils

class FruitsAdapter : RecyclerView.Adapter<FruitsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemFruitsTipsBinding
    private var currentList : List<Tips> = ArrayList()
    fun updateList(newList: List<Tips>) {
        //update currentList with newList using FruitTipsDiffUtils
        this.currentList = newList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsAdapter.ViewHolder {
        binding = ItemFruitsTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruitsTips = currentList[position]
        Log.d(TAG, "onBindViewHolder: $fruitsTips")
        with(binding) {
            Glide.with(binding.root.context)
                .load(fruitsTips.image)
                .into(ivFruitTips)
            tvTitleTips.text = fruitsTips.title
            tvDescTips.text = fruitsTips.short_desc
        }
        holder.itemView.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_homeFruitsFragment_to_tipsDetailFragment)
        }
    }

    override fun getItemCount(): Int = currentList.size

}