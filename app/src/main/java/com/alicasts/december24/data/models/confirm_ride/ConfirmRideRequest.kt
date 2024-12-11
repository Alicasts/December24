package com.alicasts.december24.data.models.confirm_ride

import com.alicasts.december24.data.models.shared.HistoryResponseDriver
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.google.gson.annotations.SerializedName

data class ConfirmRideRequest(
    @SerializedName(CUSTOMER_ID)
    val customerId: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: HistoryResponseDriver,
    val value: Double
)
