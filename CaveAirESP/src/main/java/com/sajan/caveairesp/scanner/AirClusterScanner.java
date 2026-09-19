package com.sajan.caveairesp.scanner;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public final class AirClusterScanner {
    private AirClusterScanner() {}

    public static List<AirCluster> scan(ClientLevel world, BlockPos center, int radius, int minSize, int maxSize) {
        List<AirCluster> result = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();

        int r = Math.max(4, radius);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = center.getX()-r; x <= center.getX()+r; x++) {
            for (int y = Math.max(world.getMinY(), center.getY()-r); y <= Math.min(world.getMaxY()-1, center.getY()+r); y++) {
                for (int z = center.getZ()-r; z <= center.getZ()+r; z++) {
                    cursor.set(x,y,z);
                    if (visited.contains(cursor) || world.getBlockState(cursor).getBlock() != Blocks.CAVE_AIR) continue;

                    AirCluster cluster = flood(world, cursor.immutable(), r, visited);
                    if (cluster.size() >= minSize && cluster.size() <= maxSize) result.add(cluster);
                }
            }
        }
        return result;
    }

    private static AirCluster flood(ClientLevel world, BlockPos start, int radius, Set<BlockPos> visited) {
        Set<BlockPos> cluster = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            BlockPos p = queue.removeFirst();
            cluster.add(p);

            if (cluster.size() > 4096) break; // safety cap for huge open areas

            for (BlockPos n : neighbors(p)) {
                if (Math.abs(n.getX()-start.getX()) > radius ||
                    Math.abs(n.getY()-start.getY()) > radius ||
                    Math.abs(n.getZ()-start.getZ()) > radius) continue;

                if (!visited.contains(n) && world.getBlockState(n).getBlock() == Blocks.CAVE_AIR) {
                    visited.add(n);
                    queue.addLast(n);
                }
            }
        }
        return new AirCluster(cluster);
    }

    private static List<BlockPos> neighbors(BlockPos p) {
        return List.of(
            p.above(), p.below(), p.north(), p.south(), p.east(), p.west()
        );
    }
}
