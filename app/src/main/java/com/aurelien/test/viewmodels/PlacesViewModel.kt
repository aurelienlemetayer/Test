package com.aurelien.test.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.aurelien.test.R
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.models.Place
import com.aurelien.test.data.repositories.PlacesRepository
import com.aurelien.test.services.Event
import kotlinx.coroutines.launch

class PlacesViewModel @ViewModelInject constructor(
    private val placesRepository: PlacesRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var currentSearch: String? = null

    fun searchPlaces(query: String) {
        _loaderVisibilityLiveData.value = true
        _contentVisibilityLiveData.value = false

        viewModelScope.launch {
            when (val result = placesRepository.getPlaces(query)) {
                is ApiCoroutinesClient.Result.Success -> {
                    val sortedPlaces = result.data.sorted()
                    favoritePlaces.map { fp ->
                        sortedPlaces.find { it.id == fp.id }?.isFavorite = true
                    }
                    _placesLiveData.value = sortedPlaces
                }
                is ApiCoroutinesClient.Result.Error -> {
                    _showSearchingPlacesErrorLiveData.value = Event(true)
                }
            }
            _loaderVisibilityLiveData.value = false
            _contentVisibilityLiveData.value = true
        }
    }

    fun loadFavoritePlaces() {
        _loaderVisibilityLiveData.value = true
        _contentVisibilityLiveData.value = false

        viewModelScope.launch {
            val places = placesRepository.getFavoritePlaces()
            favoritePlaces = places.toMutableList()
            if (places.isNotEmpty()) {
                _placesLiveData.value = places.toMutableList()
            }
            _loaderVisibilityLiveData.value = false
            _contentVisibilityLiveData.value = true
        }
    }

    fun addPlaceAsFavorite(place: Place) {
        viewModelScope.launch {
            placesRepository.insertFavoritePlace(place)
            favoritePlaces.apply {
                add(place)
                sort()
            }
            _placesLiveData.value?.find { it.id == place.id }?.isFavorite = true
        }
    }

    fun removePlaceAsFavorite(placeId: String, currentSearch: String) {
        viewModelScope.launch {
            placesRepository.deleteFavoritePlace(placeId)
            favoritePlaces.apply {
                val index = favoritePlaces.indexOfFirst { it.id == placeId }
                removeAt(index)
                if (currentSearch.isEmpty()) {
                    _removePlaceFromTheList.value = Event(index)
                }
            }
            _placesLiveData.value?.find { it.id == placeId }?.isFavorite = false
        }
    }

    fun searchViewTextChanged(search: String?) {

        val wasNullOrEmpty = currentSearch.isNullOrEmpty()
        val searchIsNullOrEmpty = search.isNullOrEmpty()

        if (searchIsNullOrEmpty != wasNullOrEmpty) {
            if (searchIsNullOrEmpty) {
                _searchViewDrawableId.value = R.drawable.ic_search_hint
                _placesLiveData.value = favoritePlaces
            } else {
                _searchViewDrawableId.value = R.drawable.ic_search
            }
        }
        currentSearch = search
    }

    // region LiveData

    companion object {
        private const val TAG = "PlacesViewModel"
        private const val KEY_CONTENT_VISIBILITY = "${TAG}_KEY_CONTENT_VISIBILITY"
        private const val KEY_LOADER_VISIBILITY = "${TAG}_KEY_LOADER_VISIBILITY"
        private const val KEY_PLACES = "${TAG}_KEY_PLACES"
        private const val KEY_FAVORITE_PLACES = "${TAG}_KEY_FAVORITE_PLACES"
        private const val KEY_SEARCH_VIEW_DRAWABLE_ID = "${TAG}_KEY_SEARCH_VIEW_DRAWABLE_ID"
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

    private var favoritePlaces = state.get<MutableList<Place>>(KEY_FAVORITE_PLACES)
        ?: mutableListOf()

    val showSearchingPlacesErrorLiveData: LiveData<Event<Boolean>>
        get() = _showSearchingPlacesErrorLiveData

    private val _showSearchingPlacesErrorLiveData: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))

    val searchViewDrawableId: LiveData<Int>
        get() = _searchViewDrawableId

    private val _searchViewDrawableId =
        state.getLiveData(KEY_SEARCH_VIEW_DRAWABLE_ID, R.drawable.ic_search_hint)

    val removePlaceFromTheList: LiveData<Event<Int?>>
        get() = _removePlaceFromTheList

    private val _removePlaceFromTheList: MutableLiveData<Event<Int?>> =
        MutableLiveData(Event(null))

    fun saveState() {
        state.set(KEY_CONTENT_VISIBILITY, contentVisibilityLiveData.value)
        state.set(KEY_LOADER_VISIBILITY, loaderVisibilityLiveData.value)
        state.set(KEY_PLACES, placesLiveData.value)
        state.set(KEY_FAVORITE_PLACES, favoritePlaces)
        state.set(KEY_SEARCH_VIEW_DRAWABLE_ID, searchViewDrawableId.value)
    }

//endregion
}