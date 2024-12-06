package com.alicasts.december24.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.ElevatedCustomButton
import com.alicasts.december24.presentation.theme.December24Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            December24Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CenteredButtons(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CenteredButtons(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCustomButton(
                onClick = { },
                text = stringResource(R.string.historico_de_viagens))
            ElevatedCustomButton(
                onClick = { },
                text = stringResource(R.string.opces_de_viagem))
            ElevatedCustomButton(
                onClick = { },
                text = stringResource(R.string.solicitaco_de_viagem))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CenteredButtonsPreview() {
    December24Theme {
        CenteredButtons()
    }
}