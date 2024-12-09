package com.alicasts.december24.presentation.travel_options_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.data.models.Location
import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.data.repository.TravelOptionsRepository
import com.alicasts.december24.utils.Resource
import com.alicasts.december24.utils.Secrets.MAPBOX_API_KEY
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

    private val _state = MutableLiveData<Resource<TravelResponse>>()
    val state: LiveData<Resource<TravelResponse>> = _state

    fun fetchTravelOptions(json: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading()

            try {
                val (customerId, origin, destination) = parseJson(json)
                val travelResponse = repository.getTravelOptions(customerId, origin, destination)
                _state.value = Resource.Success(travelResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = e.message?.let { Resource.Error(it) }
            }
        }
    }

    private fun parseJson(json: String): Triple<String, String, String> {
        val jsonObject = JSONObject(json)
        val customerId = jsonObject.getString("customer_id")
        val origin = jsonObject.getString("origin")
        val destination = jsonObject.getString("destination")
        return Triple(customerId, origin, destination)
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

}