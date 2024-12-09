package com.alicasts.december24.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alicasts.december24.data.models.DriverOption

@Composable
fun DriverOptionsList(options: List<DriverOption>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(options) { driverOption ->
            DriverOptionCard(
                driverOption = driverOption,
                isSelected = false,
                onSelected = { }
            )
        }
    }
}