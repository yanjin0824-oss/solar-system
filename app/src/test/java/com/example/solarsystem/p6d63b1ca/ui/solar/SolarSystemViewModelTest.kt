package com.example.solarsystem.p6d63b1ca.ui.solar

import com.example.solarsystem.p6d63b1ca.data.repository.FakeSolarSystemRepository
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase
import com.example.solarsystem.p6d63b1ca.domain.usecase.GetPlanetsUseCase
import com.example.solarsystem.p6d63b1ca.domain.usecase.SelectPlanetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SolarSystemViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = SolarSystemViewModel(
        getPlanets = GetPlanetsUseCase(FakeSolarSystemRepository()),
        selectPlanet = SelectPlanetUseCase(),
    )

    @Test
    fun initialStateLoadsFivePlanets() = runTest {
        val viewModel = createViewModel()

        assertEquals(5, viewModel.state.value.planets.size)
        assertNull(viewModel.state.value.selectedPlanet)
        assertFalse(viewModel.state.value.isFocused)
        assertEquals(PlanetDataCase.Normal, viewModel.state.value.dataCase)
    }

    @Test
    fun selectPlanetFocusesAndPublishesFacts() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(SolarSystemEvent.SelectPlanet("mars"))

        assertEquals("mars", viewModel.state.value.selectedPlanetId)
        assertEquals("Mars", viewModel.state.value.selectedPlanet?.name)
        assertTrue(viewModel.state.value.isFocused)
    }

    @Test
    fun unknownPlanetDoesNotChangeFocus() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(SolarSystemEvent.SelectPlanet("unknown"))

        assertNull(viewModel.state.value.selectedPlanet)
        assertFalse(viewModel.state.value.isFocused)
    }

    @Test
    fun closeFactsReturnsToOverview() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(SolarSystemEvent.SelectPlanet("earth"))

        viewModel.onEvent(SolarSystemEvent.CloseFacts)

        assertNull(viewModel.state.value.selectedPlanetId)
        assertNull(viewModel.state.value.selectedPlanet)
        assertFalse(viewModel.state.value.isFocused)
    }

    @Test
    fun errorDataCasePublishesUnavailableFacts() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(SolarSystemEvent.SetDataCase(PlanetDataCase.Error))

        assertEquals(PlanetDataCase.Error, viewModel.state.value.dataCase)
        assertEquals("Data unavailable", viewModel.state.value.planets.first().type)
        assertTrue(viewModel.state.value.planets.first().fact.contains("could not be loaded"))
    }
}
