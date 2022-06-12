package com.darkshandev.freshcam.app.DI

import android.content.Context
import androidx.room.Room
import com.darkshandev.freshcam.app.Config
import com.darkshandev.freshcam.data.database.AppDatabase
import com.darkshandev.freshcam.data.database.ClassifierLabelDao
import com.darkshandev.freshcam.data.database.HistoryClassificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object LocalModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase = Room
        .databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Config.DB_Name
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideClassifierLabelDao(appDatabase: AppDatabase): ClassifierLabelDao =
        appDatabase.classifierLabelDao()

    @Provides
    @Singleton
    fun provideHistoryClassificationDao(appDatabase: AppDatabase): HistoryClassificationDao =
        appDatabase.historyClassificationDao()
}
