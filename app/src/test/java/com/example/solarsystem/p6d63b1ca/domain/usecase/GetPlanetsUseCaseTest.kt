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
        assertEquals("地球", planets.first { it.id == "earth" }.name)
    }

    @Test
    fun fallbackCaseReturnsLimitedFacts() {
        val planets = useCase(PlanetDataCase.Fallback).getOrThrow()

        assertEquals(5, planets.size)
        assertEquals(
            "该行星的详细资料暂不可用。",
            planets.first().fact,
        )
        assertEquals("—", planets.first().stats.dayLength)
    }

    @Test
    fun errorCaseReturnsUnavailableFacts() {
        val planets = useCase(PlanetDataCase.Error).getOrThrow()

        assertEquals(5, planets.size)
        assertEquals("数据暂不可用", planets.first().type)
        assertTrue(planets.first().fact.contains("加载失败"))
    }
}
