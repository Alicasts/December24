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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alicasts.december24.presentation.components.DropdownMenuField
import com.alicasts.december24.presentation.components.ElevatedCustomButton

@Composable
fun RideHistoryRequestScreen(
    navController: NavController,
    viewModel: RideHistoryRequestViewModel = hiltViewModel()
) {
    val userIds by viewModel.userIds.observeAsState(emptyList())
    val driverIds by viewModel.driverIds.observeAsState(emptyList())

    var selectedUserId by remember { mutableStateOf(userIds.first()) }
    var selectedDriverId by remember { mutableStateOf(driverIds.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Request Ride History",
            style = MaterialTheme.typography.headlineMedium
        )

        DropdownMenuField(
            label = "Select User ID",
            options = userIds,
            selectedOption = selectedUserId,
            onOptionSelected = { selectedUserId = it }
        )

        DropdownMenuField(
            label = "Select Driver ID",
            options = driverIds,
            selectedOption = selectedDriverId,
            onOptionSelected = { selectedDriverId = it }
        )

        ElevatedCustomButton(
            onClick = {
                val route = viewModel.buildRideHistoryRoute(selectedUserId, selectedDriverId)
                navController.navigate(route = route)
            },
            text = "Submit"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RideHistoryRequestScreenPreview() {
    val mockUserIds = listOf("Null", "Qualquer", "CT01")
    val mockDriverIds = listOf("Null", "Qualquer", "1", "2", "3")

    RideHistoryRequestScreenPreviewContent(
        userIds = mockUserIds,
        driverIds = mockDriverIds
    )
}

@Composable
fun RideHistoryRequestScreenPreviewContent(
    userIds: List<String>,
    driverIds: List<String>
) {
    var selectedUserId by remember { mutableStateOf(userIds.first()) }
    var selectedDriverId by remember { mutableStateOf(driverIds.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Request Ride History",
            style = MaterialTheme.typography.headlineMedium
        )

        DropdownMenuField(
            label = "Select User ID",
            options = userIds,
            selectedOption = selectedUserId,
            onOptionSelected = { selectedUserId = it }
        )

        DropdownMenuField(
            label = "Select Driver ID",
            options = driverIds,
            selectedOption = selectedDriverId,
            onOptionSelected = { selectedDriverId = it }
        )

        ElevatedCustomButton(
            onClick = { },
            text = "Submit"
        )
    }
}