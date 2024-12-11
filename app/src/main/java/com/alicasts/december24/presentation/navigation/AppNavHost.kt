package com.alicasts.december24.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alicasts.december24.presentation.CenteredButtons
import com.alicasts.december24.presentation.navigation.Routes.HOME
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_REQUEST
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_RESPONSE
import com.alicasts.december24.presentation.navigation.Routes.RIDE_REQUEST
import com.alicasts.december24.presentation.navigation.Routes.RIDE_OPTIONS
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.DRIVER_ID
import com.alicasts.december24.presentation.ride_history_request_screen.RideHistoryRequestScreen
import com.alicasts.december24.presentation.ride_history_response_screen.RideHistoryResponseScreen
import com.alicasts.december24.presentation.ride_options_screen.RideOptionsScreen
import com.alicasts.december24.presentation.ride_request_screen.RideRequestScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HOME,
        modifier = modifier
    ) {
        composable(route = HOME) {
            CenteredButtons(navController)
        }
        composable(route = RIDE_HISTORY_REQUEST) {
            RideHistoryRequestScreen(navController = navController)
        }
        composable(
            route = "${RIDE_HISTORY_RESPONSE}/{$CUSTOMER_ID}?$DRIVER_ID={$DRIVER_ID}",
            arguments = listOf(
                navArgument(CUSTOMER_ID) { type = NavType.StringType },
                navArgument(DRIVER_ID) { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString(CUSTOMER_ID).orEmpty()
            val driverId = backStackEntry.arguments?.getString(DRIVER_ID).orEmpty()

            RideHistoryResponseScreen(customerId = customerId, driverId = driverId)
        }
        composable(route = RIDE_REQUEST) {
            RideRequestScreen(navController = navController)
        }
        composable(
            route = "$RIDE_OPTIONS?json={json}",
            arguments = listOf(
                navArgument("json") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("json") ?: "{}"
            RideOptionsScreen(
                navController = navController,
                requestJsonAsString = json
            )
        }
    }
}