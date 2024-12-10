package com.alicasts.december24.presentation.travel_options_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.Location
import com.alicasts.december24.data.models.TravelRequest
import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.data.repository.TravelOptionsRepository
import com.alicasts.december24.utils.Resource
import com.alicasts.december24.utils.Secrets.MAPBOX_API_KEY
import com.alicasts.december24.utils.Utils.buildRideHistoryRoute
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class TravelOptionsViewModel @Inject constructor(
    private val repository: TravelOptionsRepository,
) : ViewModel() {

    private val _travelRequest = MutableLiveData<TravelRequest>()
    val travelRequest: LiveData<TravelRequest> = _travelRequest

    private val _state = MutableLiveData<Resource<TravelResponse>>()
    val state: LiveData<Resource<TravelResponse>> = _state

    private val _submitState = MutableLiveData<Resource<ConfirmRideResponse>?>()
    val submitState: LiveData<Resource<ConfirmRideResponse>?> = _submitState

    fun fetchTravelOptions(jsonAsString: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading()

            try {
                val travelResponse = repository.getTravelOptions(jsonAsString)
                _state.value = Resource.Success(travelResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = e.message?.let { Resource.Error(it) }
            }
        }
    }

    fun parseTravelRequest(json: String) {
        try {
            val jsonObject = JSONObject(Uri.decode(json))
            val travelRequest = TravelRequest(
                customerId = jsonObject.optString("customer_id", "Unknown"),
                origin = jsonObject.optString("origin", "Unknown"),
                destination = jsonObject.optString("destination", "Unknown")
            )
            _travelRequest.value = travelRequest
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
            val travelRequest = _travelRequest.value ?: return@launch
            _submitState.value = Resource.Loading()
            try {
                val response = repository.submitRideRequest(
                    customerId = travelRequest.customerId,
                    origin = travelRequest.origin,
                    destination = travelRequest.destination,
                    distance = distance,
                    duration = duration,
                    driverOption = driverOption
                )
                _submitState.value = Resource.Success(response)
            } catch (e: Exception) {
                _submitState.value = Resource.Error(e.message ?: "Unknown error")
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
            Log.e("StaticMap", "Error generating Mapbox URL: ${e.message}")
            ""
        }
    }

    private fun encodePolyline(origin: Location, destination: Location): String {
        val coordinates = listOf(
            LatLng(origin.latitude, origin.longitude),
            LatLng(destination.latitude, destination.longitude)
        )

        return PolyUtil.encode(coordinates)
    }

    fun returnTravelHistoryRoute(nullString: String): String? {
        val travelRequest = travelRequest.value
        val currentState = state.value

        if (travelRequest == null || currentState !is Resource.Success) {
            return null
        }

        val driverId = currentState.data?.options?.firstOrNull()?.id.toString()

        return buildRideHistoryRoute(
            customerId = travelRequest.customerId,
            driverId = driverId,
            nullString = nullString
        )
    }
}

