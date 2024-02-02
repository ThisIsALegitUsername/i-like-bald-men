package dev.uwuclient.mod.components;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.UpdateEvent;
import dev.uwuclient.mod.base.Component;
import dev.uwuclient.util.PacketUtil;
import dev.uwuclient.util.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class PingSpoofComponent extends Component{
    public static final Queue<PacketUtil.TimedPacket> incomingPackets = new LinkedList<>();
    public static final Queue<PacketUtil.TimedPacket> outgoingPackets = new LinkedList<>();
    public static StopWatch stopWatch = new StopWatch();
    public static boolean spoofing;
    public static int delay;
    public static boolean normal, teleport, velocity, world, entity, client = true;

    @Override
    public void onEvent(Event e) {
        Minecraft mc = Minecraft.getMinecraft();
        if(e instanceof UpdateEvent){
            
            for (Iterator<PacketUtil.TimedPacket> itr = incomingPackets.iterator(); itr.hasNext(); ) {
                PacketUtil.TimedPacket packet = itr.next();

                if (System.currentTimeMillis() > packet.getTime() + (PingSpoofComponent.spoofing ? PingSpoofComponent.delay : 0)) {
                    try {
                        PacketUtil.receivePacketNoEvent(packet.getPacket());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    itr.remove();
                        //PingSpoofComponent.incomingPackets.remove(packet);
                }
            }
    
            for (Iterator<PacketUtil.TimedPacket> itr = outgoingPackets.iterator(); itr.hasNext(); ) {
                PacketUtil.TimedPacket packet = itr.next();

                if (System.currentTimeMillis() > packet.getTime() + (PingSpoofComponent.spoofing ? PingSpoofComponent.delay : 0)) {
                    try {
                        PacketUtil.sendPacketWithoutEvent(packet.getPacket());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    
                    itr.remove();
                    //PingSpoofComponent.outgoingPackets.remove(packet);
                }
            }
    
            if (stopWatch.finished(60) || mc.thePlayer.ticksExisted <= 20 || !mc.getNetHandler().doneLoadingTerrain) {
                PingSpoofComponent.spoofing = false;
    
                if(PingSpoofComponent.incomingPackets != null && !PingSpoofComponent.incomingPackets.isEmpty())
                for (Iterator<PacketUtil.TimedPacket> itr = incomingPackets.iterator(); itr.hasNext(); ) {
                    PacketUtil.TimedPacket packet = itr.next();

                    PacketUtil.receivePacketNoEvent(packet.getPacket());
                    itr.remove();
                    //PingSpoofComponent.incomingPackets.remove(packet);
                }
    
                for (Iterator<PacketUtil.TimedPacket> itr = outgoingPackets.iterator(); itr.hasNext(); ) {
                    PacketUtil.TimedPacket packet = itr.next();
                    
                    PacketUtil.sendPacketWithoutEvent(packet.getPacket());
                    itr.remove();
                    //PingSpoofComponent.outgoingPackets.remove(packet);
                }
            }
        }

        if(e instanceof EventSendPacket){
            if (!PingSpoofComponent.client || !PingSpoofComponent.spoofing) return;

            final Packet<?> packet = ((EventSendPacket)e).getPacket();
    
            if (packet instanceof C03PacketPlayer || packet instanceof C16PacketClientStatus ||
                    packet instanceof C0DPacketCloseWindow || packet instanceof C0EPacketClickWindow ||
                    packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity ||
                    packet instanceof C0APacketAnimation || packet instanceof C09PacketHeldItemChange ||
                    packet instanceof C18PacketSpectate || packet instanceof C19PacketResourcePackStatus ||
                    packet instanceof C17PacketCustomPayload || packet instanceof C15PacketClientSettings ||
                    packet instanceof C14PacketTabComplete || packet instanceof C07PacketPlayerDigging ||
                    packet instanceof C08PacketPlayerBlockPlacement) {
                outgoingPackets.add(new PacketUtil.TimedPacket(packet, System.currentTimeMillis()));
                ((EventSendPacket)e).setCancelled(true);
            }
        }

        if(e instanceof EventRecievePacket){

        final Packet<?> packet = ((EventRecievePacket)e).getPacket();

        if (PingSpoofComponent.spoofing && mc.getNetHandler().doneLoadingTerrain) {
            if (((packet instanceof S32PacketConfirmTransaction || packet instanceof S00PacketKeepAlive) && normal) ||

                    ((packet instanceof S08PacketPlayerPosLook || packet instanceof S09PacketHeldItemChange) && teleport) ||

                    (((packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == mc.thePlayer.getEntityId()) ||
                            packet instanceof S27PacketExplosion) && velocity) ||

                    ((packet instanceof S26PacketMapChunkBulk || packet instanceof S21PacketChunkData ||
                            packet instanceof S23PacketBlockChange || packet instanceof S22PacketMultiBlockChange) && world) ||

                    ((packet instanceof S13PacketDestroyEntities || packet instanceof S14PacketEntity ||
                            packet instanceof S18PacketEntityTeleport ||
                            packet instanceof S20PacketEntityProperties || packet instanceof S19PacketEntityHeadLook) && entity)) {

                incomingPackets.add(new PacketUtil.TimedPacket(packet, System.currentTimeMillis()));
                ((EventRecievePacket)e).setCancelled(true);
            }
        }
        }
    }

    public static void joinServer(){
        incomingPackets.clear();
        stopWatch.reset();
        PingSpoofComponent.spoofing = false;
    }

    public static void dispatch() {
        for (Iterator<PacketUtil.TimedPacket> itr = incomingPackets.iterator(); itr.hasNext(); ) {
            PacketUtil.TimedPacket packet = itr.next();
            try {
                PacketUtil.receivePacketNoEvent(packet.getPacket());
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            itr.remove();
            //PingSpoofComponent.incomingPackets.remove(packet);
        }

        for (Iterator<PacketUtil.TimedPacket> itr = outgoingPackets.iterator(); itr.hasNext(); ) {
            PacketUtil.TimedPacket packet = itr.next();

            PacketUtil.sendPacketWithoutEvent(packet.getPacket());

            itr.remove();
            //PingSpoofComponent.outgoingPackets.remove(packet);
        }
    }

    public static void setSpoofing(final int delay, final boolean normal, final boolean teleport,
                                   final boolean velocity, final boolean world, final boolean entity) {
        PingSpoofComponent.spoofing = true;
        PingSpoofComponent.delay = delay;
        PingSpoofComponent.normal = normal;
        PingSpoofComponent.teleport = teleport;
        PingSpoofComponent.velocity = velocity;
        PingSpoofComponent.world = world;
        PingSpoofComponent.entity = entity;
        PingSpoofComponent.client = false;

        stopWatch.reset();
    }

    public static void setSpoofing(final int delay, final boolean normal, final boolean teleport,
                                   final boolean velocity, final boolean world, final boolean entity, final boolean client) {
        PingSpoofComponent.spoofing = true;
        PingSpoofComponent.delay = delay;
        PingSpoofComponent.normal = normal;
        PingSpoofComponent.teleport = teleport;
        PingSpoofComponent.velocity = velocity;
        PingSpoofComponent.world = world;
        PingSpoofComponent.entity = entity;
        PingSpoofComponent.client = client;

        stopWatch.reset();
    }
}
