package com.alicasts.december24.presentation.travel_options_screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alicasts.december24.R
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.presentation.components.ConfirmTravelPopup
import com.alicasts.december24.presentation.components.ErrorMessage
import com.alicasts.december24.presentation.components.LoadingIndicator
import com.alicasts.december24.presentation.components.TravelOptionsContent
import com.alicasts.december24.utils.Resource


@Composable
fun TravelOptionsScreen(
    navController: NavController,
    requestJsonAsString: String,
    viewModel: TravelOptionsViewModel = hiltViewModel()
) {
    val travelRequest by viewModel.travelRequest.observeAsState()
    val state by viewModel.state.observeAsState()
    val submitState by viewModel.submitState.observeAsState()

    val unknownErrorString = stringResource(R.string.unknown_error)
    val nullString = stringResource(R.string.null_string)

    val hasNavigated = remember { mutableStateOf(false) }
    val (selectedDriverOption, setSelectedDriverOption) = remember { mutableStateOf<DriverOption?>(null) }

    LaunchedEffect(Unit) {
        viewModel.parseTravelRequest(requestJsonAsString)
        viewModel.fetchTravelOptions(requestJsonAsString)
    }

    when (state) {
        is Resource.Loading -> LoadingIndicator()
        is Resource.Error -> ErrorMessage((state as Resource.Error).message ?: unknownErrorString)
        is Resource.Success -> {
            val response = (state as Resource.Success).data!!
            TravelOptionsContent(
                response = response,
                origin = travelRequest?.origin.orEmpty(),
                destination = travelRequest?.destination.orEmpty(),
                onOptionSelected = setSelectedDriverOption
            )
        }
        else -> Unit
    }

    if (submitState != null && !hasNavigated.value) {
        when (submitState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Success -> {
                val response = (submitState as Resource.Success).data
                if (response?.success == true) {
                    val successfulRideMessage = stringResource(R.string.ride_confirmed_successfully)
                    Toast.makeText(
                        LocalContext.current,
                        successfulRideMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.returnTravelHistoryRoute(nullString)?.let { route ->
                        navController.navigate(route)
                        hasNavigated.value = true
                        viewModel.resetSubmitState()
                    }
                }
            }
            is Resource.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    (submitState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    selectedDriverOption?.let {
        ConfirmTravelPopup(
            driverOption = it,
            onDismiss = { setSelectedDriverOption(null) },
            onConfirm = { confirmedDriver ->
                viewModel.submitRideRequest(
                    distance = (state as Resource.Success).data!!.distance.toDouble(),
                    duration = (state as Resource.Success).data!!.duration,
                    driverOption = confirmedDriver
                )
            }
        )
    }
}