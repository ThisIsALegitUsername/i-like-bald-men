package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.mod.base.Mod;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class InventorySync extends Mod{
    public InventorySync(){
        super("InventorySync", Category.Player, 0);
    }

    public short action;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRecievePacket){
            final Packet<?> packet = ((EventRecievePacket)e).getPacket();

            if (packet instanceof S32PacketConfirmTransaction) {
                final S32PacketConfirmTransaction wrapper = (S32PacketConfirmTransaction) packet;
                final Container inventory = mc.thePlayer.inventoryContainer;
    
                if (wrapper.getWindowId() == inventory.windowId) {
                    this.action = wrapper.getActionNumber();
    
                    if (this.action > 0 && this.action < inventory.transactionID) {
                        inventory.transactionID = (short) (this.action + 1);
                    }
                }
            }
        }
    }
    
}
