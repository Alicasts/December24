package com.alicasts.december24.presentation.ride_request_screen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alicasts.december24.R
import com.alicasts.december24.presentation.navigation.Routes.RIDE_OPTIONS
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.DESTINATION
import com.alicasts.december24.presentation.navigation.RoutesArguments.ORIGIN
import com.alicasts.december24.utils.Constants.VALID_ADDRESSES
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import com.alicasts.december24.utils.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RideRequestViewModel @Inject constructor(
    stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _userIds = MutableLiveData<List<String>>()
    val userIds: LiveData<List<String>> = _userIds

    private val _addresses = MutableLiveData<List<String>>()
    val addresses: LiveData<List<String>> = _addresses

    private val nullString = stringResourceProvider.getString(R.string.null_string)
    private val anyString = stringResourceProvider.getString(R.string.any_string)

    init {
        _userIds.value = listOf(
            nullString,
            anyString,
            VALID_USER_ID
        )

        _addresses.value = listOf(
            nullString,
            anyString,
            *VALID_ADDRESSES.toTypedArray()
        )
    }

    fun buildRouteStringWithRideRequestJson(
        userId: String,
        origin: String,
        destination: String
    ): String {
        val json = JSONObject().apply {
            put(CUSTOMER_ID, if (userId == nullString) null else userId)
            put(ORIGIN, if (origin == nullString) null else origin)
            put(DESTINATION, if (destination == nullString) null else destination)
        }.toString()

        return buildString {
            append(RIDE_OPTIONS)
            append("?json=${Uri.encode(json)}")
        }
    }
}