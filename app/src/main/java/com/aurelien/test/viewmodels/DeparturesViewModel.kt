package com.aurelien.test.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.models.Departure
import com.aurelien.test.data.repositories.DeparturesRepository
import com.aurelien.test.core.livedata.Event
import kotlinx.coroutines.launch

class DeparturesViewModel @ViewModelInject constructor(
    private val departuresRepository: DeparturesRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    fun loadNextDepartures(placeId: String) {
        _loaderVisibilityLiveData.value = true
        _contentVisibilityLiveData.value = false

        viewModelScope.launch {
            when (val result = departuresRepository.getDepartures(placeId)) {
                is ApiCoroutinesClient.Result.Success -> {
                    _departuresLiveData.value = result.data
                }
                is ApiCoroutinesClient.Result.Error -> {
                    _showGettingNextDeparturesErrorLiveData.value = Event(true)
                }
            }
            _loaderVisibilityLiveData.value = false
            _contentVisibilityLiveData.value = true
        }
    }

    // region LiveData

    companion object {
        private const val TAG = "DeparturesViewModel"
        private const val KEY_CONTENT_VISIBILITY = "${TAG}_KEY_CONTENT_VISIBILITY"
        private const val KEY_LOADER_VISIBILITY = "${TAG}_KEY_LOADER_VISIBILITY"
        private const val KEY_DEPARTURES = "${TAG}_KEY_DEPARTURES"
    }

    private val _contentVisibilityLiveData = state.getLiveData(KEY_CONTENT_VISIBILITY, false)

    val contentVisibilityLiveData: LiveData<Boolean>
        get() = _contentVisibilityLiveData

    private val _loaderVisibilityLiveData = state.getLiveData(KEY_LOADER_VISIBILITY, false)

    val loaderVisibilityLiveData: LiveData<Boolean>
        get() = _loaderVisibilityLiveData

    val departuresLiveData: LiveData<List<Departure>?>
        get() = _departuresLiveData

    private val _departuresLiveData = state.getLiveData<List<Departure>?>(KEY_DEPARTURES)

    val showGettingNextDeparturesErrorLiveData: LiveData<Event<Boolean>>
        get() = _showGettingNextDeparturesErrorLiveData

    private val _showGettingNextDeparturesErrorLiveData: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))

    fun saveState() {
        state.set(KEY_CONTENT_VISIBILITY, contentVisibilityLiveData.value)
        state.set(KEY_LOADER_VISIBILITY, loaderVisibilityLiveData.value)
        state.set(KEY_DEPARTURES, _departuresLiveData.value)
    }

    //endregion
}