package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.EmptyStateMessage
import com.alicasts.december24.presentation.components.ErrorMessage
import com.alicasts.december24.presentation.components.LoadingIndicator
import com.alicasts.december24.presentation.components.RideItem
import com.alicasts.december24.utils.Resource

@Composable
fun RideHistoryResponseScreen(
    customerId: String,
    driverId: String,
    viewModel: RideHistoryResponseViewModel = hiltViewModel()
) {

    val rideHistory by viewModel.rideHistory.observeAsState()
            LaunchedEffect(Unit) {
        viewModel.fetchRideHistory(customerId, driverId)
    }

    val localMessageString = stringResource(R.string.local_message)
    val unknownErrorString = stringResource(R.string.unknown_error)
    val successfulEmptyResponseString = stringResource(R.string.successful_empty_response)
    val noRideHistoryAvailableString = stringResource(R.string.no_ride_history_available)


    when (val state = rideHistory) {
        is Resource.Loading -> LoadingIndicator()

        is Resource.Error -> {
            val errorMessage = state.message ?: (localMessageString + unknownErrorString)
            ErrorMessage(errorMessage)
        }

        is Resource.Success -> {
            if (state.data.isNullOrEmpty()) {
                EmptyStateMessage(
                    errorMessage = successfulEmptyResponseString + noRideHistoryAvailableString
                )

            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.data) { ride ->
                        RideItem(ride)
                    }
                }
            }
        }
        null -> LoadingIndicator()
    }
}