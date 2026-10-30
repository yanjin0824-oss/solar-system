# Spatial App Design Doc — Interactive 3D Solar System

## 1 · Frame

**Subject** — A stylized, sun-centered orrery. The brief is a playful astronomy exhibit rather than a data dashboard: five planets orbit a glowing sun at different speeds, and selecting one reveals a compact fact card.

**Viewer & context** — A curious explorer seated at a shared desk, viewing a transparent volumetric orrery that floats in Shared Space. The scene is read primarily by looking around and pointing; the fact card is read at arm's length.

**The single job** — Inspect one planet at a time and learn one quick fact about it.

**Spatial justification**

| Core task | Affordance | Why spatial beats flat | 2D counterfactual |
| --- | --- | --- | --- |
| Understand orbital order and relative motion | Direction, distance, motion, simulation | Five orbits at different radii and speeds are read as one continuous spatial system; the user can look around the sun and see motion without scrubbing a timeline. | A flat animated diagram can show orbits, but the viewer cannot move through depth or inspect a planet in place. |
| Focus on one planet | Position, scale, depth | Selecting a planet can pull the viewer toward that body while the rest of the system recedes; the focused planet and its card share one spatial anchor. | A flat app would swap a canvas for a detail panel; the sense of moving toward the selected body is lost. |
| Read quick facts without losing context | Position, depth | A small anchored card appears beside the focused planet, keeping the planet visible as the context for the facts. | A modal or side panel would cover or disconnect the selected object. |

The justification selects a **Volumetric WindowContainer in Shared Space**: the orrery is a 3D subject inspected in the round, while the window keeps the user anchored in their real room.

**Assumptions**

| Assumption | Confidence | What changes if wrong |
| --- | --- | --- |
| The five planets are Mercury, Venus, Earth, Mars, and Jupiter with recognizable stylized colors. | High | The planet palette and facts would be replaced; the orbit geometry and card structure stay valid. |
| The experience is a single Shared Space volumetric window, not a Full Space Stage. | High | A Planar surface would flatten the orrery; a Stage would replace the room entirely. |
| A short anchored popup is preferable to a persistent Subwindow for the fact card. | High | If the user needs persistent comparison, the card would become a Subwindow or a separate detail surface. |
| Planet data is local and does not require a network request. | Medium | If facts are remote, the fallback and error data cases become live loading states. |

## 2 · Explore — alternatives

**Direction generation** — Inline-isolated. The host did not expose a separate design-context subagent, so each direction was developed independently in reasoning before comparison, with the same divergence bar.

### Direction A — Immersive orrery (chosen)

A Shared Space volumetric sun-centered orrery with five planets moving at different orbital speeds. Selecting a planet pulls it to the center of the view, dims the other bodies, and opens a small anchored dossier card beside it.

- Signature: a warm, glowing sun at the center of nested orbital rings.
- Main risk: the card can occlude the focused planet if anchored poorly.

### Direction B — Volumetric tabletop model

A small holographic orrery sitting on a real table in Shared Space, with a separate Planar detail window. The model is inspected from above and around.

- Signature: a physical tabletop object with a floating data window.
- Main risk: the small model and separate window split attention; the user must look between two surfaces for one fact.

### Direction C — Planar mission-control board

A large wall panel showing an animated orbital diagram, a planet list, and a detail pane. Selecting a row updates the diagram and the facts.

- Signature: a broad animated orbital chart with a dense facts panel.
- Main risk: it is a 2D dashboard with a space skin; motion and depth are decorative rather than useful.

**Decision matrix**

| Direction | Task efficiency | Spatial value | PICO comfort | Domain fit | Risk | Distinctiveness |
| --- | --- | --- | --- | --- | --- | --- |
| A | High — one click focuses and reveals | High — motion, scale, depth, position | High — one card at arm's length | High — the orrery is the subject | Medium — card anchoring | High |
| B | Medium — split attention | Medium — inspectable model, but card detached | Medium — tabletop posture | High | Medium | Medium |
| C | High — dense information | Low — mostly flat | Medium — large wall panel | Low — dashboard habit | Low | Low |

**Chosen: A** — It gives the planet selection a physical consequence (camera focus), keeps the facts next to the selected body, and makes orbital motion the primary experience rather than a decorative chart.

