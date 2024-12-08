package com.alicasts.december24.presentation.travel_request_screen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alicasts.december24.presentation.navigation.Routes.TRAVEL_OPTIONS
import com.alicasts.december24.utils.Constants.VALID_ADDRESSES
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import com.alicasts.december24.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class TravelRequestViewModel @Inject constructor(
    stringProvider: StringProvider
) : ViewModel() {

    private val _userIds = MutableLiveData<List<String>>()
    val userIds: LiveData<List<String>> = _userIds

    private val _addresses = MutableLiveData<List<String>>()
    val addresses: LiveData<List<String>> = _addresses

    init {
        _userIds.value = listOf(
            stringProvider.getNullString(),
            stringProvider.getAnyString(),
            VALID_USER_ID
        )

        _addresses.value = listOf(
            stringProvider.getNullString(),
            stringProvider.getAnyString(),
            *VALID_ADDRESSES.toTypedArray()
        )
    }

    fun buildRouteStringWithTravelRequestJson(
        userId: String,
        origin: String,
        destination: String
    ): String {
        val json = JSONObject().apply {
            put("customer_id", if (userId == "Null") null else userId)
            put("origin", if (origin == "Null") null else origin)
            put("destination", if (destination == "Null") null else destination)
        }.toString()

        return buildString {
            append(TRAVEL_OPTIONS)
            append("?json=${Uri.encode(json)}")
        }
    }
}