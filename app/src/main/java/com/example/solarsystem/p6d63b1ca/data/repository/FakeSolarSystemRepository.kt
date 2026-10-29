package com.example.solarsystem.p6d63b1ca.data.repository

import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetDataCase
import com.example.solarsystem.p6d63b1ca.domain.model.PlanetStats

/**
 * Local catalogue used by the demo. The three data cases exercise the same UI
 * graph with normal, limited, and unavailable facts.
 */
class FakeSolarSystemRepository : SolarSystemRepository {

    override fun loadPlanets(dataCase: PlanetDataCase): Result<List<Planet>> {
        val planets = when (dataCase) {
            PlanetDataCase.Normal -> normalPlanets
            PlanetDataCase.Fallback -> fallbackPlanets
            PlanetDataCase.Error -> errorPlanets
        }
        return Result.success(planets)
    }

    private val normalPlanets = listOf(
        Planet(
            id = "mercury",
            name = "水星",
            type = "类地行星",
            fact = "最小的行星，白天炙热、夜晚冰冷。",
            stats = PlanetStats(
                distanceFromSun = "0.39 天文单位",
                dayLength = "58.6 地球日",
                yearLength = "88 地球日",
                knownMoons = "0",
            ),
            colorToken = "mercury",
            orbitRadiusMeters = 0.38f,
            orbitDurationSeconds = 8f,
            radiusMeters = 0.035f,
            startAngleDegrees = 20f,
        ),
        Planet(
            id = "venus",
            name = "金星",
            type = "类地行星",
            fact = "温室效应失控的星球，云层厚重压抑。",
            stats = PlanetStats(
                distanceFromSun = "0.72 天文单位",
                dayLength = "243 地球日",
                yearLength = "225 地球日",
                knownMoons = "0",
            ),
            colorToken = "venus",
            orbitRadiusMeters = 0.55f,
            orbitDurationSeconds = 13f,
            radiusMeters = 0.052f,
            startAngleDegrees = 130f,
        ),
        Planet(
            id = "earth",
            name = "地球",
            type = "类地行星",
            fact = "目前已知唯一拥有液态水海洋与生命的星球。",
            stats = PlanetStats(
                distanceFromSun = "1.00 天文单位",
                dayLength = "24 小时",
                yearLength = "365.25 天",
                knownMoons = "1",
            ),
            colorToken = "earth",
            orbitRadiusMeters = 0.72f,
            orbitDurationSeconds = 19f,
            radiusMeters = 0.058f,
            startAngleDegrees = 240f,
        ),
        Planet(
            id = "mars",
            name = "火星",
            type = "类地行星",
            fact = "遍布氧化铁荒漠，拥有太阳系最高的火山。",
            stats = PlanetStats(
                distanceFromSun = "1.52 天文单位",
                dayLength = "24.6 小时",
                yearLength = "687 地球日",
                knownMoons = "2",
            ),
            colorToken = "mars",
            orbitRadiusMeters = 0.92f,
            orbitDurationSeconds = 27f,
            radiusMeters = 0.045f,
            startAngleDegrees = 310f,
        ),
        Planet(
            id = "jupiter",
            name = "木星",
            type = "气态巨行星",
            fact = "拥有比地球还大的风暴的气态巨行星。",
            stats = PlanetStats(
                distanceFromSun = "5.20 天文单位",
                dayLength = "9.9 小时",
                yearLength = "11.9 地球年",
                knownMoons = "95",
            ),
            colorToken = "jupiter",
            orbitRadiusMeters = 1.15f,
            orbitDurationSeconds = 38f,
            radiusMeters = 0.095f,
            startAngleDegrees = 80f,
        ),
    )

    private val fallbackPlanets = normalPlanets.map { planet ->
        planet.copy(
            fact = "该行星的详细资料暂不可用。",
            stats = planet.stats.copy(
                dayLength = "—",
                yearLength = "—",
                knownMoons = "—",
            ),
        )
    }

    private val errorPlanets = normalPlanets.map { planet ->
        planet.copy(
            type = "数据暂不可用",
            fact = "行星数据加载失败，请尝试选择其他行星。",
            stats = PlanetStats(
                distanceFromSun = "—",
                dayLength = "—",
                yearLength = "—",
                knownMoons = "—",
            ),
        )
    }
}
