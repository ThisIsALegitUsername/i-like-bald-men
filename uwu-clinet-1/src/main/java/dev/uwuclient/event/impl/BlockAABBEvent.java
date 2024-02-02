package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAABBEvent extends Event{

    public BlockAABBEvent(AxisAlignedBB stuff, World world, Block block, BlockPos blockPos, AxisAlignedBB boundingBox,
            AxisAlignedBB maskBoundingBox) {
                this.collisionBoundingBox = stuff;
        this.world = world;
        this.block = block;
        this.blockPos = blockPos;
        this.boundingBox = boundingBox;
        this.maskBoundingBox = maskBoundingBox;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return collisionBoundingBox;
    }
    public void setCollisionBoundingBox(AxisAlignedBB collisionBoundingBox) {
        this.collisionBoundingBox = collisionBoundingBox;
    }

    private AxisAlignedBB collisionBoundingBox;
    private final World world;
    private final Block block;
    private final BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    private final AxisAlignedBB maskBoundingBox;
    public World getWorld() {
        return world;
    }
    public Block getBlock() {
        return block;
    }
    public BlockPos getBlockPos() {
        return blockPos;
    }
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }
    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
    public AxisAlignedBB getMaskBoundingBox() {
        return maskBoundingBox;
    }
    
}
