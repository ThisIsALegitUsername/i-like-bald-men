package dev.uwuclient.mod.impl.misc;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRot extends Mod{
    public NoRot(){
        super("NoRot", Category.Misc, 0);
        addSetting(mode);
    }

    public final ModeSetting mode = new ModeSetting("Mode", "Edit", "Vanilla", "Packet", "Delay", "Legit", "Edit", "Hypixel");
    private Float yaw, pitch;
    private Float startingYaw, startingPitch;

    public void onEvent(Event e){
        if(e instanceof EventRecievePacket){
            EventRecievePacket event = ((EventRecievePacket)e);

            final Packet<?> packet = event.getPacket();

        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook teleport = (S08PacketPlayerPosLook) packet;

            switch (mode.getValue()) {
                case "Vanilla":
                    yaw = teleport.getYaw();
                    pitch = teleport.getPitch();

                    teleport.setYaw(mc.thePlayer.rotationYaw);
                    teleport.setPitch(mc.thePlayer.rotationPitch);
                    break;

                case "Delay":
                    yaw = mc.thePlayer.rotationYaw;
                    pitch = mc.thePlayer.rotationPitch;
                    break;

                case "Legit":
                    yaw = teleport.getYaw();
                    pitch = teleport.getPitch();
                    startingYaw = mc.thePlayer.rotationYaw;
                    startingPitch = mc.thePlayer.rotationPitch;

                    teleport.setYaw(mc.thePlayer.rotationYaw);
                    teleport.setPitch(mc.thePlayer.rotationPitch);
                    break;
            }
        }
        }

        if(e instanceof PreMotionEvent){
            PreMotionEvent event = ((PreMotionEvent)e);
            
            switch (mode.getValue()) {
                case "Delay":
                    if (yaw != null && pitch != null) {
                        mc.thePlayer.rotationYaw = yaw;
                        mc.thePlayer.rotationPitch = pitch;
    
                        yaw = null;
                        pitch = null;
                    }
                    break;
    
                case "Legit":
                    if (yaw != null && pitch != null && startingYaw != null && startingPitch != null) {
                        if (startingYaw == event.getYaw() && startingPitch == event.getPitch()) {
                            event.setYaw(yaw);
                            event.setPitch(pitch);
                        } else {
                            startingPitch = startingYaw = yaw = pitch = null;
                        }
                    }
                    break;
            }
        }
    }
    
}
