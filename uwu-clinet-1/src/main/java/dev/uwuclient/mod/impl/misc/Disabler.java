package dev.uwuclient.mod.impl.misc;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.components.BadPacketsComponent;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Disabler extends Mod{

    private final BooleanSetting sprint = new BooleanSetting("Omni-Sprint", "", true);
    private final BooleanSetting autoClicker = new BooleanSetting("Auto Clicker", "", false);
    private final BooleanSetting movement = new BooleanSetting("Strafe and Jump", "", true);
    private final BooleanSetting fastUse = new BooleanSetting("Fast Use", "", false);
    private final BooleanSetting miscellaneous = new BooleanSetting("Misc", "", true);
    private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", "", false);

    public Disabler() {
        super("Vulcan disabler", Category.Misc);
        addSetting(sprint, autoClicker, movement, fastUse, miscellaneous, keepSprint);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSendPacket){
            EventSendPacket event = ((EventSendPacket)e);
            if (miscellaneous.getValue() && event.getPacket() instanceof C17PacketCustomPayload) {
                event.setCancelled(true);
            }
        }

        if(e instanceof PreMotionEvent){
            if (sprint.getValue()) {
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
    
            if (movement.getValue() && mc.thePlayer.ticksExisted % 5 == 0) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
            }
    
            if (autoClicker.getValue() && !BadPacketsComponent.bad(false, true, true, false, false, false)) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
            }
    
            if (fastUse.getValue() && mc.thePlayer.ticksExisted % 7 == 0) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }

        }
    }
}
