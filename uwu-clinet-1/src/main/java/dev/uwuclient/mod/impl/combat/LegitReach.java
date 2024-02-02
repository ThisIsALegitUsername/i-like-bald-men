package dev.uwuclient.mod.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventAttack;
import dev.uwuclient.event.impl.EventRecievePacket;
import dev.uwuclient.event.impl.EventRender3d;
import dev.uwuclient.event.impl.UpdateEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.components.PingSpoofComponent;
import dev.uwuclient.mod.impl.misc.AntiBot;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.PacketUtil;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.Vec3;

public class LegitReach extends Mod{
    public LegitReach(){
        super("Legit Reach", Category.Combat);
        addSetting(maxPingSpoof, preventRepeatedAttacks, delayVelocity, delayBlockUpdates);
    }
    
    public NumberSetting maxPingSpoof = new NumberSetting("Max Ping Spoof", "", 1000, 50, 10000, 1);
    public BooleanSetting preventRepeatedAttacks = new BooleanSetting("Prevent repeated attacks", "", true);
    public BooleanSetting delayVelocity = new BooleanSetting("Delay Velocity", "", true);
    public BooleanSetting delayBlockUpdates = new BooleanSetting("Delay Block Updates", "", true);

    private Vec3 realTargetPosition = new Vec3(0, 0, 0);
    public Entity targetEntity;
    public boolean isActive, editedPackets;

    @Override
    public void onEvent(Event e) {
        if(e instanceof UpdateEvent){
        List<Entity> targets = getTargets(9);

        if (targets.isEmpty()) {
            isActive = true;
            targetEntity = null;
            return;
        }

        targetEntity = targets.get(0);

        if (targetEntity == null || mc.thePlayer.isSwingInProgress) {
            return;
        }

        PingSpoofComponent.setSpoofing((int)maxPingSpoof.getValue(), true, true, delayVelocity.getValue(), delayBlockUpdates.getValue(), true);

        if (isActive) {
            realTargetPosition = new Vec3(targetEntity.posX, targetEntity.posY, targetEntity.posZ);
            isActive = false;
        }

        while ((targetEntity.getDistanceToEntity(mc.thePlayer) > 3 ||
                realTargetPosition.distanceTo(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) > 6) &&
                !PingSpoofComponent.incomingPackets.isEmpty()) {

            PacketUtil.TimedPacket packet = PingSpoofComponent.incomingPackets.poll();

            if (packet == null) break;

            PacketUtil.receivePacketNoEvent(packet.getPacket());
        }
        }

        if(e instanceof EventRecievePacket){
            EventRecievePacket event = ((EventRecievePacket)e);
            final Packet<?> packet = event.getPacket();

            if (targetEntity == null) {
                return;
            }
    
            if (packet instanceof S14PacketEntity) {
                S14PacketEntity s14PacketEntity = ((S14PacketEntity) packet);
    
                if (s14PacketEntity.entityId == targetEntity.getEntityId()) {
                    realTargetPosition.xCoord += s14PacketEntity.getPosX() / 32D;
                    realTargetPosition.yCoord += s14PacketEntity.getPosY() / 32D;
                    realTargetPosition.zCoord += s14PacketEntity.getPosZ() / 32D;
                }
            } else if (packet instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport s18PacketEntityTeleport = ((S18PacketEntityTeleport) packet);
    
                if (s18PacketEntityTeleport.getEntityId() == targetEntity.getEntityId()) {
                    realTargetPosition = new Vec3(s18PacketEntityTeleport.getX() / 32D, s18PacketEntityTeleport.getY() / 32D, s18PacketEntityTeleport.getZ() / 32D);
                }
            }
        }

        if(e instanceof EventRender3d){

            if (targetEntity == null) {
                return;
            }
    
            if (realTargetPosition.squareDistanceTo(new Vec3(targetEntity.posX, targetEntity.posY, targetEntity.posZ)) > 0.1) {
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
    
                double expand = 0.14;
                Color c = new Color(12, 232, 199);
                GlStateManager.color(c.getRed()/255, c.getGreen()/255, c.getRed()/255, 1);
    
                RenderGlobal.func_181561_a(mc.thePlayer.getEntityBoundingBox().offset(-mc.thePlayer.posX, -mc.thePlayer.posY, -mc.thePlayer.posZ).
                        offset(realTargetPosition.xCoord, realTargetPosition.yCoord, realTargetPosition.zCoord).expand(expand, expand, expand));
    
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.popMatrix();
                GlStateManager.resetColor();
            }
        }

        if(e instanceof EventAttack){
            if (preventRepeatedAttacks.getValue()) editedPackets = true;
        }
    }

    private List<Entity> getTargets(int range) {

        return mc.theWorld.loadedEntityList.stream()
        .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) < range)
        .filter(entity -> mc.theWorld.loadedEntityList.contains(entity))
        .filter(entity -> !AntiBot.bots.contains(entity))
        .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))
        .collect(Collectors.toList());
    }
}
