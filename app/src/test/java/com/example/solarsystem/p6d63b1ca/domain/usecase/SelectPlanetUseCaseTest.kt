package com.example.solarsystem.p6d63b1ca.domain.usecase

import com.example.solarsystem.p6d63b1ca.data.repository.FakeSolarSystemRepository
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SelectPlanetUseCaseTest {

    private val planets = FakeSolarSystemRepository()
        .loadPlanets(PlanetDataCase.Normal)
        .getOrThrow()
    private val useCase = SelectPlanetUseCase()

    @Test
    fun validIdReturnsPlanet() {
        val planet = useCase(planets, "jupiter")

        assertEquals("Jupiter", planet?.name)
        assertEquals("jupiter", planet?.colorToken)
    }

    @Test
    fun unknownIdReturnsNull() {
        assertNull(useCase(planets, "unknown"))
    }
}
