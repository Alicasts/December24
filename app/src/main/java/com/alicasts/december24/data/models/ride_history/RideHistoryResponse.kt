package com.alicasts.december24.data.models.ride_history

import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.google.gson.annotations.SerializedName

data class RideHistoryResponse(
    @SerializedName(CUSTOMER_ID)
    val customerId: String,
    val rides: List<Ride>
)


