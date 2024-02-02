package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockDamageEvent extends Event {
    private EntityPlayerSP player;
    private World world;
    private BlockPos blockPos;

    public BlockDamageEvent(final EntityPlayerSP player, final World world, final BlockPos blockPos) {
        this.player = player;
        this.world = world;
        this.blockPos = blockPos;
    }

    public EntityPlayerSP getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayerSP player) {
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}