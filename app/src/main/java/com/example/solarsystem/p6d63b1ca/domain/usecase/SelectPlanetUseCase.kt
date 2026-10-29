package com.example.solarsystem.p6d63b1ca.domain.usecase

import com.example.solarsystem.p6d63b1ca.domain.model.Planet

class SelectPlanetUseCase {
    operator fun invoke(planets: List<Planet>, planetId: String): Planet? =
        planets.firstOrNull { it.id == planetId }
}
