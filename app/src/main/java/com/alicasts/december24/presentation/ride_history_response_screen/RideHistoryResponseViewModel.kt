package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.R
import com.alicasts.december24.data.models.ride_history.Ride
import com.alicasts.december24.data.repository.ride_history.interfaces.RideHistoryRepository
import com.alicasts.december24.utils.Constants.VALID_DRIVERS
import com.alicasts.december24.utils.Resource
import com.alicasts.december24.utils.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryResponseViewModel @Inject constructor(
    stringResourceProvider: StringResourceProvider,
    private val repository: RideHistoryRepository
) : ViewModel() {
    private val _rideHistory = MutableLiveData<Resource<List<Ride>>>()
    val rideHistory: LiveData<Resource<List<Ride>>> = _rideHistory

    private val _filteredRides = MutableLiveData<List<Ride>>()
    val filteredRides: LiveData<List<Ride>> = _filteredRides

    private val allString = stringResourceProvider.getString(R.string.all)

    private val _selectedDriverId = MutableLiveData(allString)
    val selectedDriverId: LiveData<String> = _selectedDriverId

    val driverIds = listOf(stringResourceProvider.getString(R.string.all)) + VALID_DRIVERS.map { it.driverId.toString() }

    fun fetchRideHistory(customerId: String, driverId: String) {
        viewModelScope.launch {
            _rideHistory.value = Resource.Loading()
            try {
                val rides = repository.getRideHistory(customerId, driverId)
                _rideHistory.value = Resource.Success(rides)
                applyFilter(allString)
            } catch (e: Exception) {
                val errorMessage = e.message
                _rideHistory.value = errorMessage?.let { Resource.Error(it) }
            }
        }
    }

    fun applyFilter(selectedDriverId: String) {
        val currentRides = (_rideHistory.value as? Resource.Success)?.data ?: emptyList()
        _filteredRides.value = if (selectedDriverId == allString) {
            currentRides
        } else {
            currentRides.filter { it.driver.id.toString() == selectedDriverId }
        }
    }

    fun updateSelectedDriverId(driverId: String) {
        _selectedDriverId.value = driverId
        applyFilter(driverId)
    }
}