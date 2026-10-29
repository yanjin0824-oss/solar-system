package com.example.solarsystem.p6d63b1ca.ui.theme

import androidx.compose.ui.graphics.Color
import com.pico.spatial.core.math.Color4
import com.pico.spatial.ui.foundation.content.toColor4

/**
 * App-owned palette from the accepted design. Native PicoTheme ColorScheme
 * roles are intentionally not redefined here.
 */
object SolarSystemColors {
    val spaceBase = Color(0xFF050714) // design-style: fixed-figma-color spaceBase
    val spaceNebula = Color(0xFF171C46) // design-style: fixed-figma-color spaceNebula
    val star = Color(0xFFFFFFFF) // design-style: fixed-figma-color star
    val sunCore = Color(0xFFFFF3C4) // design-style: fixed-figma-color sunCore
    val sunGlow = Color(0xFFFFB347) // design-style: fixed-figma-color sunGlow
    val orbitLine = Color(0xFF3A4166) // design-style: fixed-figma-color orbitLine
    val focusRing = Color(0xFFFFD27A) // design-style: fixed-figma-color focusRing
    val mercury = Color(0xFFB8B2A6) // design-style: fixed-figma-color mercury
    val venus = Color(0xFFE8C27A) // design-style: fixed-figma-color venus
    val earth = Color(0xFF4FA3D9) // design-style: fixed-figma-color earth
    val mars = Color(0xFFC65D45) // design-style: fixed-figma-color mars
    val jupiter = Color(0xFFD8A56A) // design-style: fixed-figma-color jupiter

    fun color(token: String): Color = when (token) {
        "spaceBase" -> spaceBase
        "spaceNebula" -> spaceNebula
        "star" -> star
        "sunCore" -> sunCore
        "sunGlow" -> sunGlow
        "orbitLine" -> orbitLine
        "focusRing" -> focusRing
        "mercury" -> mercury
        "venus" -> venus
        "earth" -> earth
        "mars" -> mars
        "jupiter" -> jupiter
        else -> star
    }

    fun color4(token: String): Color4 = color(token).toColor4()

    fun dimColor4(color: Color4): Color4 =
        Color4(color.red * 0.35f, color.green * 0.35f, color.blue * 0.35f, color.alpha)
}
