package com.alicasts.december24.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.Ride

@Composable
fun RideItem(ride: Ride) {
    val dateLabelString = stringResource(R.string.date_label)
    val originLabelString = stringResource(R.string.origin_label)
    val destinationLabelString = stringResource(R.string.destination_label)
    val driverNameLabelString = stringResource(R.string.driver_name_label)
    val driverIdLabelString = stringResource(R.string.driver_id_label)
    val pricePrefixString = stringResource(R.string.price_prefix)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "$dateLabelString: ${ride.date}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "$originLabelString: ${ride.origin}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "$destinationLabelString: ${ride.destination}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "$driverNameLabelString: ${ride.driver.name}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "$driverIdLabelString: ${ride.driver.id}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "$pricePrefixString: $${ride.value}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun RideItemPreview() {
    val mockRide = Ride(
        id = 1,
        date = "2024-12-21T17:30:16",
        origin = "3585 Karl Route, 953, Alberthacester, 11534",
        destination = "68829 Damon Streets, 352, West Wilburn, 02191-1703",
        distance = 22.22,
        driver = HistoryResponseDriver(id = 2, name = "Homer Simpson"),
        value = 214.55,
        duration = "232323"
    )

    MaterialTheme {
        RideItem(ride = mockRide)
    }
}