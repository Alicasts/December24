package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.TravelResponse

interface TravelOptionsRepository {
    suspend fun getTravelOptions(jsonAsString: String): TravelResponse

    suspend fun submitRideRequest(
        customerId: String,
        origin: String,
        destination: String,
        distance: Double,
        duration: String,
        driverOption: DriverOption
    ): ConfirmRideResponse
}