package com.alicasts.december24.di

import com.alicasts.december24.utils.Constants.BASE_URL
import com.alicasts.december24.data.remote.RidesApi
import com.alicasts.december24.data.repository.RideHistoryRepository
import com.alicasts.december24.data.repository.RideHistoryRepositoryImpl
import com.alicasts.december24.data.repository.TravelOptionsRepository
import com.alicasts.december24.data.repository.TravelOptionsRepositoryImpl
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
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRidesApi(retrofit: Retrofit): RidesApi {
        return retrofit.create(RidesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRideHistoryRepository(
        api: RidesApi
    ): RideHistoryRepository {
        return RideHistoryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideTravelOptionsRepository(
        api: RidesApi
    ): TravelOptionsRepository {
        return TravelOptionsRepositoryImpl(api)
    }

}