package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.network.Packet;

/*stfu */
@SuppressWarnings("all")
public class EventRecievePacket extends Event{
    public Packet packet;
    public EventRecievePacket(Packet packet){
        this.packet = packet;
    }
    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