**Rejected** — B lost because the separate detail window breaks the one-object focus and adds a second surface for one fact. C lost because it reduces the solar system to a dashboard; the domain-swap test would leave a generic analytics layout.

## 3 · Plan

**Executable spec** — `.scratch/design-spec.json`, schema version `1.0`, revision `2`.

### Visual tokens

All 16 public `ColorScheme` roles inherit the same-name SpatialUI Vibrant role. No native role is redefined. Custom colors live only in `theme.brandColors`.

| `ColorScheme` role | Required value | Used for |
| --- | --- | --- |
| `fillPrimary` | inherit SpatialUI Vibrant `fillPrimary` | Icon button shell |
| `fillSecondary` | inherit SpatialUI Vibrant `fillSecondary` | Planet part fallback |
| `fillTertiary` | inherit SpatialUI Vibrant `fillTertiary` | Reserved for component-owned states |
| `fillLight` | inherit SpatialUI Vibrant `fillLight` | Reserved for component-owned states |
| `labelPrimaryLight` | inherit SpatialUI Vibrant `labelPrimaryLight` | Explore hint on the dark scene |
| `labelPrimary` | inherit SpatialUI Vibrant `labelPrimary` | Card title and stat values |
| `labelSecondary` | inherit SpatialUI Vibrant `labelSecondary` | Card subtitle and description |
| `labelTertiary` | inherit SpatialUI Vibrant `labelTertiary` | Stat labels |
| `labelQuaternary` | inherit SpatialUI Vibrant `labelQuaternary` | Card note |
| `lightenHover` | inherit SpatialUI Vibrant `lightenHover` | Component hover |
| `lightenPressed` | inherit SpatialUI Vibrant `lightenPressed` | Component pressed |
| `error` | inherit SpatialUI Vibrant `error` | Reserved |
| `alert` | inherit SpatialUI Vibrant `alert` | Reserved |
| `passable` | inherit SpatialUI Vibrant `passable` | Reserved |
| `interaction` | inherit SpatialUI Vibrant `interaction` | Reserved |
| `dividerLine` | inherit SpatialUI Vibrant `dividerLine` | Card divider |

**Brand / decorative tokens outside `ColorScheme`**

| Token name | Exact hex | Used for |
| --- | --- | --- |
| `spaceBase` | `#050714` | Deep-space scene base |
| `spaceNebula` | `#171C46` | Central nebula wash |
| `star` | `#FFFFFF` | Star field and specular highlights |
| `sunCore` | `#FFF3C4` | Sun core |
| `sunGlow` | `#FFB347` | Sun glow |
| `orbitLine` | `#3A4166` | Orbit rings |
| `focusRing` | `#FFD27A` | Selected-planet fill and ring |
| `mercury` | `#B8B2A6` | Mercury body |
| `venus` | `#E8C27A` | Venus body |
| `earth` | `#4FA3D9` | Earth body and card icon |
| `mars` | `#C65D45` | Mars body |
| `jupiter` | `#D8A56A` | Jupiter body |

**Semantic colors** — The only state color is the selected-planet highlight: `focusRing`, reinforced by a ring around the planet and by the anchored card. No other status semantics are encoded by color.

**Type** — `headlineLarge` (24/30/700) is the single surface title in the popup. All other app-authored text uses `bodyMedium`, `bodyMediumMultiline`, `bodySmall`, `titleSmall`, or `labelSmall`, all below the 20sp ceiling. The stronger 24sp token is necessary because the planet name is the card's semantic title and must remain the largest local text. No TitleBar or SideNavigation slot overrides exist.

**Depth** — Depth is product-owned only in the scene: orbit radii and the selected planet's focus scale. The card is a SpatialPopup anchored beside the planet, not a manually z-offset window. No `rootMaterial` or per-node material fields are used.

**Window-background premise** — This is a Volumetric WindowContainer in Shared Space; no Planar material shell is applied. The volume is transparent (`materialbackground=0`) with the base panel hidden, so the orrery floats in the room over a dark backdrop plane. The preview body remains the fixed environment `#DAD6D3` with no background image.

### Signature element

The glowing sun at the center of five concentric orbit rings. The scene stays quiet around it: no HUD chrome, no navigation, no decorative panels. The only other surface is the small planet dossier card.

### Surface sizing

