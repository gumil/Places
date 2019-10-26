package dev.gumil.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.gumil.places.data.ApiFactory
import dev.gumil.places.data.PlacesApi
import dev.gumil.places.domain.PlacesRepository
import dev.gumil.places.domain.PlacesRepositoryImpl
import dev.gumil.places.presentation.DistanceCalculator
import dev.gumil.places.presentation.DistanceCalculatorImpl
import dev.gumil.places.presentation.PlacesViewModel

internal interface AppComponent {

    val repository: PlacesRepository

    val placesApi: PlacesApi
}

internal class AppComponentImpl : AppComponent {

    override val placesApi: PlacesApi by lazy {
        ApiFactory.create()
    }

    override val repository: PlacesRepository by lazy {
        PlacesRepositoryImpl(placesApi)
    }
}

internal interface ActivityComponent {

    val distanceCalculator: DistanceCalculator

    val placesViewModel: PlacesViewModel

    val viewModelFactory: ViewModelProvider.Factory
}

internal class ActivityComponentImpl(
    appComponent: AppComponent
) : ActivityComponent, AppComponent by appComponent {

    override val distanceCalculator: DistanceCalculator by lazy {
        DistanceCalculatorImpl()
    }

    override val placesViewModel: PlacesViewModel by lazy {
        PlacesViewModel(repository, distanceCalculator)
    }

    override val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(placesViewModel)
    }

}

internal class ViewModelFactory(
    private val placesViewModel: PlacesViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            PlacesViewModel::class.java -> placesViewModel as T
            else -> throw UnsupportedOperationException("No ViewModel declared with class")
        }
    }
}
