package com.darkshandev.freshcam.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.darkshandev.freshcam.data.models.ClassifierLabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassifierLabelDao {
    @Query("SELECT * FROM ClassifierLabelEntity")
    fun getLabels(): Flow<List<ClassifierLabelEntity>>
   @Query("SELECT * FROM ClassifierLabelEntity WHERE `index` = :index")
    fun getLabel(index: Int): ClassifierLabelEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(label: ClassifierLabelEntity)
    @Query("DELETE FROM ClassifierLabelEntity")
    suspend fun deleteAll()
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun update(rows: List<ClassifierLabelEntity>)
}