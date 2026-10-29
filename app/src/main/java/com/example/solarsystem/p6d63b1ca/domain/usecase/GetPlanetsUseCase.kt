package com.example.solarsystem.p6d63b1ca.domain.usecase

import com.example.solarsystem.p6d63b1ca.data.repository.SolarSystemRepository
import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase

class GetPlanetsUseCase(
    private val repository: SolarSystemRepository,
) {
    operator fun invoke(dataCase: PlanetDataCase = PlanetDataCase.Normal): Result<List<Planet>> =
        repository.loadPlanets(dataCase)
}
