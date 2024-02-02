package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

/*stfu */
@SuppressWarnings("all")
public class Velocity extends Mod{

    public ModeSetting mode = new ModeSetting("Mode", "Custom", "Custom", "Grim");

    @Override
    public void onUpdateAlwaysGUI(){
        if(mode.getValue() == "Grim"){
            horizontal.setValue(0);
            vertical.setValue(0);
        }
    }

    private final NumberSetting horizontal = new NumberSetting("Horizontal", "", 100.0f, 0.0f, 100.0f, 0.1f);
    private final NumberSetting vertical = new NumberSetting("Vertical", "", 100.0f, 0.0f, 100.0F, 0.1f);
    private final NumberSetting timer = new NumberSetting("Timer", "", 1, 0.1f, 2, 0.1f);
    private final NumberSetting timerTicks = new NumberSetting("Timer Ticks", "", 1, 0, 9, 0.1f);
    private int ticks;

    public Velocity(){
        super("Velocity", Category.Combat);
        addSetting(mode, horizontal, vertical, timer, timerTicks);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRecievePacket){
            EventRecievePacket event = ((EventRecievePacket)e);
            final Packet<?> p = ((EventRecievePacket)e).getPacket();
            if(p instanceof S12PacketEntityVelocity){
                final S12PacketEntityVelocity veloPacket = (S12PacketEntityVelocity) p;

                if(veloPacket.getEntityID() == mc.thePlayer.getEntityId()){
                    switch (mode.getValue()) {
                        case "Custom": {
                            final double horizontal = this.horizontal.getValue();
                            final double vertical = this.vertical.getValue();
    
                            if (horizontal == 0.0 && vertical == 0.0) {
                                event.setCancelled(true);
                                return;
                            }
    
                            veloPacket.motionX *= horizontal / 100.0;
                            veloPacket.motionY *= vertical / 100.0;
                            veloPacket.motionZ *= horizontal / 100.0;
    
                            event.setPacket(veloPacket);
                            break;
                        }
    
                        case "Grim": {
                            final double horizontal = this.horizontal.getValue();
                            final double vertical = this.vertical.getValue();
    
                            if (horizontal == 0.0 && vertical == 0.0) {
                                event.setCancelled(true);
                                return;
                            }
                            break;
                        }
    
                }
            }
        }
    }
    if(e instanceof EventSendPacket){
        Packet p = ((EventSendPacket)e).getPacket();
        switch (mode.getValue()) {
            case "Grim":
                if (mc.thePlayer.hurtTime > 0 && p instanceof C0FPacketConfirmTransaction) {
                    ((EventSendPacket)e).setCancelled(true);
                }
            break;
        }
    }
    if(e instanceof PreMotionEvent){
        if (mc.thePlayer.hurtTime == 9) {
            ticks = 0;
        }

        if (ticks < timerTicks.getValue()) {
            mc.timer.timerSpeed = (float) timer.getValue();
        }

        if (ticks == (int) timerTicks.getValue() || mc.thePlayer == null || mc.thePlayer.ticksExisted < 3) {
            mc.timer.timerSpeed = 1;
        }
    }
}
    
}
