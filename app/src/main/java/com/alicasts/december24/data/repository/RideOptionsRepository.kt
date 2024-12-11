package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.RideResponse

interface RideOptionsRepository {
    suspend fun getRideOptions(jsonAsString: String): RideResponse

    suspend fun submitRideRequest(
        customerId: String,
        origin: String,
        destination: String,
        distance: Double,
        duration: String,
        driverOption: DriverOption
    ): ConfirmRideResponse
}