| Window / surface | Type | Content | Default | Min | Max |
| --- | --- | --- | --- | --- | --- |
| `main` | Volumetric WindowContainer (Shared Space) | Sun, five orbiting planets, anchored planet facts card | 960 × 960 × 960 dp | 640 × 640 × 640 dp | 1600 × 1600 × 1600 dp |

The browser preview is validated at the 960 × 960 volume default and the 640 × 640 minimum. The compact card (280dp) is used below 820dp and remains fully inside the volume.

### Layout

```
+--------------------------------------------------------------+
|  [sun icon] Point at a planet and click to focus              |
|                                                              |
|                         * orbit *                            |
|                    *                 *                       |
|               *          ( SUN )          *                  |
|                    *                 *                       |
|                         * orbit *                            |
|                                                              |
|                                          +-----------------+ |
|                                          | planet icon  X  | |
|                                          | PLANET NAME     | |
|                                          | type            | |
|                                          | quick fact ...  | |
|                                          | ---------------- | |
|                                          | stat     stat   | |
|                                          | stat     stat   | |
|                                          | stylized note   | |
|                                          +-----------------+ |
+--------------------------------------------------------------+
```

The selected planet moves to the visual center; the card sits to its right with a 32dp gap. The hint is top-left with a 32dp inset (16dp at compact width). There are no peer regions: the scene is the primary content, the hint is auxiliary support, and the card is selection-derived detail.

### Major-region qualification

| Proposed region | Independent task | Unique information | Lifecycle / interaction | Coordinate compatibility | Duplication test | Delete test | Decision + rationale |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Scene | Yes — inspect the system | Yes — orbital order, motion, planet identity | Persistent, continuously animating | Owns the shared spatial coordinates | No duplicate | Removing it removes the product | Primary content, inline |
| Hint | No — supports the scene | No — repeats the click instruction | Appears only before focus | Anchored to the scene's top-left | Does not duplicate data | Removing it hurts discoverability slightly | Auxiliary support, inline |
| Fact card | Yes — read one planet's facts | Yes — details not visible in the scene | Selection-driven, dismissible | Anchored to the selected planet | No duplicate | Removing it removes the payoff | Selection-derived detail, SpatialPopup |

The card is not a peer column. It is a small anchored surface that exists only for the selected object, so a SpatialPopup is the correct carrier rather than an inline column or a persistent Subwindow.

### Component affordance route

| Region / command surface | Role | Trigger | Relationship | Chosen presentation | Rejected alternatives + reason |
| --- | --- | --- | --- | --- | --- |
| Solar-system scene | content | always-visible | peer | inline `domain_visual` | Planar canvas would flatten the immersive subject. |
| Explore hint | auxiliary | always-visible | supports | inline text + icon | Popup would add chrome for a one-line hint. |
| Planet facts | detail | selection-driven | derives-from-selection | `SpatialPopup` | Subwindow is too persistent for one fact; Sheet is modal and not anchored; inline detail would occlude the scene. |
| Dismiss | action-feedback | command-driven | supports | `sui-icon-button` in the card header | Text button would add a second command row. |

### State graph

| State | Primary task | Primary focus | Components | Data deps | Entry/exit |
| --- | --- | --- | --- | --- | --- |
| `overview` | Choose a planet | The moving orrery and the hint | Scene, hint | Normal planet visual data | Entry: app launch or card dismissal |
| `planet-focus` | Read the selected planet's facts | The focused planet and the card | Scene (focused), popup card | Selected planet facts; normal/fallback/error cases | Entry: planet select; exit: close or dismiss |

| Transition | From → To | Trigger | Explicit confirm? |
| --- | --- | --- | --- |
| `open-planet` | `overview` → `planet-focus` | `scene` / `planet-select` | No |
| `close-planet` | `planet-focus` → `overview` | `fact-close` / `click-action` | No |
| `dismiss-planet` | `planet-focus` → `overview` | `fact-popup` / `dismiss-request` | No |

### Components

