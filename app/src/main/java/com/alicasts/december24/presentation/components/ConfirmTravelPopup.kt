package com.alicasts.december24.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.Review

@Composable
fun ConfirmTravelPopup(
    driverOption: DriverOption,
    onDismiss: () -> Unit,
    onConfirm: (DriverOption) -> Unit
) {
    val driverNameLabel = stringResource(R.string.driver_name_label)
    val pricePrefixLabel = stringResource(R.string.price_prefix)
    val driverIdLabel = stringResource(R.string.driver_id_label)
    val confirmTripLabel = stringResource(R.string.confirm_trip)
    val submitLabel = stringResource(R.string.submit)
    val cancelLabel = stringResource(R.string.cancel)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = confirmTripLabel,
                fontStyle = Italic,
                fontWeight = Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$driverNameLabel: ${driverOption.name}",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$driverIdLabel: ${driverOption.id}",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$pricePrefixLabel${driverOption.value}",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedCustomButton(
                    onClick = { onConfirm(driverOption) },
                    text = submitLabel,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                ElevatedCustomButton(
                    onClick = onDismiss,
                    text = cancelLabel,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmTravelPopupPreview() {
    MaterialTheme {
        ConfirmTravelPopup(
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