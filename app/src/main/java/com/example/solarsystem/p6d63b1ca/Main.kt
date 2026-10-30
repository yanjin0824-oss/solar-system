package com.example.solarsystem.p6d63b1ca

import com.example.solarsystem.p6d63b1ca.ui.solar.SolarSystemScreen
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.foundation.dsl.DefaultWindowContainer
import com.pico.spatial.ui.foundation.dsl.SpatialAppScope

fun mainApp(scope: SpatialAppScope) =
    with(scope) {
        DefaultWindowContainer {
            PicoTheme {
                SolarSystemScreen()
            }
        }
    }

