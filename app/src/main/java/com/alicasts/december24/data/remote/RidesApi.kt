package com.alicasts.december24.data.remote

import com.alicasts.december24.data.models.RideHistoryResponse
import com.alicasts.december24.data.models.TravelResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface RidesApi {
    @GET
    suspend fun getRide(@Url url: String): RideHistoryResponse

    @POST("ride/estimate")
    suspend fun getTravelOptions(
        @Body request: Map<String, String>
    ): TravelResponse

}