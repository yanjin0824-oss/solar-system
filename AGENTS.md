<!-- pico-cli:plugin-context:pico-spatial-agentic-tools:start -->
## Plugin Context

### Spatial Accessibility Runtime Fast Path

When the current project instructions already declare the Spatial Craft CLI entry, device,
package, target, and action arguments for a clear runtime operation:

- execute exactly one declared `interact act` command, one `interact route` command for a
  Contract-declared cross-surface target, or one `interact batch` command for multiple
  ordered actions;
- do not read additional plugin context, invoke a Skill, inspect source, query first, call
  `--help`, or repeat a performed mutation;
- use `--action <string-key>` and `--arguments '<json-object>'`; never use `--args`,
  `--key`, or put JSON after `--action`;
- append `--refresh never --compact --format json` for local transform actions;
- return the compact command result directly.

Exact single-action shape:

```bash
pico-cli interact act --device <serial> --package <package> \
  --semantic-id <semantic-id> --action <string-key> \
  --arguments '<json-object>' --refresh never --compact --format json
```

Exact cross-surface shape:

```bash
pico-cli interact route --device <serial> --project <app-project> \
  --component <contract-id> --action <string-key> \
  --arguments '<json-object>' --compact --format json
```

For development, authoring, audit, unknown targets or arguments, ambiguity, or runtime failure, read `./PICO-SPATIAL-AGENTIC-TOOLS.AGENTS.md` for the full PICO Spatial plugin guidance.
<!-- pico-cli:plugin-context:pico-spatial-agentic-tools:end -->

## Project: Interactive 3D Solar System

This is a PICO Spatial SDK 6.2 **Volumetric WindowContainer in Shared Space** app.
It renders a glowing sun and five stylized planets orbiting at different speeds,
lets the user select a planet with a spatial pointer, and reveals an anchored fact
card. The volume is transparent (`materialbackground=0`) with the base panel hidden.

- Entry: `app/src/main/java/com/example/solarsystem/p6d63b1ca/Main.kt`
- Volume scene: `ui/solar/components/SolarSystemScene.kt` (ECS entities, meshes,
  materials, lights, dark backdrop plane, panels, focus state)
- Orbit/focus systems: `ui/solar/components/OrrerySystems.kt`
- Volume host and pointer input: `ui/solar/components/OrreryStage.kt`
- Fact card: `ui/solar/components/PlanetFactsCard.kt` (SpatialPopup + SpatialUI)
- Hint panel: `ui/solar/components/ExploreHint.kt`
- State: `ui/solar/SolarSystemViewModel.kt` + `SolarSystemUiState.kt`
- Data: `data/repository/FakeSolarSystemRepository.kt`
- Design package: `.scratch/design-doc.md`, `.scratch/design-spec.json`,
  `.scratch/preview.html`

Build and run:

```bash
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.example.solarsystem.p6d63b1ca/.platform.LaunchActivity
```

SpatialUI-only UI rule: all 2D UI uses `com.pico.spatial.ui.design.*` inside
`PicoTheme`; do not introduce Material/Material3. The volume scene uses
`SpatialView` + ECS entities and `AttachmentPanel` for the 2D hint and fact card.
The card surface is the system `SpatialPopup` material; do not add app-authored
backgrounds or borders.

Manifest defaults: `pico.spatial.windowcontainer.style=2`, `defaultsize=960x960x960`,
`defaultsize.unit=dp`, `materialbackground=0`, `volumebasepanel=1`,
`captionbar=1`, `worldscaletype=2`.

Known validation notes: the Web preview validates layout/logic only. Real-device
comfort, occlusion, physical scale, and 3D camera behavior still need on-device
review. Planet facts are stylized educational copy. The app UI copy is localized
in Chinese.

