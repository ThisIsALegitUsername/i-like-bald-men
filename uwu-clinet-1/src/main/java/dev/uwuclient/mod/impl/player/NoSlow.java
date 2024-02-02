package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PostMotionEvent;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.impl.combat.Aura;
import dev.uwuclient.util.PacketUtil;
import dev.uwuclient.util.TimeUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class NoSlow extends Mod {

    public NoSlow(){
        super("NoSlow", Category.Player, 0);
        addSetting(mode);
    }

    private final ModeSetting mode = new ModeSetting("Mode", "Grim", "Vanilla", "NCP", "Reverse NCP", "Hypixel", "Delay", "Grim");

    //EntityPlayerSP 783

    private boolean aBoolean, blocking, intaveFunnyBoolean;
    private final TimeUtil timer = new TimeUtil();
    private long delay;
    private int ticks;

    @Override
    public void onEnable() {
        ticks = 0;
        blocking = false;
    }


    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            switch (mode.getValue()) {
                case "NCP": {
                    if (mc.thePlayer.isBlocking()) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    break;
                }
    
                case "Delay": {
                    if (!mc.thePlayer.isBlocking()) aBoolean = false;
    
                    if (mc.thePlayer.isBlocking() && mc.thePlayer.ticksExisted % 5 == 0 && aBoolean) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    
                        aBoolean = false;
                    }
    
                    if (mc.thePlayer.isBlocking() && mc.thePlayer.ticksExisted % 5 == 1 && !aBoolean) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
    
                        aBoolean = true;
                    }
                    break;
                }
    
                case "Reverse NCP": {
                    if (mc.thePlayer.isBlocking()) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    }
    
                    break;
                }
    
                case "Grim":
                    if (mc.thePlayer.isBlocking() && timer.hasReached(delay)) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    break;
            }
        }
        if(e instanceof PostMotionEvent){
            switch (mode.getValue()) {
                case "NCP": {
                    if (mc.thePlayer.isBlocking()) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    }
                    break;
                }
    
                case "Hypixel": {
                    if (mc.thePlayer.isUsingItem() && Aura.target == null)
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    break;
                }
    
                case "Reverse NCP": {
                    if (mc.thePlayer.isBlocking()) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
    
                    break;
                }
    
                case "Grim":
                    if (mc.thePlayer.isBlocking() && timer.hasReached(delay)) {
                        mc.playerController.syncCurrentPlayItem();
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        delay = 200;
                        if (intaveFunnyBoolean) {
                            delay = 100;
                            intaveFunnyBoolean = false;
                        } else
                            intaveFunnyBoolean = true;
                        timer.reset();
                    }
                    break;
            }
        }
    }
}
