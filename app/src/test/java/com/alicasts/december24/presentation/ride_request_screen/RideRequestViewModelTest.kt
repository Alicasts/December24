package com.alicasts.december24.presentation.ride_request_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alicasts.december24.presentation.mocks.FakeStringResourceProvider
import com.alicasts.december24.utils.Constants
import com.alicasts.december24.utils.Constants.VALID_USER_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class RideRequestViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideRequestViewModel

    @Before
    fun setUp() {
        val fakeStringResourceProvider = FakeStringResourceProvider()
        viewModel = RideRequestViewModel(fakeStringResourceProvider)
    }

    @Test
    fun `userIds should be initialized correctly`() {
        val expectedUserIds = listOf("Null", "Qualquer", VALID_USER_ID)

        val observer = mock(Observer::class.java) as Observer<List<String>>
        viewModel.userIds.observeForever(observer)

        assertEquals(expectedUserIds, viewModel.userIds.value)
        viewModel.userIds.removeObserver(observer)
    }

    @Test
    fun `addresses should be initialized correctly`() {
        val expectedAddresses = listOf("Null", "Qualquer") + Constants.VALID_ADDRESSES

        val observer = mock(Observer::class.java) as Observer<List<String>>
        viewModel.addresses.observeForever(observer)

        assertEquals(expectedAddresses, viewModel.addresses.value)
        viewModel.addresses.removeObserver(observer)
    }

}