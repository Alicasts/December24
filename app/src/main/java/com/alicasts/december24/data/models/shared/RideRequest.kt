package com.alicasts.december24.data.models.shared

data class RideRequest(
    val customerId: String,
    val origin: String,
    val destination: String
)
