package com.darkshandev.sutori.app.di

import android.content.Context
import androidx.room.Room
import com.darkshandev.sutori.app.Config
import com.darkshandev.sutori.data.database.AppDatabase
import com.darkshandev.sutori.data.database.RemoteKeysDao
import com.darkshandev.sutori.data.database.StoryDao
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
        ).build()

    @Provides
    fun provideStoryDao(appDatabase: AppDatabase): StoryDao {
        return appDatabase.storyDao()
    }

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.getRepoDao()
    }
}