- **Orrery scene** — `domain_visual` renderer `orrery-scene`. Serves inspection and selection. Data: five planet visual records (id, color token, orbit radius, duration, size, start angle), sun radius, star count/seed. States: overview (all planets moving, no selection) and focused (orbits paused, selected planet at center, others dimmed). Emits `planet-select`.
- **Facts card** — `sui-spatial-popup` anchored to the selected planet. Serves quick facts. Data: selected planet name, type, one-line fact, and four stats. States: normal, fallback, error. Dismisses through its public event or the close IconButton.
- **Close control** — `sui-icon-button` with `ic_cross`, regular size, white-on-dark native shell. Serves dismissal.
- **Planet identity icon** — `sui-icon` with `ic_planet`, tinted to the selected planet's brand token.
- **Divider** — `sui-divider` separates the fact sentence from the stat grid.
- **Text** — `sui-text` nodes for the hint, title, subtitle, description, four stat pairs, and the stylized-scale note.

### SpatialUI Web component map

| Planned element | SpatialUI tag | API checked in | Public event/state | Custom fallback reason |
| --- | --- | --- | --- | --- |
| Anchored facts card | `sui-spatial-popup` | `assets/spatialui-web/js/spatial-popup.js` | `dismiss-request` | n/a |
| Close control | `sui-icon-button` | `assets/spatialui-web/js/icon-button.js` | `click-action` | n/a |
| Planet icon / close icon / hint icon | `sui-icon` | `assets/spatialui-web/js/icon.js` | inherits `currentColor` | n/a |
| Fact divider | `sui-divider` | `assets/spatialui-web/js/divider.js` | component-owned | n/a |
| All text | `sui-text` | `assets/spatialui-web/js/text.js` | inherits component slot color | n/a |
| Solar-system canvas | `domain_visual` | renderer contract | `planet-select` | No SpatialUI control represents an orbiting 3D orrery; custom rendering is the domain-specific visualization. |

## 4 · Critique (before build)

**Generic-default simulation** — A lazy prompt would produce a near-black dashboard with a neon-teal accent, a grid of planet tiles, and a side panel. That is a space-themed analytics app, not an orrery.

**Where the plan risked that default, and what I changed** — The plan deliberately removed all dashboard chrome: no navigation, no tile grid, no side rail. The palette comes from the subject (warm sun, mineral planet colors, indigo void), and the primary interaction is spatial camera focus rather than list selection. The card is small and anchored to the object it describes.

**Hard checks**

| Check | Pass? | Evidence |
| --- | --- | --- |
| ≥3 real alternatives with rejection reasons | Yes | Directions A/B/C and decision matrix above |
| Every core task has a 2D counterfactual | Yes | Frame table |
| Form/scale follows spatial justification | Yes | Volumetric WindowContainer selected from motion/depth/position |
| Every window/surface size internally validated | Yes | 960dp volume and 960/640 browser checks |
| Attachments justified vs None/in-place | Yes | SpatialPopup compared with Subwindow, Sheet, and inline |
| Every peer region passes all six qualification tests | Yes | Only the scene is peer; hint and card are subordinate |
| Component affordance route keeps non-peer content out of primary Row/Grid | Yes | Hint is inline auxiliary; card is a popup |
| Domain-swap test | Yes | Swapping the domain would break the orbit geometry, sun glow, and planet palette |
| Every core component has data source + task | Yes | Components section |
| All 16 `ColorScheme` roles explicitly defined | Yes | `design-spec.json.theme.colorScheme` |
| Every standard control/system surface maps to SpatialUI Web | Yes | Component map |
| `design-spec.json` valid and references resolve | Yes | Schema validation and surface-discipline check |
| Surface-discipline check passes | Yes | `PASS errors=0 warnings=0` |
| Repeated-item depth is state/data-driven, never index-driven | Yes | Only the selected planet receives the focus state |
| Single primary focus / clear decision | Yes | Inspect one planet at a time |

Only after these checks passed was `preview.html` generated.

## 5 · Build — coverage manifest

