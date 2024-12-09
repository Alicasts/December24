package com.alicasts.december24.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.data.models.TravelResponse

@Composable
fun TravelOptionsContent(response: TravelResponse) {
    val successfulResponseString = stringResource(R.string.successful_response_no_drivers)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StaticMapWithPlaceholder(
            origin = response.origin,
            destination = response.destination
        )

        if (response.options.isEmpty()) {
            EmptyStateMessage(successfulResponseString)
        } else {
            DriverOptionsList(options = response.options)
        }
    }
}
