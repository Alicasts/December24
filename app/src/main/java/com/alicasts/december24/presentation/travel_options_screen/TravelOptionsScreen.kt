package com.alicasts.december24.presentation.travel_options_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.ErrorMessage
import com.alicasts.december24.presentation.components.LoadingIndicator
import com.alicasts.december24.presentation.components.TravelOptionsContent
import com.alicasts.december24.utils.Resource

@Composable
fun TravelOptionsScreen(
    requestJsonAsString: String,
    viewModel: TravelOptionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    val unknownErrorString = stringResource(R.string.unknown_error)

    LaunchedEffect(Unit) {
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
            TravelOptionsContent(response = response)
        }
        else -> Unit
    }
}