package com.alicasts.december24.data.models

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