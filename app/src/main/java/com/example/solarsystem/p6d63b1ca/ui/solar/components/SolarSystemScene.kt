package com.example.solarsystem.p6d63b1ca.ui.solar.components

import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.ui.theme.SolarSystemColors
import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.CollisionComponent
import com.pico.spatial.core.ecs.DirectionalLightComponent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.HoverEffectComponent
import com.pico.spatial.core.ecs.InteractableComponent
import com.pico.spatial.core.ecs.ModelEntity
import com.pico.spatial.core.ecs.PointLightComponent
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.ecs.resource.BlendingMode
import com.pico.spatial.core.ecs.resource.MeshResource
import com.pico.spatial.core.ecs.resource.PhysicallyBasedMaterial
import com.pico.spatial.core.ecs.resource.PhysicsMaterialResource
import com.pico.spatial.core.ecs.resource.ShapeResource
import com.pico.spatial.core.ecs.resource.UnlitMaterial
import com.pico.spatial.core.math.Color4
import com.pico.spatial.core.math.EulerAngles
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.ui.foundation.content.SpatialViewAttachments
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val BASE_POSITION = Vector3(0f, 0f, 0f)
private val FOCUS_TARGET = Vector3(0f, 0.10f, 0.16f)
private val FACTS_PANEL_POSITION = Vector3(0.24f, 0.10f, 0.16f)
private val HINT_PANEL_POSITION = Vector3(0f, 0.40f, -0.04f)
private const val BASE_SCALE = 0.30f
private const val FOCUS_SCALE = 0.48f
private const val SUN_RADIUS = 0.18f

const val PANEL_FACTS = "planet_facts"
const val PANEL_HINT = "explore_hint"

/**
 * Owns the code-built Stage scene and the resource references needed to update
 * focus state. The scene is built once from [SpatialView]'s initial callback.
 */
class SolarSystemSceneState {
    val planetIdByEntity = mutableMapOf<Entity, String>()

    private val planetEntities = mutableMapOf<String, Entity>()
    private val planetComponents = mutableMapOf<String, PlanetOrbitComponent>()
    private val planetMaterials = mutableMapOf<String, PhysicallyBasedMaterial>()
    private val planetOriginalColors = mutableMapOf<String, Color4>()
    private val planetHalos = mutableMapOf<String, Entity>()
    private val planetRadiusById = mutableMapOf<String, Float>()
    private val topLevelEntities = mutableListOf<Entity>()

    private var rootEntity: Entity? = null
    private var rootComponent: OrreryRootComponent? = null
    private var orbitMaterial: UnlitMaterial? = null
    private var factsPanelEntity: Entity? = null
    private var hintPanelEntity: Entity? = null
    private var pendingFocusId: String? = null

    fun build(
        content: SpatialViewContent,
        attachments: SpatialViewAttachments,
        planets: List<Planet>,
    ) {
        if (rootEntity != null) return

        val root = Entity()
        root.components.set(
            TransformComponent(BASE_POSITION, EulerAngles(0f, 0f, 0f), Vector3(BASE_SCALE))
        )
        val rootState = OrreryRootComponent(BASE_POSITION, BASE_SCALE)
        root.components.set(rootState)
        rootEntity = root
        rootComponent = rootState
        content.addEntity(root)

        buildBackdrop(content)
        buildSun(root)
        buildOrbits(root, planets)
        planets.forEach { planet -> buildPlanet(root, planet) }
        buildDirectionalLight(content)

        attachments.entity(PANEL_FACTS)?.let { panel ->
            ensureTransform(panel).setPosition(FACTS_PANEL_POSITION)
            content.addEntity(panel)
            factsPanelEntity = panel
        }
        attachments.entity(PANEL_HINT)?.let { panel ->
            ensureTransform(panel).setPosition(HINT_PANEL_POSITION)
            content.addEntity(panel)
            hintPanelEntity = panel
        }

        applyFocus(pendingFocusId)
    }

