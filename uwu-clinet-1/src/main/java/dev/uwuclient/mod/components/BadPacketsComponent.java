package dev.uwuclient.mod.components;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.mod.base.Component;
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
import net.minecraft.network.play.client.C16PacketClientStatus;

public class BadPacketsComponent extends Component{

// WHY USUNTINSTTIOSTRIPHFWEPIHOPHJOIWPIJEOFJPIOFSDPJIPJOISD
// I SIT NOT WOKRING WHY IS IT IOSG O FLAGIGNG WHY AM I EVENI DOING THIS
// PLEASE FUCKIHUIOEPHWFOQWDPJIOEFDQWOCNHBXNHBDSV
// 
    private static boolean slot, attack, swing, block, inventory, sprint;

    public static boolean bad() {
        return bad(true, true, true, true, true, true);
    }

    public static boolean bad(final boolean slot, final boolean attack, final boolean swing, final boolean block, final boolean sprint, final boolean inventory) {
        return (BadPacketsComponent.slot && slot) ||
                (BadPacketsComponent.attack && attack) ||
                (BadPacketsComponent.swing && swing) ||
                (BadPacketsComponent.block && block) ||
                (BadPacketsComponent.sprint && sprint) ||
                (BadPacketsComponent.inventory && inventory);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSendPacket){

        final Packet<?> packet = ((EventSendPacket)e).getPacket();

            if (packet instanceof C09PacketHeldItemChange) {
                slot = true;
            } else if (packet instanceof C0APacketAnimation) {
                swing = true;
            } else if (packet instanceof C02PacketUseEntity) {
                attack = true;
            } else if (packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging) {
                block = true;
            } else if (packet instanceof C0EPacketClickWindow ||
                    (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus) packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) ||
                    packet instanceof C0DPacketCloseWindow) {
                inventory = true;
            } else if(packet instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)packet).getAction() == C0BPacketEntityAction.Action.START_SPRINTING){
            sprint = true;  
            } else if (packet instanceof C03PacketPlayer) {
                reset();
            }
    }
}

    public static void reset() {
        slot = false;
        swing = false;
        attack = false;
        block = false;
        inventory = false;
        sprint = false;
    }
    
}
