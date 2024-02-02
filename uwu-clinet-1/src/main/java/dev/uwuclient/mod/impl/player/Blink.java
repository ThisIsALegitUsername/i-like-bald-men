package dev.uwuclient.mod.impl.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public final class Blink extends Mod {

    public Blink(){
        super("Blink", Category.Combat);
        addSetting(allPackets, showPlayer, pulse, delayPulse);
    }

    private final BooleanSetting allPackets = new BooleanSetting("All Packets", "", true);
    private final BooleanSetting showPlayer = new BooleanSetting("Show Player", "", false);
    private final Queue<Packet<?>> packets = new LinkedList<>();

    private final BooleanSetting pulse = new BooleanSetting("Pulse", "", true);
    private final NumberSetting delayPulse = new NumberSetting("Pulse Delay", "", 39, 4, 100, 0.1f);

    private EntityOtherPlayerMP blinkEntity;

    List<Vec3> path = new ArrayList<>();

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSendPacket){
            EventSendPacket event= ((EventSendPacket)e);
            if (mc.thePlayer == null || mc.thePlayer.isDead || mc.isSingleplayer() || mc.thePlayer.ticksExisted < 50) {
                packets.clear();
                return;
            }
    
            if (allPackets.getValue()) {
                packets.add(event.getPacket());
                event.setCancelled(true);
            } else {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    packets.add(event.getPacket());
                    event.setCancelled(true);
                }
            }
    
            if (pulse.getValue()) {
                if (!packets.isEmpty() && mc.thePlayer.ticksExisted % (int) delayPulse.getValue() == 0 && Math.random() > 0.1) {
                    packets.forEach(PacketUtil::sendPacketWithoutEvent);
                    packets.clear();
                }
            }
        }

        if(e instanceof PreMotionEvent){
            if (mc.thePlayer.ticksExisted < 50) return;

            if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
                path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
            }
    
            if (pulse.getValue()) {
                while (path.size() > (int) delayPulse.getValue()) {
                    path.remove(0);
                }
            }
    
            if (pulse.getValue() && blinkEntity != null) {
                mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());
            }
        }
    }

    @Override
    public void onEnable() {
        path.clear();

        if (!pulse.getValue() && showPlayer.getValue()) {
            blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            blinkEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            blinkEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
            blinkEntity.setSprinting(mc.thePlayer.isSprinting());
            blinkEntity.setInvisible(mc.thePlayer.isInvisible());
            blinkEntity.setSneaking(mc.thePlayer.isSneaking());

            mc.theWorld.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
        }
    }

    @Override
    public void onDisable() {
        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();

        if (showPlayer.getValue()) {
            if (blinkEntity != null) {
                mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());
            }
        }
    }
}
