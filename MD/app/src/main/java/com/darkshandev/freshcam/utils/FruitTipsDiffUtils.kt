package com.darkshandev.freshcam.utils

import androidx.recyclerview.widget.DiffUtil
import com.darkshandev.freshcam.data.models.FruitsTips

class FruitTipsDiffUtils(private val oldList: List<FruitsTips>, private val newList: List<FruitsTips>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList == newList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when (old.title) {
            latest.title -> true
            else -> false
        }
    }
}