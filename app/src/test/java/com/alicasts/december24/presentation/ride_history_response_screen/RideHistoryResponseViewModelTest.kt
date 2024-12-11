package com.alicasts.december24.presentation.ride_history_response_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.data.models.shared.HistoryResponseDriver
import com.alicasts.december24.data.models.ride_history.Ride
import com.alicasts.december24.data.repository.ride_history.interfaces.RideHistoryRepository
import com.alicasts.december24.presentation.mocks.FakeStringResourceProvider
import com.alicasts.december24.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
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
    private val stringResourceProvider = FakeStringResourceProvider()

    private val mockDriver = HistoryResponseDriver(id = 1, name = "John Doe")
    private val mockRides = listOf(
        Ride(1, "2023-12-08", "Origin1", "Destination1", 10.5, "15 min", mockDriver, 50.0),
        Ride(2, "2023-12-08", "Origin2", "Destination2", 20.0, "25 min", mockDriver, 100.0)
    )
    private val errorMessage = "An error occurred"
    private val allString = "Fake String"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RideHistoryResponseViewModel(stringResourceProvider, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createObserver() = mockk<Observer<Resource<List<Ride>>>>(relaxed = true)
    private fun createFilteredRidesObserver() = mockk<Observer<List<Ride>>>(relaxed = true)

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

    @Test
    fun `applyFilter with All should post all rides`() = runTest {
        coEvery { repository.getRideHistory(any(), any()) } returns mockRides
        val filteredObserver = createFilteredRidesObserver()
        viewModel.filteredRides.observeForever(filteredObserver)

        viewModel.fetchRideHistory("customerId", "driverId")
        viewModel.applyFilter(allString)

        verify {
            filteredObserver.onChanged(mockRides)
        }
    }

    @Test
    fun `applyFilter with specific driverId should post filtered rides`() = runTest {
        coEvery { repository.getRideHistory(any(), any()) } returns mockRides
        val filteredObserver = createFilteredRidesObserver()
        viewModel.filteredRides.observeForever(filteredObserver)

        viewModel.fetchRideHistory("customerId", "driverId")
        viewModel.applyFilter("1") // Filter by driverId = 1

        verify {
            filteredObserver.onChanged(mockRides.filter { it.driver.id.toString() == "1" })
        }
    }

    @Test
    fun `updateSelectedDriverId should update selectedDriverId and apply filter`() = runTest {
        coEvery { repository.getRideHistory(any(), any()) } returns mockRides
        val filteredObserver = createFilteredRidesObserver()
        viewModel.filteredRides.observeForever(filteredObserver)

        viewModel.fetchRideHistory("customerId", "driverId")
        viewModel.updateSelectedDriverId("1")

        assert(viewModel.selectedDriverId.value == "1")
        verify {
            filteredObserver.onChanged(mockRides.filter { it.driver.id.toString() == "1" })
        }
    }
}