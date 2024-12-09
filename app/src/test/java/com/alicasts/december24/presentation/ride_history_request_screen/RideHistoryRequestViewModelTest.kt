package com.alicasts.december24.presentation.ride_history_request_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.utils.Constants
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class RideHistoryRequestViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideHistoryRequestViewModel

    @Before
    fun setUp() {
        val stringProvider = StringProvider("Qualquer", "Null")
        viewModel = RideHistoryRequestViewModel(stringProvider)
    }

    @Test
    fun `userIds should be initialized correctly`() {
        val expectedUserIds = listOf("Qualquer", VALID_USER_ID)

        val observer = mock<Observer<List<String>>>()
        viewModel.userIds.observeForever(observer)

        assertEquals(expectedUserIds, viewModel.userIds.value)
        viewModel.userIds.removeObserver(observer)
    }

    @Test
    fun `driverIds should be initialized correctly`() {
        val expectedDriverIds = listOf("Null", "Qualquer") + Constants.VALID_DRIVERS.map { it.driverId.toString() }

        val observer = mock<Observer<List<String>>>()
        viewModel.driverIds.observeForever(observer)

        assertEquals(expectedDriverIds, viewModel.driverIds.value)
        viewModel.driverIds.removeObserver(observer)
    }
}