| # | Kind | JSON ID/path | Implemented in `preview.html` |
| --- | --- | --- | --- |
| 1 | Surface | `main` (`stage_full`) | `#preview-root .stage`, full viewport |
| 2 | State | `overview` | Initial render, hint visible, no card |
| 3 | State | `planet-focus` | Focused render, card visible |
| 4 | Transition | `open-planet` | Planet click / keyboard activation |
| 5 | Transition | `close-planet` | Close IconButton `click-action` |
| 6 | Transition | `dismiss-planet` | Popup `dismiss-request` |
| 7 | Action | `select-planet` | `selectPlanet(planetId)` |
| 8 | Action | `focus-planet` | State change to `planet-focus` |
| 9 | Action | `close-facts` | `closeFacts()` |
| 10 | Action | `dismiss-facts` | Popup event handler |
| 11 | Node | `stage-root` | `.stage > div[data-node-id="stage-root"]` |
| 12 | Node | `scene` | `.orrery-scene[data-node-id="scene"]` |
| 13 | Node | `hint-layer` | `.hint-layer` with `sui-icon` + `sui-text` |
| 14 | Node | `fact-popup` | `sui-spatial-popup[data-node-id="fact-popup"]` |
| 15 | Node | `fact-card` and descendants | `[data-node-id="fact-card"]` subtree |
| 16 | Node | `fact-close` / `close-icon` | `sui-icon-button` + inline `ic_cross` SVG |
| 17 | Node | `fact-icon` | `sui-icon` + inline `ic_planet` SVG |
| 18 | Node | `fact-divider` | `sui-divider` |
| 19 | Binding | `selectedPlanet.*` | Text bindings update for each planet and data case |
| 20 | Data case | `normal` | Five planets with facts |
| 21 | Data case | `fallback` | Same five planets with limited facts |
| 22 | Data case | `error` | Same five planets with unavailable-data copy |
| 23 | Responsive | `compact-popup` | `fact-card` 280dp and hint inset 16dp at ≤820px |
| 24 | Asset | `ic_cross`, `ic_planet`, `ic_sun` | Exact inline currentColor SVGs from ICON 7.0 |
| 25 | Domain visual | `orrery-scene` | Sun, 5 orbit rings, 5 planets, 90 stars, focus transform |

The embedded `design-spec.json` is byte-identical to the external file. No product copy, colors, dimensions, or state targets are authored in the renderer.

## 6 · Critique again

**Independent coverage rebuild** — Rebuilt from `design-spec.json`: 1 stage surface, 2 states, 3 transitions, 4 actions, 32 nodes, 17 `spatialui` nodes, 3 assets, 1 responsive rule, 3 data cases. The rendered DOM contains exactly one element per declared `spatialui` node ID and the expected tag; all component shadow roots are present. The scene renders 90 stars, 5 orbit rings, 5 planets, and 1 sun. The hint is present only in `overview`; the popup is present only in `planet-focus`.

**Rendered evidence**

- Screenshots: `.scratch/screenshots/overview-default.png` (1200×1200 with a 960×960 volume canvas), `.scratch/screenshots/focus-default.png` (1200×1200 with a 960×960 volume canvas), `.scratch/screenshots/focus-compact.png` (800×800 with a 640×640 volume canvas).
- Body computed style: `background-color: rgb(218, 214, 211)`, `background-image: none` in every captured state.
- Volume shell: no Planar material shell is applied; the transparent volumetric window floats the orrery in Shared Space.
- Overview pixel check: scene center is sun core `rgb(255, 243, 196)`; scene corner is deep space `rgb(7, 10, 27)`.
- Focus default: the card material surface is `rgb(221, 221, 221)` at the right of the focused planet; the card is presented beside the selected planet inside the volumetric window.
- Focus compact: the 280dp card is presented beside the selected planet inside the 640×640 volume canvas; card title renders at 24px/700 with a 30px line height.
- Card geometry: 340×306 at default, 280×286 at compact; the card begins 32px to the right of the planet anchor and does not overlap it.
- State coverage: overview, Mars focus, close-button return, popup dismissal, error data, and fallback data were all exercised without console errors.
- DOM mapping: every declared `spatialui` node has exactly one matching element and an open shadow root; no substituted or duplicate tags.
- Part coverage: space base, nebula, sun, orbit, planet body, and star templates are all marked with `data-design-part-node` / `data-design-part`; the selected planet uses the declared `selected` fill. The scene root itself stays transparent.
- Icon coverage: `ic_cross`, `ic_planet`, and `ic_sun` resolve to exact inline currentColor SVGs; no glyph or emoji placeholders.
- ColorScheme: all 16 roles remain same-name native references; brand colors are separate CSS variables.

**Quality bar**