    fun applyFocus(focusedPlanetId: String?) {
        pendingFocusId = focusedPlanetId
        val rootState = rootComponent ?: return
        rootState.focusedPlanetId = focusedPlanetId

        if (focusedPlanetId != null) {
            val orbit = planetComponents[focusedPlanetId] ?: return
            planetComponents.values.forEach { it.paused = true }

            val localX = orbit.orbitRadius * cos(orbit.angle)
            val localZ = orbit.orbitRadius * sin(orbit.angle)
            rootState.targetPosition = Vector3(
                FOCUS_TARGET.x - localX * FOCUS_SCALE,
                FOCUS_TARGET.y,
                FOCUS_TARGET.z - localZ * FOCUS_SCALE,
            )
            rootState.targetScale = FOCUS_SCALE

            planetEntities.forEach { (id, entity) ->
                val selected = id == focusedPlanetId
                val material = planetMaterials[id]
                val original = planetOriginalColors[id]
                if (selected) {
                    material?.setBaseColor(SolarSystemColors.color4("focusRing"))
                } else if (material != null && original != null) {
                    material.setBaseColor(SolarSystemColors.dimColor4(original))
                }
                ensureTransform(entity).setScaleVector(Vector3(if (selected) 1.4f else 1f))
                planetHalos[id]?.let { halo ->
                    val radius = planetRadiusById[id] ?: 0f
                    ensureTransform(halo).setScaleVector(
                        Vector3(if (selected) radius * 1.9f else 0.001f)
                    )
                }
            }
            orbitMaterial?.setOpacity(0.3f)
        } else {
            planetComponents.values.forEach { it.paused = false }
            rootState.targetPosition = rootState.basePosition
            rootState.targetScale = rootState.baseScale

            planetEntities.forEach { (id, entity) ->
                planetMaterials[id]?.let { material ->
                    planetOriginalColors[id]?.let { original -> material.setBaseColor(original) }
                }
                ensureTransform(entity).setScaleVector(Vector3(1f))
                planetHalos[id]?.let { halo ->
                    ensureTransform(halo).setScaleVector(Vector3(0.001f))
                }
            }
            orbitMaterial?.setOpacity(0.55f)
        }
    }

    fun destroy() {
        rootEntity?.destroy()
        factsPanelEntity?.destroy()
        hintPanelEntity?.destroy()
        topLevelEntities.forEach { it.destroy() }
        rootEntity = null
        rootComponent = null
        factsPanelEntity = null
        hintPanelEntity = null
        topLevelEntities.clear()
        planetIdByEntity.clear()
        planetEntities.clear()
        planetComponents.clear()
        planetMaterials.clear()
        planetOriginalColors.clear()
        planetHalos.clear()
        planetRadiusById.clear()
        orbitMaterial = null
        pendingFocusId = null
    }

    private fun buildSun(root: Entity) {
        val sunMaterial = PhysicallyBasedMaterial.create(BlendingMode.OPAQUE).apply {
            setBaseColor(SolarSystemColors.color4("sunCore"))
            setEmissiveColor(SolarSystemColors.color4("sunGlow"))
            setRoughness(0.45f)
            setMetallic(0f)
        }
        val sun = ModelEntity(MeshResource.createSphere(SUN_RADIUS), sunMaterial)
        root.addChild(sun)

        val glow = SolarSystemColors.color4("sunGlow")
        val glowMaterial = UnlitMaterial.create(BlendingMode.ADD).apply {
            setBaseColor(Color4(glow.red, glow.green, glow.blue, 0.28f))
            setDepthWrite(false)
        }
        val glowEntity = ModelEntity(MeshResource.createSphere(SUN_RADIUS * 1.5f), glowMaterial)
        sun.addChild(glowEntity)

        val light = Entity()
        ensureTransform(light)
        light.components.set(
            PointLightComponent(SolarSystemColors.color4("sunGlow"), 500f, 1.0f)
        )
        root.addChild(light)
    }

