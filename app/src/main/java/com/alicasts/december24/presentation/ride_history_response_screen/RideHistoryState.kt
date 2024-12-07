package com.alicasts.december24.presentation.ride_history_response_screen

import com.alicasts.december24.data.models.Ride

data class RideHistoryState(
    val rides: List<Ride> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
