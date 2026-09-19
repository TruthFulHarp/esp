# Cave Air ESP

A standalone Meteor Client addon for Minecraft 26.1.2.

## What it does

Cave Air ESP detects connected `CAVE_AIR` blocks and only highlights clusters whose size is between the configured minimum and maximum.

This is intentionally separate from Meteor's normal Block ESP.

### Main settings

- Range
- Min Air Cluster
- Max Air Cluster
- Scan Delay
- Shape Mode
- Side Color
- Line Color

### Example

With:

- Min Air Cluster = 12
- Max Air Cluster = 200

clusters containing 1-11 cave-air blocks are ignored, clusters containing 12-200 are highlighted, and clusters larger than 200 are ignored.

## Build

Use Java 25 and Minecraft 26.1.2.

```text
gradlew.bat build
```

The resulting addon JAR is placed in:

```text
build/libs/
```

Put the JAR in the same Fabric `mods` folder as Meteor Client.

## Important

This project is an independent addon and does not modify Meteor's Block ESP source.
It uses the Meteor addon API and Minecraft's cave-air block type.


## Chat Block

The addon also includes a separate **Chat Block** module.

When enabled, it intercepts only outgoing messages from the client. It allows messages whose first character is:

- `.` — Meteor/client commands
- `/` — Minecraft/server commands
- `#` — Baritone-style commands

Ordinary outgoing text such as:

```text
hello
goto 100 64 100
mine diamond_ore
```

is cancelled locally.

Incoming/server chat is not intercepted by this module.

The prefix checks are configurable in the module settings.

### Fail-safe behavior

The Chat Block is designed as a local client-side event handler. It does not send a replacement message to the server when it blocks something. If the addon/module is disabled or fails to load, it does not install a separate server-side component or packet listener, so it cannot generate a server-visible "blocked" message.

As with any client mod, no software can guarantee that an unrelated Minecraft/Fabric/Meteor crash will never be visible to a server; this module specifically avoids sending error/alert packets as part of its own failure handling.
