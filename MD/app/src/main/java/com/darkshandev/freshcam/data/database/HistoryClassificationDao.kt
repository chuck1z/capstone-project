package com.darkshandev.freshcam.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darkshandev.freshcam.data.models.HistoryClassificationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface HistoryClassificationDao {
    @Query("SELECT * from HistoryClassificationEntity")
    fun getHistories(): Flow<List<HistoryClassificationEntity>>
    @Insert()
    suspend fun addHistory(newHistory:HistoryClassificationEntity)
    @Query("DELETE from HistoryClassificationEntity")
    suspend fun clearHistories()
}