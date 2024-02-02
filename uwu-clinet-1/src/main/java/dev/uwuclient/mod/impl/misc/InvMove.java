package dev.uwuclient.mod.impl.misc;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.PacketUtil;
import dev.uwuclient.visual.clickguis.lemon.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class InvMove extends Mod{

    public InvMove(){
        super("Inventory move", Category.Misc);
    }

    private final NumberSetting slowdown = new NumberSetting("Slow Down", this, 1, 0.1f, 1, 0.1f);
    private final BooleanSetting packet = new BooleanSetting("Packet", this, false);

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            if (Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                mc.gameSettings.keyBindForward.setKeyPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
                mc.gameSettings.keyBindBack.setKeyPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindBack));
                mc.gameSettings.keyBindRight.setKeyPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindRight));
                mc.gameSettings.keyBindLeft.setKeyPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindLeft));
                mc.gameSettings.keyBindJump.setKeyPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindJump));
    
                if (Minecraft.getMinecraft().currentScreen != null) {
                    final double s = slowdown.getValue();
    
                    if (!(Minecraft.getMinecraft().currentScreen instanceof ClickGui)) {
                        Minecraft.getMinecraft().thePlayer.motionX *= s;
                        Minecraft.getMinecraft().thePlayer.motionZ *= s;
                    }
                }
            }
        }
        if(e instanceof EventSendPacket){
            EventSendPacket event = (EventSendPacket)e;
            final Packet<?> p = event.getPacket();

            if (packet.getValue()) {
                if (p instanceof C0DPacketCloseWindow)
                    event.setCancelled(true);
    
                if (p instanceof C0EPacketClickWindow) {
                    event.setCancelled(true);
    
                    PacketUtil.sendPacketWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    PacketUtil.sendPacketWithoutEvent(event.getPacket());
                    PacketUtil.sendPacketWithoutEvent(new C0DPacketCloseWindow(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId));
                }
    
                if (p instanceof C16PacketClientStatus) {
                    final C16PacketClientStatus packetClientStatus = (C16PacketClientStatus) event.getPacket();
                    if (packetClientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                        event.setCancelled(true);
                }
            }
        }
    }
    
}
