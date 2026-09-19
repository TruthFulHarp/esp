package com.sajan.caveairesp.modules;

import com.sajan.caveairesp.CaveAirESPAddon;
import com.sajan.caveairesp.scanner.AirCluster;
import com.sajan.caveairesp.scanner.AirClusterScanner;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.utils.render.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CaveAirESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range").description("Scan radius in blocks.")
        .defaultValue(48).min(8).sliderRange(8,96).build());

    private final Setting<Integer> minCluster = sgGeneral.add(new IntSetting.Builder()
        .name("min-air-cluster").description("Minimum connected cave-air blocks to highlight.")
        .defaultValue(12).min(1).sliderRange(1,200).build());

    private final Setting<Integer> maxCluster = sgGeneral.add(new IntSetting.Builder()
        .name("max-air-cluster").description("Maximum connected cave-air blocks to highlight. Larger clusters are ignored.")
        .defaultValue(200).min(1).sliderRange(10,1000).build());

    private final Setting<Integer> scanDelay = sgGeneral.add(new IntSetting.Builder()
        .name("scan-delay").description("Ticks between scans.")
        .defaultValue(10).min(1).sliderRange(1,40).build());

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode").defaultValue(ShapeMode.Lines).build());

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color").defaultValue(new SettingColor(255, 80, 80, 35)).build());

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color").defaultValue(new SettingColor(255, 80, 80, 255)).build());

    private final List<AirCluster> clusters = new ArrayList<>();
    private int ticks;

    public CaveAirESP() {
        super(CaveAirESPAddon.CATEGORY, "Cave Air ESP",
            "Highlights connected cave-air clusters while filtering tiny pockets and huge air networks.");
    }

    @Override
    public void onActivate() {
        ticks = 0;
        clusters.clear();
        scan();
    }

    @Override
    public void onDeactivate() {
        clusters.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.level == null || mc.player == null) return;
        if (++ticks >= scanDelay.get()) {
            ticks = 0;
            scan();
        }
    }

    private void scan() {
        if (mc.level == null || mc.player == null) return;

        List<AirCluster> found = AirClusterScanner.scan(
            mc.level,
            mc.player.blockPosition(),
            range.get(),
            minCluster.get(),
            maxCluster.get()
        );

        synchronized (clusters) {
            clusters.clear();
            clusters.addAll(found);
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        synchronized (clusters) {
            for (AirCluster cluster : clusters) {
                for (BlockPos p : cluster.blocks()) {
                    event.renderer.box(
                        p,
                        sideColor.get(),
                        lineColor.get(),
                        shapeMode.get(),
                        0
                    );
                }
            }
        }
    }
}
