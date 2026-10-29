package com.example.solarsystem.p6d63b1ca.domain.model

/**
 * The data case used by the local planet catalogue. These are testable content
 * variants, not user-facing debug controls.
 */
enum class PlanetDataCase {
    Normal,
    Fallback,
    Error,
}

data class PlanetStats(
    val distanceFromSun: String,
    val dayLength: String,
    val yearLength: String,
    val knownMoons: String,
)

/**
 * A stylized planet: presentation facts plus the physical values used by the
 * Stage orrery. Values are deliberately simple and are not Compose-aware.
 */
data class Planet(
    val id: String,
    val name: String,
    val type: String,
    val fact: String,
    val stats: PlanetStats,
    val colorToken: String,
    val orbitRadiusMeters: Float,
    val orbitDurationSeconds: Float,
    val radiusMeters: Float,
    val startAngleDegrees: Float,
)
