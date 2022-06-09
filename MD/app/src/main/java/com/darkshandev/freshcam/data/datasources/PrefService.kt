package com.darkshandev.freshcam.data.datasources

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("preferences")

class PrefService @Inject constructor(@ApplicationContext appContext: Context){
    private val dataStore = appContext.dataStore
    val isFirstLaunch: Flow<Boolean> =dataStore.data.map {
        it[IS_FIRST_LAUNCH_KEY]?:true
    }
    suspend fun marksAsLaunched(){
        dataStore.edit {
            it[IS_FIRST_LAUNCH_KEY]=false
        }
    }
    companion object {
        private val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("IS_FIRST_LAUNCH")

    }
}