package dev.gumil.places

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dev.gumil.places.data.ApiFactory
import dev.gumil.places.data.PlacesApi
import dev.gumil.places.db.PlaceDatabase
import dev.gumil.places.domain.PlacesRepository
import dev.gumil.places.domain.PlacesRepositoryImpl
import dev.gumil.places.presentation.DistanceCalculator
import dev.gumil.places.presentation.DistanceCalculatorImpl
import dev.gumil.places.presentation.PlacesViewModel

internal interface AppComponent {

    val appContext: Context

    val placesApi: PlacesApi

    val placeDatabase: PlaceDatabase

    val repository: PlacesRepository
}

internal class AppComponentImpl(
    override val appContext: Context
) : AppComponent {

    override val placesApi: PlacesApi by lazy {
        ApiFactory.create()
    }

    override val placeDatabase: PlaceDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            PlaceDatabase::class.java,
            "placedatabase"
        ).build()
    }

    override val repository: PlacesRepository by lazy {
        PlacesRepositoryImpl(placesApi, placeDatabase.placeDao())
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
