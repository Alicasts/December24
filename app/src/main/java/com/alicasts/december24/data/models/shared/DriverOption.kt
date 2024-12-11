package com.alicasts.december24.data.models.shared

data class DriverOption(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
)
