package com.darkshandev.freshcam.utils

import androidx.recyclerview.widget.DiffUtil
import com.darkshandev.freshcam.data.models.FruitsTips
import com.darkshandev.freshcam.data.models.Tips

class SimpleTipsDiffUtils(
    private val oldList: List<String>,
    private val newList: List<String>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList == newList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when (old) {
            latest -> true
            else -> false
        }
    }
}