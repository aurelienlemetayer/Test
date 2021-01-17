package com.aurelien.test.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.models.Place
import com.aurelien.test.data.repositories.PlacesRepository
import com.aurelien.test.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlacesViewModelTests {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var placesRepository: PlacesRepository

    private lateinit var viewModel: PlacesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = PlacesViewModel(placesRepository, SavedStateHandle())
    }

    @Test
    fun `Check search places succeeded`() = testCoroutineRule.runBlockingTest {
        val search = "Gare"
        val places = listOf(
            Place("1", "Gare de Lyon"),
            Place("2", "Gare de l'est", true),
            Place("3", "Gare du Nord", true),
            Place("4", "Gare de Paris-Montparnasse")
        )

        val sortedPlaces = listOf(
            Place("2", "Gare de l'est", true),
            Place("1", "Gare de Lyon"),
            Place("4", "Gare de Paris-Montparnasse"),
            Place("3", "Gare du Nord", true)
        )

        `when`(placesRepository.getPlaces(search))
            .thenReturn(ApiCoroutinesClient.Result.Success(places))

        viewModel.searchPlaces(search)

        assertEquals(sortedPlaces, viewModel.placesLiveData.value)
        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
    }

    @Test
    fun `Check search places failed`() = testCoroutineRule.runBlockingTest {
        val search = "Gare"

        `when`(placesRepository.getPlaces(search))
            .thenReturn(ApiCoroutinesClient.Result.Error(NullPointerException("The result is null")))

        viewModel.searchPlaces(search)

        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
        assertEquals(true, viewModel.showSearchingPlacesErrorLiveData.value?.peekContent())
    }

    @Test
    fun `Check load favorite places`() = testCoroutineRule.runBlockingTest {
        val places = listOf(
            Place("2", "Gare de l'est", true),
            Place("3", "Gare du Nord", true)
        )

        `when`(placesRepository.getFavoritePlaces())
            .thenReturn(places)

        viewModel.loadFavoritePlaces()

        assertEquals(places, viewModel.placesLiveData.value)
        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
    }


    @Test
    fun `Check add place as favorite`() = testCoroutineRule.runBlockingTest {

        val places = listOf(
            Place("2", "Gare de l'est", false),
            Place("1", "Gare de Lyon", false),
            Place("4", "Gare de Paris-Montparnasse", true),
            Place("3", "Gare du Nord", false)
        )

        `when`(placesRepository.getFavoritePlaces())
            .thenReturn(places)

        viewModel.loadFavoritePlaces()

        val placeToAddAsFavorite = Place("1", "Gare de Lyon", true)

        `when`(placesRepository.insertFavoritePlace(placeToAddAsFavorite))
            .thenReturn(Unit)

        viewModel.addPlaceAsFavorite(placeToAddAsFavorite)

        assertEquals(
            true,
            viewModel.placesLiveData.value?.find { it.id == placeToAddAsFavorite.id }?.isFavorite
        )
        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
    }

    @Test
    fun `Check remove place as favorite`() = testCoroutineRule.runBlockingTest {

        val places = listOf(
            Place("2", "Gare de l'est", true),
            Place("1", "Gare de Lyon", true),
            Place("4", "Gare de Paris-Montparnasse", true),
            Place("3", "Gare du Nord", false)
        )

        `when`(placesRepository.getFavoritePlaces())
            .thenReturn(places)

        viewModel.loadFavoritePlaces()

        val placeIdToRemoveAsFavorite = "1"

        `when`(placesRepository.deleteFavoritePlace(placeIdToRemoveAsFavorite))
            .thenReturn(Unit)

        viewModel.removePlaceAsFavorite(placeIdToRemoveAsFavorite, "ga")

        assertEquals(
            false,
            viewModel.placesLiveData.value?.find { it.id == placeIdToRemoveAsFavorite }?.isFavorite
        )
        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
    }
}