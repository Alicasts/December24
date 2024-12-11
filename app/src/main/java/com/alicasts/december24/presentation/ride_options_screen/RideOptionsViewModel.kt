package com.alicasts.december24.presentation.ride_options_screen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.R
import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.Location
import com.alicasts.december24.data.models.RideRequest
import com.alicasts.december24.data.models.RideResponse
import com.alicasts.december24.data.repository.RideOptionsRepository
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.DESTINATION
import com.alicasts.december24.presentation.navigation.RoutesArguments.ORIGIN
import com.alicasts.december24.utils.Resource
import com.alicasts.december24.utils.Secrets.MAPBOX_API_KEY
import com.alicasts.december24.utils.StringResourceProvider
import com.alicasts.december24.utils.Utils.buildRideHistoryRoute
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
open class RideOptionsViewModel @Inject constructor(
    private val repository: RideOptionsRepository,
    stringResourceProvider: StringResourceProvider
) : ViewModel() {

    val _rideRequest = MutableLiveData<RideRequest>()
    val rideRequest: LiveData<RideRequest> = _rideRequest

    val _state = MutableLiveData<Resource<RideResponse>>()
    val state: LiveData<Resource<RideResponse>> = _state

    private val _submitState = MutableLiveData<Resource<ConfirmRideResponse>?>()
    val submitState: LiveData<Resource<ConfirmRideResponse>?> = _submitState

    private val unknownErrorMessage = stringResourceProvider.getString(R.string.unknown_error)

    fun fetchRideOptions(jsonAsString: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading()

            try {
                val rideResponse = repository.getRideOptions(jsonAsString)
                _state.value = Resource.Success(rideResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = e.message?.let { Resource.Error(it) }
            }
        }
    }

    fun parseRideRequest(json: String) {
        try {
            val jsonObject = JSONObject(Uri.decode(json))
            val rideRequest = RideRequest(
                customerId = jsonObject.optString(CUSTOMER_ID),
                origin = jsonObject.optString(ORIGIN),
                destination = jsonObject.optString(DESTINATION)
            )
            _rideRequest.value = rideRequest
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun submitRideRequest(
        distance: Double,
        duration: String,
        driverOption: DriverOption
    ) {
        viewModelScope.launch {
            val rideRequest = _rideRequest.value ?: return@launch
            _submitState.value = Resource.Loading()
            try {
                val response = repository.submitRideRequest(
                    customerId = rideRequest.customerId,
                    origin = rideRequest.origin,
                    destination = rideRequest.destination,
                    distance = distance,
                    duration = duration,
                    driverOption = driverOption
                )
                _submitState.value = Resource.Success(response)
            } catch (e: Exception) {
                _submitState.value = Resource.Error(e.message ?: unknownErrorMessage)
            }
        }
    }

    fun resetSubmitState() {
        _submitState.value = null
    }

    fun buildStaticMapUrl(origin: Location, destination: Location): String {
        val pathCoordinates = encodePolyline(origin, destination)
        return try {

            buildString {
                append("https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/")
                append("pin-s-a+9ed4bd(${origin.longitude},${origin.latitude}),")
                append("pin-s-b+000(${destination.longitude},${destination.latitude}),")
                append("path-5+f44-0.5($pathCoordinates)/")
                append("auto/600x400")
                append("?access_token=$MAPBOX_API_KEY")
            }
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    private fun encodePolyline(origin: Location, destination: Location): String {
        val coordinates = listOf(
            LatLng(origin.latitude, origin.longitude),
            LatLng(destination.latitude, destination.longitude)
        )

        return PolyUtil.encode(coordinates)
    }

    fun returnRideHistoryRoute(nullString: String): String? {
        val rideRequest = rideRequest.value
        val currentState = state.value

        if (rideRequest == null || currentState !is Resource.Success) {
            return null
        }

        val driverId = currentState.data?.options?.firstOrNull()?.id.toString()

        return buildRideHistoryRoute(
            customerId = rideRequest.customerId,
            driverId = driverId,
            nullString = nullString
        )
    }
}
