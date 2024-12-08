package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.TravelResponse

interface TravelOptionsRepository {
    suspend fun getTravelOptions(customerId: String, origin: String, destination: String): TravelResponse
}