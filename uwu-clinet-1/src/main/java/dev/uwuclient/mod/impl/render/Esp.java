package dev.uwuclient.mod.impl.render;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRender3d;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.impl.misc.AntiBot;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Esp extends Mod{
    public Esp(){
        super("Esp", Category.Render, 0);
    }

    public void onEvent(Event e){
        if(e instanceof EventRender3d){
        if(this.isEnabled()){
        for(Object o : mc.theWorld.loadedEntityList){
            if(o instanceof EntityPlayer && !AntiBot.bots.contains(o) && !o.equals(mc.thePlayer)){
                entityESPBox(((Entity)o), 0);
            }
        }
    }
}
    }

        public void entityESPBox(Entity entity, int mode) {
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            EaglercraftGPU.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            if(mode == 0) //Enemy
                GlStateManager.color(
                        1 - (float)mc.thePlayer.getDistanceSqToEntity(entity) / 40,
                        (float)mc.thePlayer.getDistanceSqToEntity(entity) / 40,
                        0, 0.5F);
                        
            
            else if(mode == 1)//friend
                GlStateManager.color(0, 0, 1, 0.5F);
            else if(mode == 2)//Other
                GlStateManager.color(1, 1, 0, 0.5F);
            else if(mode == 3)// Target
                GlStateManager.color(1, 0, 0, 0.5F);
            else if(mode == 4)//Team
                GlStateManager.color(0, 1, 0, 0.5F);
            RenderGlobal.func_181561_a(
                    new AxisAlignedBB(
                            entity.getEntityBoundingBox().minX
                                -0.05
                                - entity.posX
                                + (entity.posX -mc.getRenderManager().renderPosX),
                            entity.getEntityBoundingBox().minY
                                -0.05
                                - entity.posY
                                + (entity.posY -mc.getRenderManager().renderPosY),
                            entity.getEntityBoundingBox().minZ
                                -0.05
                                - entity.posZ
                                + (entity.posZ -mc.getRenderManager().renderPosZ),
                            entity.getEntityBoundingBox().maxX
                                +0.05
                                - entity.posX
                                + (entity.posX -mc.getRenderManager().renderPosX),
                            entity.getEntityBoundingBox().maxY
                                +0.1
                                - entity.posY
                                + (entity.posY -mc.getRenderManager().renderPosY),
                            entity.getEntityBoundingBox().maxZ
                                +0.05
                                - entity.posZ
                                + (entity.posZ -mc.getRenderManager().renderPosZ)));
            
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }
    
}
