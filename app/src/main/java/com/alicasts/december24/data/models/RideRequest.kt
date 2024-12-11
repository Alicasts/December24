package com.alicasts.december24.data.models

data class RideRequest(
    val customerId: String,
    val origin: String,
    val destination: String
)
