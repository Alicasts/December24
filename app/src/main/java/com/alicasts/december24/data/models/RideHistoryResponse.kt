package com.alicasts.december24.data.models

import com.google.gson.annotations.SerializedName

data class RideHistoryResponse(
    @SerializedName("customer_id")
    val customerId: String,
    val rides: List<Ride>
)


