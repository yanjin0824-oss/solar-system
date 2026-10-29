package com.example.solarsystem.p6d63b1ca.data.repository

import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase

interface SolarSystemRepository {
    fun loadPlanets(dataCase: PlanetDataCase = PlanetDataCase.Normal): Result<List<Planet>>
}
