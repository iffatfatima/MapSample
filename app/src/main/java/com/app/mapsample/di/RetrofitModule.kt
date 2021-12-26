package com.app.mapsample.di

import androidx.viewbinding.BuildConfig
import com.app.codesamples.network.data.network.DirectionsService
import com.app.codesamples.network.data.repository.DirectionsRepositoryImpl
import com.app.mapsample.domain.repository.DirectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    fun provideBaseUrl() = "https://www.google.com"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideDataRepository(directionsRepositoryImpl: DirectionsRepositoryImpl): DirectionsRepository = directionsRepositoryImpl

    @Provides
    @Singleton
    fun provideDataService(retrofit: Retrofit): DirectionsService = retrofit.create(DirectionsService::class.java)

}