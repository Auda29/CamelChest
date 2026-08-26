# Camel Chest

Camel Chest is a Minecraft mod for Fabric and NeoForge. It lets adult camels carry a chest and 15 inventory slots, matching the storage behavior of donkeys and mules.

## Use

1. Right-click an adult camel with a chest.
2. Sneak and right-click the camel to open its inventory.
3. Use the 15 storage slots next to the saddle slot.

The camel keeps its inventory when the world is saved. If it dies, it drops the stored items and the attached chest. The chest is also visible on both sides of the camel.

## Requirements

| Minecraft | Loader | Java | Extra dependency |
| --- | --- | --- | --- |
| `26.2` | Fabric Loader `0.19.3` or newer | `25` | None |
| `26.2` | NeoForge `26.2.0.66` or newer | `25` | None |
| `1.21.11` | Fabric Loader `0.19.3` or newer | `21` | None |
| `1.21.11` | NeoForge `21.11.45` or newer | `21` | None |
| `1.21.1` | Fabric Loader `0.19.3` or newer | `21` | None |
| `1.21.1` | NeoForge `21.1.248` or newer | `21` | None |

Install the mod on both the client and server for multiplayer. Use only the JAR matching the active loader.

## Build

Build and test all supported target pairs:

```powershell
.\gradlew.bat clean build
.\gradlew.bat -p versions/1.21.11 clean build
.\gradlew.bat -p versions/1.21.1 clean build
```

The production JARs are written to:

- `fabric/build/libs/camel-chest-0.1.1+mc26.2-fabric.jar`
- `neoforge/build/libs/camel-chest-0.1.1+mc26.2-neoforge.jar`
- `versions/1.21.11/fabric/build/libs/camel-chest-0.1.1+mc1.21.11-fabric.jar`
- `versions/1.21.11/neoforge/build/libs/camel-chest-0.1.1+mc1.21.11-neoforge.jar`
- `versions/1.21.1/fabric/build/libs/camel-chest-0.1.1+mc1.21.1-fabric.jar`
- `versions/1.21.1/neoforge/build/libs/camel-chest-0.1.1+mc1.21.1-neoforge.jar`

Start a development client with one of these commands:

```powershell
.\gradlew.bat runFabricClient
.\gradlew.bat runNeoForgeClient
```

## Publishing

The workflows under `.github/workflows/` use the same release chain as Ore Height Indicator:

1. `Release` builds and tests both loader targets, creates SHA-256 files and publishes a GitHub release.
2. `Publish Modrinth` downloads those GitHub release assets, verifies the checksums and uploads both loader variants.
3. `Publish CurseForge` verifies the same assets and uploads both loader variants to CurseForge.

A push to `main` creates the tag matching `mod_version` when that tag does not exist yet. A pushed `v*` tag must match `mod_version` and point to a commit contained in `main`.

Configure these repository settings before publishing outside GitHub:

| Type | Name | Value |
| --- | --- | --- |
| GitHub Actions secret | `MODRINTH_TOKEN` | Modrinth personal access token with version upload permission |
| GitHub Actions variable | `MODRINTH_PROJECT_ID` | Modrinth project ID or slug |
| GitHub Actions secret | `CURSEFORGE_API_TOKEN` | CurseForge API token |
| GitHub Actions variable | `CURSEFORGE_PROJECT_ID` | Numeric CurseForge project ID |

The Modrinth and CurseForge workflows also support manual runs with an existing GitHub release tag. Do not reuse a tag that has already been published on the target platform.

## Project layout

- `common/`: shared camel behavior, tests and current chest rendering
- `fabric/`: Fabric entry point and metadata
- `neoforge/`: NeoForge entry point and metadata
- `versions/`: Minecraft-version-specific build files and compatibility sources

The loaders produce separate binaries because their startup and metadata formats differ. The actual camel behavior stays in one shared source set.

## License

Camel Chest is distributed under the [MIT License](LICENSE).
