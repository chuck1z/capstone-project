package com.darkshandev.freshcam.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.darkshandev.freshcam.data.models.ClassifierLabelEntity
import kotlinx.coroutines.flow.Flow


interface ClassifierLabelDao {
    @Query("SELECT * FROM ClassifierLabelEntity")
   suspend fun getLabels(): Flow<List<ClassifierLabelEntity>>
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun update(rows: List<ClassifierLabelEntity>)
}