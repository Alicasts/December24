package com.alicasts.december24.presentation.ride_history_request_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alicasts.december24.R
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_RESPONSE
import com.alicasts.december24.utils.Constants
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import com.alicasts.december24.utils.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RideHistoryRequestViewModel @Inject constructor(
    stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _userIds = MutableLiveData<List<String>>()
    open val userIds: LiveData<List<String>> = _userIds

    private val _driverIds = MutableLiveData<List<String>>()
    open val driverIds: LiveData<List<String>> = _driverIds

    private val nullString = stringResourceProvider.getString(R.string.null_string)
    private val anyString = stringResourceProvider.getString(R.string.any_string)

    init {
        _userIds.value = initializeUserIds()
        _driverIds.value = initializeDriverIds()
    }

    private fun initializeUserIds(): List<String> {
        return listOf(
            anyString,
            VALID_USER_ID
        )
    }

    private fun initializeDriverIds(): List<String> {
        val validDriverIds = Constants.VALID_DRIVERS.map { it.driverId.toString() }
        return listOf(
            nullString,
            anyString
        ) + validDriverIds
    }

    fun buildRideHistoryRoute(customerId: String, driverId: String?): String {
        return buildString {
            append("$RIDE_HISTORY_RESPONSE/$customerId")
            if (!driverId.isNullOrEmpty() && driverId != nullString) {
                append("?driver_id=$driverId")
            }
        }
    }
}