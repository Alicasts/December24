package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.data.models.ride_history.Ride
import com.alicasts.december24.data.repository.ride_history.interfaces.RideHistoryRepository
import com.alicasts.december24.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryResponseViewModel @Inject constructor(
    private val repository: RideHistoryRepository,
) : ViewModel() {
    private val _rideHistory = MutableLiveData<Resource<List<Ride>>>()
    val rideHistory: LiveData<Resource<List<Ride>>> = _rideHistory

    fun fetchRideHistory(customerId: String, driverId: String) {
        viewModelScope.launch {
            _rideHistory.value = Resource.Loading()
            try {
                val rides = repository.getRideHistory(customerId, driverId)
                _rideHistory.value = Resource.Success(rides)
            } catch (e: Exception) {
                val errorMessage = e.message
                _rideHistory.value = errorMessage?.let { Resource.Error(it) }
            }
        }
    }
}