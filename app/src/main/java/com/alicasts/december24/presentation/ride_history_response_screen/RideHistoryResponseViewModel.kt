package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alicasts.december24.data.repository.RideHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryResponseViewModel @Inject constructor(
    private val repository: RideHistoryRepository
) : ViewModel() {
    private val _state = MutableLiveData(RideHistoryState())
    val state: LiveData<RideHistoryState> = _state

    fun fetchRideHistory(customerId: String, driverId: String) {
        viewModelScope.launch {
            _state.value = _state.value?.copy(isLoading = true)
            try {
                val rides = repository.getRideHistory(customerId, driverId)
                _state.value = RideHistoryState(rides = rides)
            } catch (e: Exception) {
                _state.value = e.message?.let { RideHistoryState(error = it) }
            }
        }
    }
}