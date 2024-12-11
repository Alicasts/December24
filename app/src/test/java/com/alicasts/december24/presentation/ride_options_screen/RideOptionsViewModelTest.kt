package com.alicasts.december24.presentation.ride_options_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.data.models.confirm_ride.ConfirmRideResponse
import com.alicasts.december24.data.models.shared.DriverOption
import com.alicasts.december24.data.models.shared.Location
import com.alicasts.december24.data.models.shared.Review
import com.alicasts.december24.data.models.shared.RideRequest
import com.alicasts.december24.data.models.ride_options.RideResponse
import com.alicasts.december24.data.repository.ride_options.interfaces.RideOptionsRepository
import com.alicasts.december24.presentation.mocks.FakeStringResourceProvider
import com.alicasts.december24.utils.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
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
class RideOptionsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideOptionsViewModel
    private val repository = mockk<RideOptionsRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val stringResourceProvider = FakeStringResourceProvider()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = spyk(RideOptionsViewModel(repository, stringResourceProvider))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRideOptions should post Loading and then Success`() = runTest {
        val mockResponse = RideResponse(
            origin = mockk(),
            destination = mockk(),
            distance = 1000,
            duration = "10 min",
            options = emptyList(),
            routeResponse = null
        )
        val mockJson = """{"customer_id":"customer1","origin":"origin1","destination":"destination1"}"""

        coEvery { repository.getRideOptions(mockJson) } returns mockResponse
        every { viewModel["parseRideRequest"](mockJson) } returns Triple("customer1", "origin1", "destination1")

        val observer = mockk<Observer<Resource<RideResponse>>>(relaxed = true)
        viewModel.state.observeForever(observer)

        viewModel.fetchRideOptions(mockJson)

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Success && it.data == mockResponse })
        }

        viewModel.state.removeObserver(observer)
    }

    @Test
    fun `fetchRideOptions should post Loading and then Error`() = runTest {
        val errorMessage = "An error occurred"
        val mockJson = """{"customer_id":"customer1","origin":"origin1","destination":"destination1"}"""

        coEvery { repository.getRideOptions(mockJson) } throws Exception(errorMessage)

        every { viewModel["parseRideRequest"](mockJson) } returns Triple("customer1", "origin1", "destination1")

        val observer = mockk<Observer<Resource<RideResponse>>>(relaxed = true)
        viewModel.state.observeForever(observer)

        viewModel.fetchRideOptions(mockJson)

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Error && it.message == errorMessage })
        }

        viewModel.state.removeObserver(observer)
    }

    @Test
    fun `resetSubmitState should set submitState to null`() {
        viewModel.resetSubmitState()

        val observer = mockk<Observer<Resource<ConfirmRideResponse>?>>(relaxed = true)
        viewModel.submitState.observeForever(observer)

        verify { observer.onChanged(null) }

        viewModel.submitState.removeObserver(observer)
    }


    @Test
    fun `returnTravelHistoryRoute should return route when data is valid`() {
        val rideRequest = RideRequest("customer1", "origin1", "destination1")
        val mockResponse = RideResponse(
            origin = mockk(),
            destination = mockk(),
            distance = 1000,
            duration = "10 min",
            options = listOf(
                DriverOption(
                1,
                "Dean Winchester",
                "hmmm",
                vehicle = "Impala 68",
                review = Review(rating = 4, comment = "ee"),
                value = 88.88
            )
            ),
            routeResponse = null
        )
        viewModel._rideRequest.postValue(rideRequest)
        viewModel._state.postValue(Resource.Success(mockResponse))

        val route = viewModel.returnRideHistoryRoute("nullString")

        assertNotNull(route)
        assertTrue(route!!.contains("customer1"))
        assertTrue(route.contains("1"))
    }

    @Test
    fun `returnTravelHistoryRoute should return null when data is invalid`() {
        val route = viewModel.returnRideHistoryRoute("nullString")
        assertNull(route)
    }

    @Test
    fun `buildStaticMapUrl should return correct URL`() {
        val origin = Location(1.0, 1.0)
        val destination = Location(2.0, 2.0)

        val url = viewModel.buildStaticMapUrl(origin, destination)

        assertTrue(url.startsWith("https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/"))
        assertTrue(url.contains("pin-s-a+9ed4bd(1.0,1.0)"))
        assertTrue(url.contains("pin-s-b+000(2.0,2.0)"))
    }
}