package com.alicasts.december24.data.remote

import com.alicasts.december24.data.model.RideHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RidesApi {
    @GET("ride/{customer_id}")
    suspend fun getRide(
        @Path("customer_id") customerId: String,
        @Query("driver_id") driverId: Int
    ): RideHistoryResponse

}