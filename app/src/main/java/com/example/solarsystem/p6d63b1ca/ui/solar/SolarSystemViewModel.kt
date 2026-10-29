package com.example.solarsystem.p6d63b1ca.ui.solar

import androidx.lifecycle.ViewModel
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase
import com.example.solarsystem.p6d63b1ca.domain.usecase.GetPlanetsUseCase
import com.example.solarsystem.p6d63b1ca.domain.usecase.SelectPlanetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SolarSystemViewModel(
    private val getPlanets: GetPlanetsUseCase,
    private val selectPlanet: SelectPlanetUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SolarSystemUiState())
    val state: StateFlow<SolarSystemUiState> = _state.asStateFlow()

    init {
        loadPlanets(PlanetDataCase.Normal)
    }

    fun onEvent(event: SolarSystemEvent) {
        when (event) {
            is SolarSystemEvent.SelectPlanet -> select(event.planetId)
            SolarSystemEvent.CloseFacts -> _state.update {
                it.copy(selectedPlanetId = null, selectedPlanet = null, isFocused = false)
            }
            is SolarSystemEvent.SetDataCase -> loadPlanets(event.dataCase)
            SolarSystemEvent.Retry -> loadPlanets(_state.value.dataCase)
        }
    }

    private fun loadPlanets(dataCase: PlanetDataCase) {
        _state.update { it.copy(isLoading = true, dataCase = dataCase, errorMessage = null) }
        getPlanets(dataCase).fold(
            onSuccess = { planets ->
                _state.update { current ->
                    val selectedId = current.selectedPlanetId
                    current.copy(
                        planets = planets,
                        isLoading = false,
                        errorMessage = null,
                        selectedPlanetId = selectedId,
                        selectedPlanet = selectedId?.let { id -> planets.firstOrNull { it.id == id } },
                        isFocused = selectedId != null,
                    )
                }
            },
            onFailure = { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "行星数据加载失败。",
                    )
                }
            },
        )
    }

    private fun select(planetId: String) {
        val selected = selectPlanet(_state.value.planets, planetId) ?: return
        _state.update {
            it.copy(
                selectedPlanetId = selected.id,
                selectedPlanet = selected,
                isFocused = true,
            )
        }
    }
}
