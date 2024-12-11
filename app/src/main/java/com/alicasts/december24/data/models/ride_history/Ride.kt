package com.alicasts.december24.data.models.ride_history

import com.alicasts.december24.data.models.shared.HistoryResponseDriver

data class Ride(
    val id: Int,
    val date: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: HistoryResponseDriver,
    val value: Double
)