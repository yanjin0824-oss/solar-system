package com.example.solarsystem.p6d63b1ca.domain.usecase

import com.example.solarsystem.p6d63b1ca.data.repository.FakeSolarSystemRepository
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPlanetsUseCaseTest {

    private val useCase = GetPlanetsUseCase(FakeSolarSystemRepository())

    @Test
    fun normalCaseReturnsFivePlanets() {
        val result = useCase(PlanetDataCase.Normal)
        val planets = result.getOrThrow()

        assertEquals(5, planets.size)
        assertEquals("Earth", planets.first { it.id == "earth" }.name)
    }

    @Test
    fun fallbackCaseReturnsLimitedFacts() {
        val planets = useCase(PlanetDataCase.Fallback).getOrThrow()

        assertEquals(5, planets.size)
        assertEquals(
            "Detailed facts are unavailable for this planet right now.",
            planets.first().fact,
        )
        assertEquals("—", planets.first().stats.dayLength)
    }

    @Test
    fun errorCaseReturnsUnavailableFacts() {
        val planets = useCase(PlanetDataCase.Error).getOrThrow()

        assertEquals(5, planets.size)
        assertEquals("Data unavailable", planets.first().type)
        assertTrue(planets.first().fact.contains("could not be loaded"))
    }
}
