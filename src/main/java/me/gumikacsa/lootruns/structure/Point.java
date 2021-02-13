package me.gumikacsa.lootruns.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Point {

    public float x, y, z;

    @Deprecated
    public Point() {}

    public Vec3d toVector() {
        return new Vec3d(x, y, z);
    }

    public BlockPos toBlockPosition() {
        return new BlockPos(x, y, z);
    }

}
