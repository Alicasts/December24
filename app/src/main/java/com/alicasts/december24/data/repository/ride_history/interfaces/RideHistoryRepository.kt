package com.alicasts.december24.data.repository.ride_history.interfaces

import com.alicasts.december24.data.models.ride_history.Ride

interface RideHistoryRepository {
    suspend fun getRideHistory(customerId: String, driverId: String): List<Ride>
}