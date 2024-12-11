package com.alicasts.december24.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alicasts.december24.R
import com.alicasts.december24.data.models.Location
import com.alicasts.december24.presentation.ride_options_screen.RideOptionsViewModel

@Composable
fun StaticMapWithPlaceholder(
    origin: Location,
    destination: Location,
    viewModel: RideOptionsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val mapUrl = remember { viewModel.buildStaticMapUrl(origin, destination) }
    var isLoading by remember { mutableStateOf(true) }
    val staticMapDescriptionString = stringResource(R.string.static_map_description)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }

        AsyncImage(
            model = mapUrl,
            contentDescription = staticMapDescriptionString,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
            error = painterResource(id = R.drawable.broken)
        )
    }
}