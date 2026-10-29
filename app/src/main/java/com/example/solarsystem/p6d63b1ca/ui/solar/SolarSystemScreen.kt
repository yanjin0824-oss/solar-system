package com.example.solarsystem.p6d63b1ca.ui.solar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solarsystem.p6d63b1ca.ui.solar.components.OrreryStage

@Composable
fun SolarSystemScreen() {
    val viewModel: SolarSystemViewModel = viewModel(factory = SolarSystemViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()
    SolarSystemContent(state = state, onEvent = viewModel::onEvent)
}

@Composable
internal fun SolarSystemContent(
    state: SolarSystemUiState,
    onEvent: (SolarSystemEvent) -> Unit,
) {
    OrreryStage(
        planets = state.planets,
        selectedPlanet = state.selectedPlanet,
        isFocused = state.isFocused,
        onSelectPlanet = { onEvent(SolarSystemEvent.SelectPlanet(it)) },
        onCloseFacts = { onEvent(SolarSystemEvent.CloseFacts) },
    )
}
