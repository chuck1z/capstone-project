package com.darkshandev.freshcam.utils

import androidx.recyclerview.widget.DiffUtil
import com.darkshandev.freshcam.data.models.HistoryClassificationEntity

class HistoryClassificationDiffUtils(
    private val oldList: List<HistoryClassificationEntity>,
    private val newList: List<HistoryClassificationEntity>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList == newList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when (old.id) {
            latest.id -> true
            else -> false
        }
    }
}