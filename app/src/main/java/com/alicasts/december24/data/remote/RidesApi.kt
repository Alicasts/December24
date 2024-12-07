package com.alicasts.december24.data.remote

import com.alicasts.december24.data.models.RideHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface RidesApi {
    @GET
    suspend fun getRide(@Url url: String): RideHistoryResponse
}