| Criterion | Met? | Evidence |
| --- | --- | --- |
| App's job is a clear decision/task outcome | Yes | Inspect one planet and read one fact |
| Form/scale follows spatial justification | Yes | Shared Space Volumetric WindowContainer from motion/depth/position |
| Spatialization justified affordance-by-affordance | Yes | Frame table |
| ≥3 alternatives compared, rejects have reasons | Yes | Direction matrix |
| Every surface records final default/min/max dimensions | Yes | 4×3×4 m / 3×2.4×3 m / 6×4.5×6 m |
| Design derives from domain | Yes | Orbital geometry, warm sun, planet palette |
| `design-spec.json` schema-valid and references resolve | Yes | Schema + discipline check |
| Embedded JSON exactly matches external spec | Yes | Parsed equality, revision 2 |
| SpatialUI bundle inline; mapped tags registered and event-wired | Yes | `SUI_COMPONENTS` and DOM audit |
| No unjustified native duplicate of a `sui-*` control | Yes | Only domain-visual planet hit targets are custom |
| Component presentations match affordances | Yes | Scene inline; card popup; hint auxiliary inline |
| Root leaves system window background unchanged | Yes | Transparent volumetric window, no material shell |
| Structural layout/domain regions transparent | Yes | `app_surfaces=0/32` |
| Preview backgrounds use canonical fill paths | Yes | Only declared part fills and domain-visual CSS |
| No app-authored content borders or material fields | Yes | Discipline check |
| No design/debug-only controls | Yes | Product UI only |
| `preview.html` is a triggerable state machine | Yes | 2 states, 3 transitions, 3 data cases exercised |
| Peer regions remain justified after rendering | Yes | No peer region added |
| Shared guides, padding, and gap hierarchy coherent | Yes | Card inset 24/16dp; hint inset 32/16dp; 32dp anchor gap |
| Peer controls and primary actions align consistently | Yes | 56×48 close control; header row aligned to title baseline block |
| Dividers do not compensate for layout; content not clipped | Yes | One divider between fact and stats; card fully inside viewport |
| No redundant nested surfaces/rounding | Yes | Single popup material surface; card transparent |
| Initial spec checks rerun after repairs | Yes | Schema + discipline rerun at revision 2 |
| Embedded/external spec parity reverified after repairs | Yes | Parsed equality at revision 2 |
| Signature present, surroundings quiet | Yes | Glowing sun + five orbits; no chrome |
| Device validation | Not performed | Web validates logic/layout only; real-device comfort, occlusion, and physical size remain unverified |

**Patch log**

| Round | Problem | JSON target / renderer defect | Expected improvement | Revision | Re-ran Build+critique? |
| --- | --- | --- | --- | --- | --- |
| 1 | A card placed above the planet could overlap the selected body at compact sizes. | `fact-popup` props changed to `to-end-of` / `center`, `offset-x: 32`; compact card width 280dp. | Card sits beside the planet with a consistent 32dp gap and fits the 700px viewport. | 2 | Yes |
| 2 | Overview retained the last selected planet highlight; popup position could remain stale until a later resize. | Renderer: selection highlight gated on `planet-focus`; popup position refreshed synchronously after reveal. | Overview returns to an unselected orrery; card anchors correctly on every focus. | 2 | Yes |

**Device validation** — `not_performed` (Web validates logic/layout only; real-device comfort, occlusion, size, and performance not verified).

**Verdict** — pass. The design package is complete, the spec is schema-valid and discipline-clean, the embedded JSON matches, the state machine is triggerable, and the rendered geometry and pixel checks pass at default and compact sizes.


## Revision 3 — Shared Space migration (user request)

The user requested that the experience move from a Full Space Stage to **Shared Space**.

- Form: **Volumetric WindowContainer** (`Form.Volumetric`, 960×960×960 dp default, 640×640×640 min, 1600×1600×1600 max).
- Window: transparent (`materialbackground=0`), base panel hidden, caption bar auto-hide, uniform resize, fixed world scale.
- Scene: orrery root scale `0.30` at the volume center; dark `spaceBase` backdrop plane behind the planets for contrast; focus scale `0.48`; selected planet target `(0, 0.10, 0.16)`.
- 2D surfaces: facts panel at `(0.24, 0.10, 0.16)`, hint panel at `(0, 0.40, -0.04)`. The facts card remains a `SpatialPopup` inside an ECS-attached `AttachmentPanel`.
- The Web preview now validates a 960×960 volume canvas centered on the `#DAD6D3` environment; no Planar material shell is used.
- The app UI copy is localized in Chinese; the design IR retains the English design facts.
