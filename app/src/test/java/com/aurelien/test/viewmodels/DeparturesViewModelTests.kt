package com.aurelien.test.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.models.CommercialMode
import com.aurelien.test.data.models.Departure
import com.aurelien.test.data.repositories.DeparturesRepository
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
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeparturesViewModelTests {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var departuresRepository: DeparturesRepository

    private lateinit var viewModel: DeparturesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = DeparturesViewModel(departuresRepository, SavedStateHandle())
    }

    @Test
    fun `Check next departures succeeded`() = testCoroutineRule.runBlockingTest {
        val placeId = "1"
        val departures = listOf(
            Departure("1", "FFFFFF", Date(), CommercialMode.BUS, "Gare de Lyon"),
            Departure("2", "F56F90", Date(), CommercialMode.BUS, "Gare du Nord"),
            Departure("3", "459832", Date(), CommercialMode.BUS, "Gare de l'est"),
        )


        `when`(departuresRepository.getDepartures(placeId))
            .thenReturn(ApiCoroutinesClient.Result.Success(departures))

        viewModel.loadNextDepartures(placeId)

        assertEquals(departures, viewModel.departuresLiveData.value)
        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
    }

    @Test
    fun `Check next departures failed`() = testCoroutineRule.runBlockingTest {
        val placeId = "1"

        `when`(departuresRepository.getDepartures(placeId))
            .thenReturn(ApiCoroutinesClient.Result.Error(NullPointerException("The result is null")))

        viewModel.loadNextDepartures(placeId)

        assertEquals(false, viewModel.loaderVisibilityLiveData.value)
        assertEquals(true, viewModel.contentVisibilityLiveData.value)
        assertEquals(true, viewModel.showGettingNextDeparturesErrorLiveData.value?.peekContent())
    }
}