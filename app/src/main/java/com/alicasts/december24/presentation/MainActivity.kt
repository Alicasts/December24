package com.alicasts.december24.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alicasts.december24.R
import com.alicasts.december24.presentation.components.ElevatedCustomButton
import com.alicasts.december24.presentation.navigation.AppNavHost
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_REQUEST
import com.alicasts.december24.presentation.navigation.Routes.RIDE_REQUEST
import com.alicasts.december24.presentation.theme.December24Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun CenteredButtons(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCustomButton(
                onClick = { navController.navigate( RIDE_HISTORY_REQUEST ) },
                text = stringResource(R.string.request_ride_history))
            ElevatedCustomButton(
                onClick = { navController.navigate( RIDE_REQUEST ) },
                text = stringResource(R.string.ride_request))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CenteredButtonsPreview() {
    val fakeNavController = rememberNavController()
    December24Theme {
        CenteredButtons(fakeNavController)
    }
}