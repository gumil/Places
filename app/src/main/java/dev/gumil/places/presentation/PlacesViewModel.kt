package dev.gumil.places.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.gumil.places.data.PlacesType
import dev.gumil.places.domain.Place
import dev.gumil.places.domain.PlacesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class PlacesViewModel(
    private val placesRepository: PlacesRepository,
    scope: CoroutineScope? = null
) : ViewModel() {

    val state: LiveData<State> get() = stateLiveData

    var onError: () -> Unit = {}

    private val stateLiveData = MutableLiveData<State>()

    private val job = scope?.coroutineContext?.get(Job) ?: Job()

    private val uiScope = scope ?: CoroutineScope(Dispatchers.Main + job)

    private var nextPageToken: String? = null

    fun refresh(latitude: Double, longitude: Double, type: PlacesType) {
        loadNearby(latitude, longitude, type)
    }

    fun loadMore(latitude: Double, longitude: Double, type: PlacesType) {
        loadNearby(latitude, longitude, type, State.Mode.LOAD_MORE, nextPageToken)
    }

    private fun loadNearby(
        latitude: Double,
        longitude: Double,
        type: PlacesType,
        mode: State.Mode = State.Mode.REFRESH,
        token: String? = null
    ) {
        uiScope.launch {
            try {
                val places = placesRepository.getNearby(latitude, longitude, type, token)
                nextPageToken = places.first
                stateLiveData.postValue(State(places.second, mode))
            } catch (e: Throwable) {
                onError()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    data class State(
        val list: List<Place>,
        val loadingMode: Mode = Mode.REFRESH
    ) {
        enum class Mode {
            REFRESH, LOAD_MORE
        }
    }
}