package com.alicasts.december24.presentation.travel_options_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alicasts.december24.R
import com.alicasts.december24.data.models.Location

@Composable
fun StaticMap(
    origin: Location,
    destination: Location,
    viewModel: TravelOptionsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val mapUrl = remember { viewModel.buildStaticMapUrl(origin, destination) }
    Log.d("StaticMap", "Origin: ${origin.latitude}, ${origin.longitude}")
    Log.d("StaticMap", "Destination: ${destination.latitude}, ${destination.longitude}")
    Log.d("StaticMap", "Generated URL: $mapUrl")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = mapUrl,
            contentDescription = "Static Map showing route",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = painterResource(id = R.drawable.broken)
        )
    }
}