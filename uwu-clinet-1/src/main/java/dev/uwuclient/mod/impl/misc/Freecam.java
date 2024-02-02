package dev.uwuclient.mod.impl.misc;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.BlockAABBEvent;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.MoveButtonEvent;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.MoveUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Freecam extends Mod{
    public Freecam(){
        super("Freecam", Category.Misc);
    }

    public final BooleanSetting fly = new BooleanSetting("Fly", this, true);
    public final NumberSetting flySpeed = new NumberSetting("Fly Speed", this, 1, 0.1f, 9.5f, 0.1f);
    public final BooleanSetting noClip = new BooleanSetting("No Clip", this, true);

    private EntityOtherPlayerMP freecamEntity;
    public static double startX, startY, startZ;
    private float startYaw, startPitch;

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            if (noClip.getValue() && fly.getValue())
                mc.thePlayer.noClip = true;
    
            if (fly.getValue()) {
                mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;
    
                if (MoveUtil.isMoving())
                    MoveUtil.strafe(flySpeed.getValue());
                else
                    MoveUtil.stop();
            }
        }

        if(e instanceof EventSendPacket){
            final Packet<?> p = ((EventSendPacket)e).getPacket();

            if (!(p instanceof C01PacketChatMessage || p instanceof C08PacketPlayerBlockPlacement || p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive || p instanceof C09PacketHeldItemChange || p instanceof C12PacketUpdateSign || p instanceof C10PacketCreativeInventoryAction || p instanceof C0EPacketClickWindow || p instanceof C0DPacketCloseWindow || p instanceof C16PacketClientStatus || p instanceof C0APacketAnimation || p instanceof C02PacketUseEntity))
                ((EventSendPacket)e).setCancelled(true);
        }

        if(e instanceof EventRecievePacket){
            final Packet<?> p = ((EventRecievePacket)e).getPacket();

            if (p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 1)
                ((EventRecievePacket)e).setCancelled(true);
        }

        if(e instanceof MoveButtonEvent){
            if (fly.getValue())
            ((MoveButtonEvent)e).setSneak(false);
        }

        if(e instanceof BlockAABBEvent){
            if (noClip.getValue() && fly.getValue()){
                ((BlockAABBEvent)e).setCollisionBoundingBox(null);
                ((BlockAABBEvent)e).setBoundingBox(null);
            }
        }
    }

    @Override
    public void onEnable() {
        freecamEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        freecamEntity.setSprinting(mc.thePlayer.isSprinting());
        freecamEntity.setInvisible(mc.thePlayer.isInvisible());
        freecamEntity.setSneaking(mc.thePlayer.isSneaking());

        mc.theWorld.addEntityToWorld(freecamEntity.getEntityId(), freecamEntity);

        startPitch = mc.thePlayer.rotationPitch;
        startYaw = mc.thePlayer.rotationYaw;
        startX = mc.thePlayer.posX;
        startY = mc.thePlayer.posY;
        startZ = mc.thePlayer.posZ;
    }

    @Override
    public void onDisable() {
        if (freecamEntity != null) {
            mc.theWorld.removeEntityFromWorld(freecamEntity.getEntityId());
            mc.thePlayer.setPositionAndRotation(startX, startY, startZ, startYaw, startPitch);
        }
        mc.thePlayer.noClip = false;
        mc.thePlayer.motionY = 0;
        MoveUtil.strafe(0);
    }
    
}
