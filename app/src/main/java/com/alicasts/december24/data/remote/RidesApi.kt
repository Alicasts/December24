package com.alicasts.december24.data.remote

import com.alicasts.december24.data.models.ConfirmRideRequest
import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.RideHistoryResponse
import com.alicasts.december24.data.models.RideResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Url

interface RidesApi {
    @GET
    suspend fun getRide(@Url url: String): RideHistoryResponse

    @POST("ride/estimate")
    suspend fun getRideOptions(
        @Body request: Map<String, String>
    ): RideResponse

    @PATCH("ride/confirm")
    suspend fun confirmRide(
        @Body request: ConfirmRideRequest
    ): ConfirmRideResponse

}