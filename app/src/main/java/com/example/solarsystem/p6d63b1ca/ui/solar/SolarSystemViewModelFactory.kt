package com.example.solarsystem.p6d63b1ca.ui.solar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.solarsystem.p6d63b1ca.data.repository.FakeSolarSystemRepository
import com.example.solarsystem.p6d63b1ca.domain.usecase.GetPlanetsUseCase
import com.example.solarsystem.p6d63b1ca.domain.usecase.SelectPlanetUseCase

class SolarSystemViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = FakeSolarSystemRepository()
        return SolarSystemViewModel(
            getPlanets = GetPlanetsUseCase(repository),
            selectPlanet = SelectPlanetUseCase(),
        ) as T
    }
}
