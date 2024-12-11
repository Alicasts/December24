package com.alicasts.december24.presentation.ride_history_request_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.presentation.mocks.FakeStringResourceProvider
import com.alicasts.december24.presentation.navigation.Routes.RIDE_HISTORY_RESPONSE
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
        val fakeStringResourceProvider = FakeStringResourceProvider()
        viewModel = RideHistoryRequestViewModel(fakeStringResourceProvider)
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

    @Test
    fun `buildRideHistoryRoute should append driver_id when provided`() {
        val customerId = "CT01"
        val driverId = "Driver123"

        val result = viewModel.returnRideHistoryRoute(customerId, driverId)

        assertEquals("$RIDE_HISTORY_RESPONSE/$customerId?driver_id=$driverId", result)
    }

    @Test
    fun `buildRideHistoryRoute should not append driver_id when null`() {
        val customerId = "CT01"
        val driverId = null

        val result = viewModel.returnRideHistoryRoute(customerId, driverId)

        assertEquals("$RIDE_HISTORY_RESPONSE/$customerId", result)
    }

    @Test
    fun `buildRideHistoryRoute should not append driver_id when Null string`() {
        val customerId = "CT01"
        val driverId = "Null"

        val result = viewModel.returnRideHistoryRoute(customerId, driverId)

        assertEquals("$RIDE_HISTORY_RESPONSE/$customerId", result)
    }
}