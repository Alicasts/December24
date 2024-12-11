package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alicasts.december24.R
import com.alicasts.december24.data.models.ride_history.Ride
import com.alicasts.december24.presentation.components.DropdownMenuField
import com.alicasts.december24.presentation.components.EmptyStateMessage
import com.alicasts.december24.presentation.components.ErrorMessage
import com.alicasts.december24.presentation.components.LoadingIndicator
import com.alicasts.december24.presentation.ride_history_response_screen.components.RideHistoryItem
import com.alicasts.december24.utils.Resource

@Composable
fun RideHistoryResponseScreen(
    customerId: String,
    driverId: String,
    viewModel: RideHistoryResponseViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchRideHistory(customerId, driverId)
    }
    val localMessageString = stringResource(R.string.local_message)
    val unknownErrorString = stringResource(R.string.unknown_error)
    val successfulEmptyResponseString = stringResource(R.string.successful_empty_response)
    val noRideHistoryAvailableString = stringResource(R.string.no_ride_history_available)
    val allString = stringResource(R.string.all)

    val rideHistory by viewModel.rideHistory.observeAsState()
    val filteredRides by viewModel.filteredRides.observeAsState(emptyList())
    val driverIds = viewModel.driverIds
    val selectedDriverId by viewModel.selectedDriverId.observeAsState(allString)


    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        when (val state = rideHistory) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error ->  ShowError(state, localMessageString, unknownErrorString)
            is Resource.Success -> {
                if (state.data.isNullOrEmpty()) {
                    EmptyStateMessage(errorMessage = successfulEmptyResponseString + noRideHistoryAvailableString)

                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            DropdownMenuField(
                                label = stringResource(R.string.select_user_id),
                                options = driverIds,
                                selectedOption = selectedDriverId,
                                onOptionSelected = { newDriverId ->
                                    viewModel.updateSelectedDriverId(newDriverId)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(filteredRides) { ride ->
                            RideHistoryItem(ride)
                        }
                    }
                }
            }
            null -> LoadingIndicator()
        }
    }
}

@Composable
private fun ShowError(
    state: Resource.Error<List<Ride>>,
    localMessageString: String,
    unknownErrorString: String
) {
    val errorMessage = state.message ?: (localMessageString + unknownErrorString)
    ErrorMessage(errorMessage)
}