package com.alicasts.december24.presentation.components

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ElevatedCustomButton(
    text: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = { onClick() }
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewElevatedCustomButton() {
    ElevatedCustomButton(
        text = "Test",
        onClick = {})
}