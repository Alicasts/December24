package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.Ride
import com.alicasts.december24.data.repository.RideHistoryRepository
import com.alicasts.december24.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RideHistoryResponseViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideHistoryResponseViewModel
    private val repository = mockk<RideHistoryRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockDriver = HistoryResponseDriver(id = 1, name = "John Doe")
    private val mockRides = listOf(
        Ride(1, "2023-12-08", "Origin1", "Destination1", 10.5, "15 min", mockDriver, 50.0),
        Ride(2, "2023-12-08", "Origin2", "Destination2", 20.0, "25 min", mockDriver, 100.0)
    )
    private val errorMessage = "An error occurred"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RideHistoryResponseViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createObserver() = mockk<Observer<Resource<List<Ride>>>>(relaxed = true)

    @Test
    fun `fetchRideHistory should post Loading and then Success`() = runTest {
        coEvery { repository.getRideHistory(any(), any()) } returns mockRides
        val observer = createObserver()
        viewModel.rideHistory.observeForever(observer)

        viewModel.fetchRideHistory("customerId", "driverId")

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Success && it.data == mockRides })
        }
    }

    @Test
    fun `fetchRideHistory should post Error when repository throws exception`() = runTest {
        coEvery { repository.getRideHistory(any(), any()) } throws Exception(errorMessage)
        val observer = createObserver()
        viewModel.rideHistory.observeForever(observer)

        viewModel.fetchRideHistory("customerId", "driverId")

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Error && it.message == errorMessage })
        }
    }
}