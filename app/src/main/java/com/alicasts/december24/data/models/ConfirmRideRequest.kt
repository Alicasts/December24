package com.alicasts.december24.data.models

import com.google.gson.annotations.SerializedName

data class ConfirmRideRequest(
    @SerializedName("customer_id")
    val customerId: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: HistoryResponseDriver,
    val value: Double
)
