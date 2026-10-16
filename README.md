# Pivot Cam

Client-only Fabric mod that renders the world from a configurable pivot point, with optional rotation transforms (pitch/yaw invert and offset), a telescope offset along the view direction, and an optional vertical image flip.

Nothing is sent to the server: your real look direction stays vanilla. Only the rendered camera changes.

## Supported versions

| Jar | Minecraft |
| --- | --- |
| `pivot-cam-*-1.21.11.jar` | 1.21.11 |
| `pivot-cam-*-26.1.jar` | 26.1 – 26.1.2 |
| `pivot-cam-*-26.2.jar` | 26.2 |
| `pivot-cam-*-26.3.jar` | 26.3 |

Requires [Fabric Loader](https://fabricmc.net/use/installer/) and [Fabric API](https://modrinth.com/mod/fabric-api).

## Commands

All commands are client-side. Settings are saved to `config/pivot-cam.json`. The camera always starts stopped and turns off on disconnect.

| Command | Effect |
| --- | --- |
| `/pivotcam start` / `stop` / `toggle` | Run or stop the camera |
| `/pivotcam pivot <x> <y> <z>` | Set absolute pivot position |
| `/pivotcam pivot here` | Set pivot to your eye position |
| `/pivotcam telescope <blocks>` | Offset along the rendered view direction |
| `/pivotcam pitch invert <true\|false>` | Negate pitch for rendering |
| `/pivotcam pitch offset <degrees>` | Add a pitch offset |
| `/pivotcam yaw invert <true\|false>` | Negate yaw for rendering |
| `/pivotcam yaw offset <degrees>` | Add a yaw offset |
| `/pivotcam flip <true\|false>` | Flip the world image vertically (HUD stays upright) |
| `/pivotcam info` | Print current settings |

### Example (inverted pitch + upside-down view)

```
/pivotcam pivot -948.62 25.88 1004.99
/pivotcam pitch invert true
/pivotcam flip true
/pivotcam telescope 2
/pivotcam start
```

## Building

Needs a JDK that can run Gradle 9 (JDK 25 is fine). The build downloads Java 21 for 1.21.11 automatically.

```bat
gradlew buildAndCollect
```

Jars land in `build/libs/1.0.0/`.

Active development target is **26.3** (`stonecutter active "26.3"`).

## Side effects while running

- Sounds are heard from the camera position.
- Crosshair / block targeting still use your real head.
- Chunks around a distant pivot may not be loaded.

## License

MIT
