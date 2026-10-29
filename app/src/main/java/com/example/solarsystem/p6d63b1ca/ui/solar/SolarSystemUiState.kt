package com.example.solarsystem.p6d63b1ca.ui.solar

import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase

data class SolarSystemUiState(
    val planets: List<Planet> = emptyList(),
    val selectedPlanetId: String? = null,
    val selectedPlanet: Planet? = null,
    val isFocused: Boolean = false,
    val dataCase: PlanetDataCase = PlanetDataCase.Normal,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface SolarSystemEvent {
    data class SelectPlanet(val planetId: String) : SolarSystemEvent
    data object CloseFacts : SolarSystemEvent
    data class SetDataCase(val dataCase: PlanetDataCase) : SolarSystemEvent
    data object Retry : SolarSystemEvent
}
