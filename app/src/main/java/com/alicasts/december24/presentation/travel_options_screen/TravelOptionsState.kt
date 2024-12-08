package com.alicasts.december24.presentation.travel_options_screen

import com.alicasts.december24.data.models.TravelResponse

data class TravelOptionsState(
    val isLoading: Boolean = false,
    val travelResponse: TravelResponse? = null,
    val error: String = ""
)
