package com.aurelien.test.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.services.Event
import com.aurelien.test.services.PlacesRepository
import com.aurelien.test.services.models.Place
import kotlinx.coroutines.launch

class PlacesViewModel @ViewModelInject constructor(
    private val placesRepository: PlacesRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    fun searchPlaces(query: String) {
        _loaderVisibilityLiveData.value = true
        _contentVisibilityLiveData.value = false

        viewModelScope.launch {
            when (val result = placesRepository.getPlaces(query)) {
                is ApiCoroutinesClient.Result.Success -> {
                    _placesLiveData.value = result.data.sorted()
                }
                is ApiCoroutinesClient.Result.Error -> {
                    _showSearchingPlacesErrorLiveData.value = Event(true)
                }
            }
            _loaderVisibilityLiveData.value = false
            _contentVisibilityLiveData.value = true
        }
    }

    // region LiveData

    companion object {
        private const val TAG = "PlacesViewModel"
        private const val KEY_CONTENT_VISIBILITY = "${TAG}_KEY_CONTENT_VISIBILITY"
        private const val KEY_LOADER_VISIBILITY = "${TAG}_KEY_LOADER_VISIBILITY"
        private const val KEY_PLACES = "${TAG}_KEY_PLACES"
    }

    private val _contentVisibilityLiveData = state.getLiveData(KEY_CONTENT_VISIBILITY, false)

    val contentVisibilityLiveData: LiveData<Boolean>
        get() = _contentVisibilityLiveData

    private val _loaderVisibilityLiveData = state.getLiveData(KEY_LOADER_VISIBILITY, false)

    val loaderVisibilityLiveData: LiveData<Boolean>
        get() = _loaderVisibilityLiveData

    val placesLiveData: LiveData<List<Place>?>
        get() = _placesLiveData

    private val _placesLiveData = state.getLiveData<List<Place>?>(KEY_PLACES)

    val showSearchingPlacesErrorLiveData: LiveData<Event<Boolean>>
        get() = _showSearchingPlacesErrorLiveData

    private val _showSearchingPlacesErrorLiveData: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))

    fun saveState() {
        state.set(KEY_CONTENT_VISIBILITY, contentVisibilityLiveData.value)
        state.set(KEY_LOADER_VISIBILITY, loaderVisibilityLiveData.value)
        state.set(KEY_PLACES, placesLiveData.value)
    }

//endregion
}