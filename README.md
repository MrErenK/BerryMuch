# Berry Much 🫐

A Minecraft Fabric mod that makes berries actually nutritious! Finally, eating 3 berries gives you the nutrition of 3 berries.

## The Problem

- **Sweet Berries**: Shows 3 berries in texture → Only gives 2 hunger points
- **Glow Berries**: Shows 2 berries in texture → Only gives 2 hunger points
- **Melon Slice**: High water content → Still poor saturation

This mod fixes these visual inconsistencies!

## What Changes

| Food | Before | After | Why |
|------|--------|-------|-----|
| **Sweet Berries** | 2 hunger, 1.2 saturation | **6 hunger, 1.2 saturation** | 3 berries = 3× nutrition |
| **Glow Berries** | 2 hunger, 1.2 saturation | **4 hunger, 0.8 saturation** | 2 berries = 2× nutrition |
| **Melon Slice** | 2 hunger, 1.2 saturation | **2 hunger, 2.4 saturation** | Better hydration |

## Configuration

The mod creates a config file at `config/berry-much.json` that you can edit:

```json
{
  "items": [
    {
      "itemId": "minecraft:sweet_berries",
      "nutrition": 6,
      "saturation": 1.2
    }
  ]
}
```

You can add any food item and customize its nutrition values. Run /berrymuch reload to reload the config in-game.

## Installation
## Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/MrErenK/BerryMuch.git
   cd BerryMuch
   ```

2. Build the mod:
   ```bash
   ./gradlew build
   ```

3. Find the built `.jar` file in `build/libs/`

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://modrinth.com/mod/fabric-api)
2. Download the mod from [GitHub Releases](https://github.com/MrErenK/BerryMuch/releases) or directly install on [Modrinth](https://modrinth.com/mod/berry-much) App
3. Put the `.jar` file in your `mods` folder
4. Enjoy!

## Requirements

- **Minecraft**: 1.20.6 - 1.21.8
- **Fabric Loader**: 0.16.14+
- **Fabric API**: Required

## Compatibility

- ✅ Works on both client and server
- ✅ Only required on server for multiplayer
- ⚠️ May conflict with other food-modifying mods that modify the foods in the config

## License

MIT License - see [LICENSE](LICENSE) for details.

---

*"If it looks like 3 berries, it should feed like 3 berries!"* 🫐🫐🫐
