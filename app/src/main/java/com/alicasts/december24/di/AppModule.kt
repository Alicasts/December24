package com.alicasts.december24.di

import android.content.Context
import com.alicasts.december24.utils.Constants.BASE_URL
import com.alicasts.december24.data.remote.RidesApi
import com.alicasts.december24.data.repository.ride_history.interfaces.RideHistoryRepository
import com.alicasts.december24.data.repository.ride_history.implementations.RideHistoryRepositoryImpl
import com.alicasts.december24.data.repository.ride_options.interfaces.RideOptionsRepository
import com.alicasts.december24.data.repository.ride_options.implementation.RideOptionsRepositoryImpl
import com.alicasts.december24.utils.DefaultStringResourceProvider
import com.alicasts.december24.utils.StringResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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
        api: RidesApi,
        stringResourceProvider: StringResourceProvider
    ): RideHistoryRepository {
        return RideHistoryRepositoryImpl(api, stringResourceProvider)
    }

    @Provides
    @Singleton
    fun provideRideOptionsRepository(
        api: RidesApi,
        stringResourceProvider: StringResourceProvider
    ): RideOptionsRepository {
        return RideOptionsRepositoryImpl(api, stringResourceProvider)
    }

    @Provides
    @Singleton
    fun provideStringResourceProvider(
        @ApplicationContext context: Context
    ): StringResourceProvider {
        return DefaultStringResourceProvider(context)
    }

}