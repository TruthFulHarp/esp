package com.sajan.caveairesp.modules;

import com.sajan.caveairesp.CaveAirESPAddon;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * Client-side safety module.
 *
 * It cancels ordinary outgoing chat text unless it begins with one of the
 * configured command prefixes. Incoming/server messages are not touched.
 */
public class ChatBlock extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> allowDot = sgGeneral.add(new BoolSetting.Builder()
        .name("allow-dot")
        .description("Allow outgoing messages beginning with '.'.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> allowSlash = sgGeneral.add(new BoolSetting.Builder()
        .name("allow-slash")
        .description("Allow outgoing messages beginning with '/'.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> allowHash = sgGeneral.add(new BoolSetting.Builder()
        .name("allow-hash")
        .description("Allow outgoing messages beginning with '#'.")
        .defaultValue(true)
        .build());

    public ChatBlock() {
        super(CaveAirESPAddon.CATEGORY, "Chat Block",
            "Blocks accidental ordinary outgoing chat and only allows configured command prefixes.");
    }

    @EventHandler
    private void onSendMessage(SendMessageEvent event) {
        String message = event.message();

        if (message == null || message.isEmpty()) {
            event.cancel();
            return;
        }

        char first = message.charAt(0);

        boolean allowed =
            (first == '.' && allowDot.get()) ||
            (first == '/' && allowSlash.get()) ||
            (first == '#' && allowHash.get());

        if (!allowed) {
            event.cancel();
            info("Blocked outgoing chat message. Start commands with ., /, or #.");
        }
    }
}
