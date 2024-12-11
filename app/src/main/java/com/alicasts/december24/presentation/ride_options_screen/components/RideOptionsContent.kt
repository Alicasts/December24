package com.alicasts.december24.presentation.ride_options_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.data.models.shared.DriverOption
import com.alicasts.december24.data.models.ride_options.RideResponse
import com.alicasts.december24.presentation.components.EmptyStateMessage

@Composable
fun RideOptionsContent(
    response: RideResponse,
    origin: String,
    destination: String,
    onOptionSelected: (DriverOption) -> Unit
) {
    val noDriversMessage = stringResource(R.string.successful_response_no_drivers)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (response.options.isEmpty()) {
            EmptyStateMessage(noDriversMessage)
        } else {
            RideDetails(
                origin = origin,
                destination = destination,
                distance = response.distance.toString(),
                duration = response.duration
            )
            StaticMapWithPlaceholder(
                origin = response.origin,
                destination = response.destination
            )
            DriverOptionsList(
                options = response.options,
                onSelected = onOptionSelected
            )
        }
    }
}

@Composable
fun RideDetails(
    origin: String,
    destination: String,
    distance: String,
    duration: String
) {
    val originLabel = stringResource(R.string.origin_label)
    val destinationLabel = stringResource(R.string.destination_label)
    val distanceLabel = stringResource(R.string.distance_label)
    val durationLabel = stringResource(R.string.duration_label)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "$originLabel: $origin")
        Text(text = "$destinationLabel: $destination")
        Text(text = "$distanceLabel: $distance")
        Text(text = "$durationLabel: $duration")
    }
}