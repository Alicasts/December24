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
import com.alicasts.december24.presentation.navigation.Routes.TRAVEL_REQUEST
import com.alicasts.december24.presentation.navigation.Routes.TRAVEL_OPTIONS
import com.alicasts.december24.presentation.ride_history_request_screen.RideHistoryRequestScreen
import com.alicasts.december24.presentation.ride_history_response_screen.RideHistoryResponseScreen
import com.alicasts.december24.presentation.travel_options_screen.TravelOptionsScreen
import com.alicasts.december24.presentation.travel_request_screen.TravelRequestScreen

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
            route = "${RIDE_HISTORY_RESPONSE}/{customer_id}?driver_id={driver_id}",
            arguments = listOf(
                navArgument("customer_id") { type = NavType.StringType },
                navArgument("driver_id") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customer_id").orEmpty()
            val driverId = backStackEntry.arguments?.getString("driver_id").orEmpty()

            RideHistoryResponseScreen(customerId = customerId, driverId = driverId)
        }
        composable(route = TRAVEL_REQUEST) {
            TravelRequestScreen(navController = navController)
        }
        composable(
            route = "$TRAVEL_OPTIONS?json={json}",
            arguments = listOf(
                navArgument("json") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("json") ?: "{}"
            TravelOptionsScreen(json = json)
        }
    }
}