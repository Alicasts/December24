package com.alicasts.december24.data.repository.ride_options.interfaces

import com.alicasts.december24.data.models.confirm_ride.ConfirmRideResponse
import com.alicasts.december24.data.models.shared.DriverOption
import com.alicasts.december24.data.models.ride_options.RideResponse

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