package com.darkshandev.freshcam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [ClassifierLabelDao::class, ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classifierLabelDao(): ClassifierLabelDao
}