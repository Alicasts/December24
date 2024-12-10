package com.alicasts.december24.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.Review

@Composable
fun DriverOptionCard(
    driverOption: DriverOption,
    isSelected: Boolean,
    onSelected: (DriverOption) -> Unit
) {
    val vehicleLabel = stringResource(R.string.vehicle)
    val evaluationLabel = stringResource(R.string.evaluation)
    val evaluationStarsDescription = stringResource(R.string.evaluation_stars)
    val pricePrefixLabel = stringResource(R.string.price_prefix)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelected(driverOption) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = driverOption.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = driverOption.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$vehicleLabel: " + driverOption.vehicle,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$evaluationLabel: ",
                    style = MaterialTheme.typography.bodyMedium
                )
                repeat(driverOption.review.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = evaluationStarsDescription,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pricePrefixLabel + driverOption.value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DriverOptionCardPreview() {

    val mockDriverOption = DriverOption(
        id = 1,
        name = "Homer Simpson",
        description = "Seu motorista camarada e descontraído.",
        vehicle = "Plymouth Valiant 1973",
        review = Review(
            rating = 4,
            comment = "Motorista amigável, mas demorou um pouco."
        ),
        value = 49.99
    )

    DriverOptionCard(
        driverOption = mockDriverOption,
        isSelected = true,
        onSelected = {}
    )
}