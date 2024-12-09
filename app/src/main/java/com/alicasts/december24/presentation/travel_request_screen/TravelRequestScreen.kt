package com.alicasts.december24.presentation.travel_request_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.DropdownMenuField
import com.alicasts.december24.presentation.components.ElevatedCustomButton

@Composable
fun TravelRequestScreen(
    navController: NavController,
    viewModel: TravelRequestViewModel = hiltViewModel()
) {
    val userIds by viewModel.userIds.observeAsState(emptyList())
    val addresses by viewModel.addresses.observeAsState(emptyList())

    val travelRequestString = stringResource(R.string.travel_request)
    val selectUserIdString = stringResource(R.string.select_user_id)
    val selectOriginAddressString = stringResource(R.string.select_origin_address)
    val selectDestinationAddressString = stringResource(R.string.select_destination_address)
    val submitString = stringResource(R.string.submit)

    var selectedUserId by remember { mutableStateOf(userIds.first()) }
    var selectedOrigin by remember { mutableStateOf(addresses.first()) }
    var selectedDestination by remember { mutableStateOf(addresses.last()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = travelRequestString,
            style = MaterialTheme.typography.headlineMedium
        )

        DropdownMenuField(
            label = selectUserIdString,
            options = userIds,
            selectedOption = selectedUserId,
            onOptionSelected = { selectedUserId = it }
        )

        DropdownMenuField(
            label = selectOriginAddressString,
            options = addresses,
            selectedOption = selectedOrigin,
            onOptionSelected = { selectedOrigin = it }
        )

        DropdownMenuField(
            label = selectDestinationAddressString,
            options = addresses,
            selectedOption = selectedDestination,
            onOptionSelected = { selectedDestination = it }
        )

        ElevatedCustomButton(
            onClick = {
                val route = viewModel.buildRouteStringWithTravelRequestJson(
                    userId = selectedUserId,
                    origin = selectedOrigin,
                    destination = selectedDestination
                )
                navController.navigate(route)
            },
            text = submitString
        )
    }
}