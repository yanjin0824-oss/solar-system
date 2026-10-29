package com.example.solarsystem.p6d63b1ca.ui.solar.components

import com.pico.spatial.core.ecs.Component
import com.pico.spatial.core.ecs.EntityQueryCondition
import com.pico.spatial.core.ecs.SceneUpdateContext
import com.pico.spatial.core.ecs.System
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PlanetOrbitComponent(
    val orbitRadius: Float,
    val angularSpeed: Float,
    var angle: Float,
    var paused: Boolean = false,
) : Component()

class OrreryRootComponent(
    val basePosition: Vector3,
    val baseScale: Float,
) : Component() {
    var targetPosition: Vector3 = basePosition
    var targetScale: Float = baseScale
    var focusedPlanetId: String? = null
}

class PlanetOrbitSystem : System() {
    override fun update(context: SceneUpdateContext) {
        val delta = context.deltaTime
        context.scene
            .queryEntity(EntityQueryCondition.hasComponent(PlanetOrbitComponent::class.java))
            .forEach { entity ->
                val orbit = entity.components[PlanetOrbitComponent::class.java] ?: return@forEach
                if (!orbit.paused) {
                    orbit.angle = (orbit.angle + orbit.angularSpeed * delta) % TWO_PI
                }
                val transform = entity.components[TransformComponent::class.java] ?: return@forEach
                transform.setPosition(
                    Vector3(
                        orbit.orbitRadius * cos(orbit.angle),
                        0f,
                        orbit.orbitRadius * sin(orbit.angle),
                    )
                )
            }
    }

    private companion object {
        const val TWO_PI = (PI * 2.0).toFloat()
    }
}

class OrreryFocusSystem : System() {
    override fun update(context: SceneUpdateContext) {
        val blend = (context.deltaTime * FOCUS_LERP_SPEED).coerceIn(0f, 1f)
        context.scene
            .queryEntity(EntityQueryCondition.hasComponent(OrreryRootComponent::class.java))
            .forEach { entity ->
                val root = entity.components[OrreryRootComponent::class.java] ?: return@forEach
                val transform = entity.components[TransformComponent::class.java] ?: return@forEach
                val currentPosition = transform.position
                val targetPosition = root.targetPosition
                transform.setPosition(
                    Vector3(
                        currentPosition.x + (targetPosition.x - currentPosition.x) * blend,
                        currentPosition.y + (targetPosition.y - currentPosition.y) * blend,
                        currentPosition.z + (targetPosition.z - currentPosition.z) * blend,
                    )
                )
                val currentScale = transform.scaleVector.x
                val nextScale = currentScale + (root.targetScale - currentScale) * blend
                transform.setScaleVector(Vector3(nextScale))
            }
    }

    private companion object {
        const val FOCUS_LERP_SPEED = 5f
    }
}
