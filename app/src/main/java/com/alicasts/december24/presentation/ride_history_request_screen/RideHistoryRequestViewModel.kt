package com.alicasts.december24.presentation.ride_history_request_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_RESPONSE
import com.alicasts.december24.utils.Constants
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RideHistoryRequestViewModel @Inject constructor() : ViewModel() {

    private val _userIds = MutableLiveData<List<String>>()
    val userIds: LiveData<List<String>> = _userIds

    private val _driverIds = MutableLiveData<List<String>>()
    val driverIds: LiveData<List<String>> = _driverIds

    init {
        _userIds.value = listOf(
            "Qualquer",
            VALID_USER_ID
        )

        val validDriverIds = Constants.VALID_DRIVERS.map { "${it.driverId}" }
        _driverIds.value = listOf(
            "Null",
            "Qualquer"
        ) + validDriverIds
    }

    fun buildRideHistoryRoute(customerId: String, driverId: String?): String {
        return buildString {
            append("$RIDE_HISTORY_RESPONSE/$customerId")
            if (!driverId.isNullOrEmpty() && driverId != "Null") {
                append("?driver_id=$driverId")
            }
        }
    }
}