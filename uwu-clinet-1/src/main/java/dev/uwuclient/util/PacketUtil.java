package dev.uwuclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
    public static void sendPacket(final Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }

    public static void sendPacketWithoutEvent(final Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueueWithoutEvent(packet);
    }

    public static void receivePacket(final Packet<?> pkt){
        Minecraft.getMinecraft().getNetHandler().addToReceiveQueue(pkt);
    }

    public static void receivePacketNoEvent(final Packet<?> pkt){
        Minecraft.getMinecraft().getNetHandler().addToReceiveQueueWithoutEvent(pkt);
    }

    public static class TimedPacket {
        private final Packet<?> packet;
        private final long time;

        public TimedPacket(final Packet<?> packet, final long time) {
            this.packet = packet;
            this.time = time;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public long getTime() {
            return time;
        }
    }

}
