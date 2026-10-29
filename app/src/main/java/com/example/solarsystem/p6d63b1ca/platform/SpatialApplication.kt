package com.example.solarsystem.p6d63b1ca.platform

import android.app.Application
import com.pico.spatial.ui.foundation.dsl.launch
import com.example.solarsystem.p6d63b1ca.mainApp

class SpatialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        launch(::mainApp)
    }
}
