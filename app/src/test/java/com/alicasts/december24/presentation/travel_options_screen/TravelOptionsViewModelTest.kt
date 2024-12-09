package com.alicasts.december24.presentation.travel_options_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.data.repository.TravelOptionsRepository
import com.alicasts.december24.utils.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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
class TravelOptionsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TravelOptionsViewModel
    private val repository = mockk<TravelOptionsRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = spyk(TravelOptionsViewModel(repository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchTravelOptions should post Loading and then Success`() = runTest {
        val mockResponse = TravelResponse(
            origin = mockk(),
            destination = mockk(),
            distance = 1000,
            duration = "10 min",
            options = emptyList(),
            routeResponse = null
        )
        val mockJson = """{"customer_id":"customer1","origin":"origin1","destination":"destination1"}"""

        coEvery { repository.getTravelOptions("customer1", "origin1", "destination1") } returns mockResponse
        every { viewModel["parseJson"](mockJson) } returns Triple("customer1", "origin1", "destination1")

        val observer = mockk<Observer<Resource<TravelResponse>>>(relaxed = true)
        viewModel.state.observeForever(observer)

        viewModel.fetchTravelOptions(mockJson)

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Success && it.data == mockResponse })
        }

        viewModel.state.removeObserver(observer)
    }

    @Test
    fun `fetchTravelOptions should post Loading and then Error`() = runTest {
        val errorMessage = "An error occurred"
        val mockJson = """{"customer_id":"customer1","origin":"origin1","destination":"destination1"}"""

        coEvery { repository.getTravelOptions("customer1", "origin1", "destination1") } throws Exception(errorMessage)
        every { viewModel["parseJson"](mockJson) } returns Triple("customer1", "origin1", "destination1")

        val observer = mockk<Observer<Resource<TravelResponse>>>(relaxed = true)
        viewModel.state.observeForever(observer)

        viewModel.fetchTravelOptions(mockJson)

        verifySequence {
            observer.onChanged(match { it is Resource.Loading })
            observer.onChanged(match { it is Resource.Error && it.message == errorMessage })
        }

        viewModel.state.removeObserver(observer)
    }
}