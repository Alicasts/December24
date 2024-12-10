package com.alicasts.december24.presentation.travel_options_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alicasts.december24.R
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.Review
import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.presentation.components.DriverOptionsList
import com.alicasts.december24.presentation.components.ElevatedCustomButton
import com.alicasts.december24.presentation.components.EmptyStateMessage
import com.alicasts.december24.presentation.components.ErrorMessage
import com.alicasts.december24.presentation.components.LoadingIndicator
import com.alicasts.december24.presentation.components.StaticMapWithPlaceholder
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
        is Resource.Error -> {
            val errorMessage = (state as Resource.Error).message ?: unknownErrorString
            ErrorMessage(errorMessage)
        }
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

    submitState?.let { submitState ->
        if (!hasNavigated.value) {
            when (submitState) {
                is Resource.Loading -> {
                    LoadingIndicator()
                }
                is Resource.Success -> {
                    val response = submitState.data
                    if (response != null && response.success) {
                        Toast.makeText(
                            LocalContext.current,
                            "Ride confirmed successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        val route = viewModel.returnTravelHistoryRoute(nullString = nullString)
                        if (route != null) {
                            navController.navigate(route)
                            hasNavigated.value = true
                            viewModel.resetSubmitState()
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        submitState.message ?: "Error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    if (selectedDriverOption != null) {
        DriverOptionPopup(
            driverOption = selectedDriverOption,
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

@Composable
fun TravelOptionsContent(
    response: TravelResponse,
    origin: String,
    destination: String,
    onOptionSelected: (DriverOption) -> Unit
) {
    val successfulResponseString = stringResource(R.string.successful_response_no_drivers)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (response.options.isEmpty()) {
            EmptyStateMessage(successfulResponseString)
        } else {
            Text(text = "Origin: $origin")
            Text(text = "Destination: $destination")
            Text(text = "Distance: ${response.distance}")
            Text(text = "Duration: ${response.duration}")

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
fun DriverOptionPopup(
    driverOption: DriverOption,
    onDismiss: () -> Unit,
    onConfirm: (DriverOption) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.confirm),
                fontStyle = Italic,
                fontWeight = Bold,
                textAlign = TextAlign.Center
            )
                },
        text = {
            Column {
                Text(
                    text = "Driver: ${driverOption.name}",
                    style = typography.bodyLarge
                )
                Text(
                    text = "Value: R$ ${driverOption.value}",
                    style = typography.bodyLarge
                )
            }
        },
        confirmButton = {
            ElevatedCustomButton(
                onClick = { onConfirm(driverOption) },
                text = stringResource(R.string.submit)
            )
        },
        dismissButton = {
            ElevatedCustomButton(
                onClick = onDismiss,
                text = stringResource(R.string.cancel)
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
    )
}

@Preview(showBackground = true)
@Composable
fun DriverOptionPopupPreview() {
    MaterialTheme {
        DriverOptionPopup(
            driverOption = DriverOption(
                id = 1,
                name = "John Doe",
                description = "Experienced driver",
                vehicle = "Sedan",
                review = Review(
                    rating = 5,
                    comment = "Great experience"
                ),
                value = 100.0
            ),
            onDismiss = {},
            onConfirm = {}
        )
    }
}