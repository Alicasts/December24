package com.alicasts.december24.utils

import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_RESPONSE

object Utils {

    fun buildRideHistoryRoute(customerId: String, driverId: String?, nullString: String): String {
        return buildString {
            append("$RIDE_HISTORY_RESPONSE/$customerId")
            if (!driverId.isNullOrEmpty() && driverId != nullString) {
                append("?driver_id=$driverId")
            }
        }
    }
}