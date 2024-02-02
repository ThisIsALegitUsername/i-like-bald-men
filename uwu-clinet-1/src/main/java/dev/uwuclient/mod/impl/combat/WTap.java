package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventAttack;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class WTap extends Mod{
    public WTap(){
        super("WTap", Category.Combat);
        addSetting(legit);
    }
    
    private final BooleanSetting legit = new BooleanSetting("Legit", "", false);
    public static int ticks;
    private boolean sprinting;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventAttack){
            ticks = 0;
        }

        if(e instanceof EventSendPacket){
            final Packet<?> packet = ((EventSendPacket)e).getPacket();

            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction wrapper = (C0BPacketEntityAction) packet;
    
                switch (wrapper.getAction()) {
                    case START_SPRINTING:
                        sprinting = true;
                        break;
    
                    case STOP_SPRINTING:
                        sprinting = false;
                        break;
                }
            }
        }

        if(e instanceof PreMotionEvent){
            ++ticks;

            if (mc.thePlayer.isSprinting()) {
                if (legit.getValue()) {
                    if (ticks == 2) mc.thePlayer.setSprinting(false);
                    if (ticks == 3) mc.thePlayer.setSprinting(true);
                } else {
                    switch (ticks) {
                        case 1:
                            if (sprinting) {
                                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            } else {
                                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            }
                            break;
            
                        case 2:
                            if (!sprinting) {
                                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            }
                            break;
                    }
                }
            }
        }
    }
}
