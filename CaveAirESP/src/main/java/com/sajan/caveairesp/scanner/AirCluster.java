package com.sajan.caveairesp.scanner;

import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AirCluster {
    private final Set<BlockPos> blocks;
    private final int minX, minY, minZ, maxX, maxY, maxZ;

    public AirCluster(Set<BlockPos> blocks) {
        this.blocks = Collections.unmodifiableSet(new HashSet<>(blocks));

        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, z1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE, z2 = Integer.MIN_VALUE;

        for (BlockPos p : blocks) {
            x1 = Math.min(x1, p.getX()); y1 = Math.min(y1, p.getY()); z1 = Math.min(z1, p.getZ());
            x2 = Math.max(x2, p.getX()); y2 = Math.max(y2, p.getY()); z2 = Math.max(z2, p.getZ());
        }
        minX=x1; minY=y1; minZ=z1; maxX=x2; maxY=y2; maxZ=z2;
    }

    public Set<BlockPos> blocks() { return blocks; }
    public int size() { return blocks.size(); }
    public int width() { return maxX-minX+1; }
    public int height() { return maxY-minY+1; }
    public int depth() { return maxZ-minZ+1; }
    public BlockPos min() { return new BlockPos(minX,minY,minZ); }
    public BlockPos max() { return new BlockPos(maxX,maxY,maxZ); }
}
