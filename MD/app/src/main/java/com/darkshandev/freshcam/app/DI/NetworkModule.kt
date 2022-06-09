package com.darkshandev.freshcam.app.DI

import com.darkshandev.freshcam.BuildConfig
import com.darkshandev.freshcam.app.Config
import com.darkshandev.freshcam.data.networks.ClassifierService
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(
        interceptor: HttpLoggingInterceptor,

        ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideModelDownloader(): FirebaseModelDownloader = FirebaseModelDownloader.getInstance()
    @Singleton
    @Provides
    fun provideRemoteConfig():FirebaseRemoteConfig {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
        return remoteConfig
    }
    @Singleton
    @Provides
    fun provideRetrofitClient(
        client: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(Config.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    @Singleton
    @Provides
    fun provideClassifierService(
        retrofit: Retrofit
    ):ClassifierService=retrofit
        .create(ClassifierService::class.java)
}
