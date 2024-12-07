package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.Ride

interface RideHistoryRepository {
    suspend fun getRideHistory(customerId: String, driverId: String): List<Ride>
}