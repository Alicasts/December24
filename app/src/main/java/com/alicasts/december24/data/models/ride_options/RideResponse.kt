package com.alicasts.december24.data.models.ride_options

import com.alicasts.december24.data.models.shared.DriverOption
import com.alicasts.december24.data.models.shared.Location

data class RideResponse(
    val origin: Location,
    val destination: Location,
    val distance: Int,
    val duration: String,
    val options: List<DriverOption>,
    val routeResponse: Any?
)
