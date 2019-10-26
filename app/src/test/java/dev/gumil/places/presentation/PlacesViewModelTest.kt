package dev.gumil.places.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.gumil.places.data.PlacesType
import dev.gumil.places.domain.Place
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

internal class PlacesViewModelTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val fakeRepository = FakePlacesRepository()

    private val distanceCalculator = object : DistanceCalculator {
        override fun sortByDistance(
            latitude: Double,
            longitude: Double,
            places: List<Place>
        ): List<Place> {
            return places
        }

    }

    @Test
    fun `test refresh`() = runBlocking {
        // 1. Setup
        val viewModel = PlacesViewModel(fakeRepository, distanceCalculator, this)
        val mockFunction = mockk<() -> Unit>(relaxed = true)
        val mockObserver = mockk<Observer<PlacesViewModel.State>>(relaxed = true)
        viewModel.onError = mockFunction
        viewModel.state.observeForever(mockObserver)

        val places = listOf(
            Place(
                52.3760864,
                4.880024100000001,
                "Willemine Semeins Jazz vocalist en coach",
                false,
                0.0,
                "Egelantiersstraat 121-B, Amsterdam"
            ),
            Place(
                52.375233,
                4.880817,
                "Café De Nieuwe Lelie",
                true,
                4.6,
                "Nieuwe Leliestraat 83, Amsterdam"
            )
        )

        val expected = PlacesViewModel.State(places, PlacesViewModel.State.Mode.REFRESH)

        // 2. Action
        viewModel.refresh(0.0, 0.0, PlacesType.BAR)

        // 2. Verification
        coVerify { mockObserver.onChanged(expected) }
        confirmVerified(mockFunction)
        confirmVerified(mockObserver)
    }


    @Test
    fun `test load more when pagetoken is not null`() = runBlocking {
        // 1. Setup
        val viewModel = PlacesViewModel(fakeRepository, distanceCalculator, this)
        val mockFunction = mockk<() -> Unit>(relaxed = true)
        val mockObserver = mockk<Observer<PlacesViewModel.State>>(relaxed = true)
        viewModel.onError = mockFunction
        viewModel.state.observeForever(mockObserver)

        val places = listOf(
            Place(
                52.3760864,
                4.880024100000001,
                "Willemine Semeins Jazz vocalist en coach",
                false,
                0.0,
                "Egelantiersstraat 121-B, Amsterdam"
            )
        )

        val expected = PlacesViewModel.State(places, PlacesViewModel.State.Mode.LOAD_MORE)

        // 2. Action
        viewModel.loadMore(0.0, 0.0, PlacesType.CAFE)

        // 2. Verification
        coVerify { mockObserver.onChanged(expected) }
        confirmVerified(mockFunction)
        confirmVerified(mockObserver)
    }

    @Test
    fun `test load more when pagetoken is null`() = runBlocking {
        // 1. Setup
        val viewModel = PlacesViewModel(fakeRepository, distanceCalculator, this)
        val mockFunction = mockk<() -> Unit>(relaxed = true)
        val mockObserver = mockk<Observer<PlacesViewModel.State>>(relaxed = true)
        viewModel.onError = mockFunction

        // 2. Action
        val observer = object : Observer<PlacesViewModel.State> {
            override fun onChanged(t: PlacesViewModel.State?) {
                viewModel.loadMore(0.0, 0.0, PlacesType.CAFE)
                viewModel.state.observeForever(mockObserver)
                viewModel.state.removeObserver(this)
            }
        }
        viewModel.state.observeForever(observer)
        viewModel.loadMore(0.0, 0.0, PlacesType.CAFE)

        // 2. Verification
        coVerify {
            mockObserver.onChanged(
                PlacesViewModel.State(
                    emptyList(),
                    PlacesViewModel.State.Mode.LOAD_MORE
                )
            )
        }
        confirmVerified(mockFunction)
        confirmVerified(mockObserver)
    }

    @Test
    fun `test handle error`() = runBlocking {
        // 1. Setup
        val viewModel = PlacesViewModel(fakeRepository, distanceCalculator, this)
        val mockFunction = mockk<() -> Unit>(relaxed = true)
        val mockObserver = mockk<Observer<PlacesViewModel.State>>(relaxed = true)
        viewModel.onError = mockFunction
        viewModel.state.observeForever(mockObserver)

        // 2. Action
        viewModel.refresh(0.0, 0.0, PlacesType.RESTAURANT)

        // 2. Verification
        coVerify(exactly = 1) { mockFunction.invoke() }
        confirmVerified(mockFunction)
        confirmVerified(mockObserver)
    }
}