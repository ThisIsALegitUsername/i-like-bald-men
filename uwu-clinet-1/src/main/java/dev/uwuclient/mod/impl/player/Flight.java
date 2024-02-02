package dev.uwuclient.mod.impl.player;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.BlockAABBEvent;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.EventStrafe;
import dev.uwuclient.event.impl.MoveButtonEvent;
import dev.uwuclient.event.impl.MoveEvent;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.event.impl.UpdateEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.components.BadPacketsComponent;
import dev.uwuclient.util.MoveUtil;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Flight extends Mod{

    private int ticks, vticks;

    public Flight(){
        super("Flight", Category.Player);
    }

    public final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Vulcan", "Verus", "Buffer abuse"){
        @Override
        public void onChange() {
            MoveUtil.stop();

            switch(this.getValue()){
                case "Vulcan":{
                    ticks = 0;
                    PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ,
                            mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                }

            }
        }
    };
    private final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.1f, 9.5f, 0.1f);
    private final BooleanSetting sendFlying = new BooleanSetting("Send Flying", this, false);

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventStrafe){
            EventStrafe event = (EventStrafe)e;

            switch(mode.getValue()){
                case "Buffer abuse": {
                    final float speed = this.speed.getValue();

                    event.setSpeed(speed);
                }

                case "Vulcan": {
                    ((EventStrafe)e).setSpeed(1);
                }
            }
        }
        if(e instanceof PreMotionEvent){
            PreMotionEvent event = (PreMotionEvent)e;

            switch(mode.getValue()){
                case "Buffer abuse": {
                    final float speed = this.speed.getValue();

                    mc.thePlayer.motionY = -1E-10D
                            + (mc.gameSettings.keyBindJump.isKeyDown() ? speed : 0.0D)
                            - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed : 0.0D);
            
                    if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY, mc.thePlayer.lastReportedPosZ) <= 10 - speed - 0.15) {
                        event.setCancelled(true);
                    }
                }

                case "Vulcan": {
                    final float speed = this.speed.getValue();

                    mc.thePlayer.motionY = -1E-10D
                            + (mc.gameSettings.keyBindJump.isKeyDown() ? speed : 0.0D)
                            - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed : 0.0D);
            
                    if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY, mc.thePlayer.lastReportedPosZ) <= 10 - speed - 0.15) {
                        event.setCancelled(true);
                    } else {
                        ticks++;
            
                        if (ticks >= 8) {
                            MoveUtil.stop();
                            this.toggle();
                            UwUClient.INSTANCE.notificationManager.registerNotification("Flight was disabled.");
                        }
                    }
                }

                case "Verus": {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            mc.thePlayer.motionY = 0.42F;
                        }
                    }
            
                    ++vticks;
                }
            }
        }
        if(e instanceof MoveButtonEvent){
            switch(mode.getValue()){
                case "Buffer abuse": 
                    ((MoveButtonEvent)e).setSneak(false);
                    break;

                case "Verus":
                    ((MoveButtonEvent)e).setSneak(false);
                    break;
            }
        }
        if(e instanceof EventSendPacket){
            EventSendPacket event = (EventSendPacket)e;
            switch(mode.getValue()){
                case "Buffer abuse": {
                    if (!sendFlying.getValue()) {
                        Packet<?> packet = event.getPacket();
            
                        if (packet instanceof C03PacketPlayer) {
                            C03PacketPlayer c03PacketPlayer = ((C03PacketPlayer) packet);
            
                            if (!c03PacketPlayer.isMoving() && !BadPacketsComponent.bad()) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

        if(e instanceof BlockAABBEvent){
            BlockAABBEvent event = (BlockAABBEvent)e;
            if(mode.is("Verus")){
                if (event.getBlock() instanceof BlockAir && !mc.gameSettings.keyBindSneak.isKeyDown() || mc.gameSettings.keyBindJump.isKeyDown()) {
                    final double x = event.getBlockPos().getX(), y = event.getBlockPos().getY(), z = event.getBlockPos().getZ();

                    if (y < mc.thePlayer.posY) {
                        event.setBoundingBox(AxisAlignedBB.fromBounds(
                                -15,
                                -1,
                                -15,
                                15,
                                1,
                                15
                        ).offset(x, y, z));
                    }
                }
            }
        }

        if(e instanceof MoveEvent){
            if(mode.is("Verus")){
                if (mc.thePlayer.onGround && vticks % 14 == 0) {
                    ((MoveEvent)e).setPosY(0.42F);
                    MoveUtil.strafe(0.69);
                    mc.thePlayer.motionY = -(mc.thePlayer.posY - Math.floor(mc.thePlayer.posY));
                } else {
                    // A Slight Speed Boost.
                    if (mc.thePlayer.onGround) {
                        MoveUtil.strafe(1.01 + MoveUtil.speedPotionAmp(0.15));
                        // Slows Down To Not Flag Speed11A.
                    } else MoveUtil.strafe(0.41 + MoveUtil.speedPotionAmp(0.05));
                }
    
                mc.thePlayer.setSprinting(true);
                mc.thePlayer.omniSprint = true;
            }
    
            vticks++;
        }

        if(e instanceof UpdateEvent){
            if(mode.is("Vanilla")){
                mc.thePlayer.capabilities.isFlying = true;
                mc.thePlayer.capabilities.flySpeed = speed.getValue();
            }
        }
    }
    
}
