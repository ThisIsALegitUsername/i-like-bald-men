package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event{
    public Packet<?> packet;
    public EventSendPacket(Packet<?> packet){
        this.packet = packet;
    }
    public Packet<?> getPacket() {
        return packet;
    }
    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