    private fun buildOrbits(root: Entity, planets: List<Planet>) {
        val material = UnlitMaterial.create(BlendingMode.TRANSPARENT).apply {
            setBaseColor(SolarSystemColors.color4("orbitLine"))
            setOpacity(0.55f)
            setDepthWrite(false)
        }
        orbitMaterial = material

        planets.forEach { planet ->
            val mesh = MeshResource.createTorus(
                outerRingRadius = planet.orbitRadiusMeters + 0.0015f,
                innerRingRadius = planet.orbitRadiusMeters - 0.0015f,
            )
            val orbit = ModelEntity(mesh, material)
            ensureTransform(orbit).setEulerAngles(EulerAngles(90f, 0f, 0f))
            root.addChild(orbit)
        }
    }

    private fun buildPlanet(root: Entity, planet: Planet) {
        val originalColor = SolarSystemColors.color4(planet.colorToken)
        val material = PhysicallyBasedMaterial.create(BlendingMode.OPAQUE).apply {
            setBaseColor(originalColor)
            setRoughness(0.72f)
            setMetallic(0f)
        }
        val angle = Math.toRadians(planet.startAngleDegrees.toDouble()).toFloat()
        val entity = ModelEntity(
            MeshResource.createSphere(planet.radiusMeters),
            material,
        )
        ensureTransform(entity).setPosition(
            Vector3(
                planet.orbitRadiusMeters * cos(angle),
                0f,
                planet.orbitRadiusMeters * sin(angle),
            )
        )

        entity.components.set(
            CollisionComponent(
                collisionShape = listOf(ShapeResource.createSphere(planet.radiusMeters)),
                physicsMaterial = PhysicsMaterialResource(),
            )
        )
        entity.components.set(InteractableComponent())
        entity.components.set(HoverEffectComponent())

        val orbit = PlanetOrbitComponent(
            orbitRadius = planet.orbitRadiusMeters,
            angularSpeed = ((PI * 2.0) / planet.orbitDurationSeconds).toFloat(),
            angle = angle,
        )
        entity.components.set(orbit)

        val haloMaterial = UnlitMaterial.create(BlendingMode.ADD).apply {
            setBaseColor(
                Color4(
                    SolarSystemColors.focusRing.red,
                    SolarSystemColors.focusRing.green,
                    SolarSystemColors.focusRing.blue,
                    0.18f,
                )
            )
            setDepthWrite(false)
        }
        val halo = ModelEntity(MeshResource.createSphere(1f), haloMaterial)
        ensureTransform(halo).setScaleVector(Vector3(0.001f))
        entity.addChild(halo)

        root.addChild(entity)

        planetEntities[planet.id] = entity
        planetComponents[planet.id] = orbit
        planetMaterials[planet.id] = material
        planetOriginalColors[planet.id] = originalColor
        planetHalos[planet.id] = halo
        planetRadiusById[planet.id] = planet.radiusMeters
        planetIdByEntity[entity] = planet.id
    }

    private fun buildBackdrop(content: SpatialViewContent) {
        val mesh = MeshResource.createPlane(0.9f, 0.9f, 0.06f)
        val material = UnlitMaterial.create(BlendingMode.OPAQUE).apply {
            setBaseColor(SolarSystemColors.color4("spaceBase"))
        }
        val backdrop = ModelEntity(mesh, material)
        ensureTransform(backdrop).setPosition(Vector3(0f, 0f, -0.40f))
        content.addEntity(backdrop)
        topLevelEntities.add(backdrop)
    }

    private fun buildDirectionalLight(content: SpatialViewContent) {
        val light = Entity()
        ensureTransform(light).setEulerAngles(EulerAngles(-55f, 0f, 0f))
        light.components.set(
            DirectionalLightComponent(
                SolarSystemColors.color4("star"),
                300f,
                castsShadowEnabled = false,
            )
        )
        content.addEntity(light)
        topLevelEntities.add(light)
    }

    private fun ensureTransform(entity: Entity): TransformComponent =
        entity.components[TransformComponent::class.java]
            ?: TransformComponent().also { entity.components.set(it) }
}
