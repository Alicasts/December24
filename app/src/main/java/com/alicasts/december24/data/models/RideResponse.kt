package com.alicasts.december24.data.models

data class RideResponse(
    val origin: Location,
    val destination: Location,
    val distance: Int,
    val duration: String,
    val options: List<DriverOption>,
    val routeResponse: Any?
)
