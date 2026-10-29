package com.example.solarsystem.p6d63b1ca.ui.solar.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.pico.spatial.ui.foundation.content.SpatialView
import com.pico.spatial.ui.foundation.dsl.registerSystem
import com.pico.spatial.ui.foundation.dsl.unregisterSystem
import com.pico.spatial.ui.foundation.gesture.detectSpatialPointerEvent

@Composable
fun OrreryStage(
    planets: List<Planet>,
    selectedPlanet: Planet?,
    isFocused: Boolean,
    onSelectPlanet: (String) -> Unit,
    onCloseFacts: () -> Unit,
) {
    val context = LocalContext.current
    val compact = LocalConfiguration.current.screenWidthDp <= 820
    val sceneState = remember { SolarSystemSceneState() }
    val currentOnSelectPlanet by rememberUpdatedState(onSelectPlanet)

    DisposableEffect(Unit) {
        registerSystem<PlanetOrbitSystem>()
        registerSystem<OrreryFocusSystem>()
        onDispose {
            unregisterSystem<PlanetOrbitSystem>()
            unregisterSystem<OrreryFocusSystem>()
            sceneState.destroy()
        }
    }

    LaunchedEffect(isFocused, selectedPlanet?.id) {
        sceneState.applyFocus(if (isFocused) selectedPlanet?.id else null)
    }

    SpatialView(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectSpatialPointerEvent(context = context) { events ->
                    var consumed = false
                    events.forEach { info ->
                        if (info.pressed) {
                            val planetId = sceneState.planetIdByEntity[info.targetedEntity]
                            if (planetId != null) {
                                currentOnSelectPlanet(planetId)
                                consumed = true
                            }
                        }
                    }
                    consumed
                }
            },
        initial = { content, attachments ->
            sceneState.build(content, attachments, planets)
        },
        attachments = {
            AttachmentPanel(PANEL_FACTS) {
                if (isFocused && selectedPlanet != null) {
                    PlanetFactsCard(
                        planet = selectedPlanet,
                        compact = compact,
                        onClose = onCloseFacts,
                    )
                }
            }
            AttachmentPanel(PANEL_HINT) {
                if (!isFocused) {
                    ExploreHint(compact = compact)
                }
            }
        },
    )
}
