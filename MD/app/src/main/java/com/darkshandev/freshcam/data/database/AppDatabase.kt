package com.darkshandev.freshcam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darkshandev.freshcam.data.models.ClassifierLabelEntity
import com.darkshandev.freshcam.data.models.HistoryClassificationEntity


@Database(
    entities = [ClassifierLabelEntity::class, HistoryClassificationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classifierLabelDao(): ClassifierLabelDao
    abstract fun historyClassificationDao(): HistoryClassificationDao
}