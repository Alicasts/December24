package com.alicasts.december24.presentation.components

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ElevatedCustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    debounceTime: Long = 1000
) {
    var isClickable by remember { mutableStateOf(true) }

    ElevatedButton(
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(debounceTime)
                    isClickable = true
                }
            }
        },
        modifier = modifier
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