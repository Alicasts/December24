package com.alicasts.december24.presentation.ride_history_request_screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.DropdownMenuField
import com.alicasts.december24.presentation.components.ElevatedCustomButton
import com.alicasts.december24.presentation.mocks.FakeStringResourceProvider
import com.alicasts.december24.utils.StringResourceProvider

@Composable
fun RideHistoryRequestScreen(
    navController: NavController,
    viewModel: RideHistoryRequestViewModel = hiltViewModel()
) {
    val userIds by viewModel.userIds.observeAsState(emptyList())
    val driverIds by viewModel.driverIds.observeAsState(emptyList())

    var selectedUserId by remember { mutableStateOf(userIds.first()) }
    var selectedDriverId by remember { mutableStateOf(driverIds.first()) }

    val requestRideHistoryString = stringResource(R.string.request_ride_history)
    val selectUseIdString = stringResource(R.string.select_user_id)
    val selectedDriverIdString = stringResource(R.string.select_driver_id)
    val submitString = stringResource(R.string.submit)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = requestRideHistoryString,
            style = MaterialTheme.typography.headlineMedium
        )

        DropdownMenuField(
            label = selectUseIdString,
            options = userIds,
            selectedOption = selectedUserId,
            onOptionSelected = { selectedUserId = it }
        )

        DropdownMenuField(
            label = selectedDriverIdString,
            options = driverIds,
            selectedOption = selectedDriverId,
            onOptionSelected = { selectedDriverId = it }
        )

        ElevatedCustomButton(
            onClick = {
                val route = viewModel.returnRideHistoryRoute(selectedUserId, selectedDriverId)
                navController.navigate(route = route)
            },
            text = submitString
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RideHistoryRequestScreenPreview() {
    val fakeNavController = rememberNavController()
    val fakeViewModel = object : RideHistoryRequestViewModel(FakeStringResourceProvider()) {
        override val userIds = MutableLiveData(listOf("User1", "User2"))
        override val driverIds = MutableLiveData(listOf("Driver1", "Driver2"))
    }

    RideHistoryRequestScreen(
        navController = fakeNavController,
        viewModel = fakeViewModel
    )
}