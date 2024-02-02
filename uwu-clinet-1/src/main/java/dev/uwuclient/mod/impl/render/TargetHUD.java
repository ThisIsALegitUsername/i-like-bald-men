package dev.uwuclient.mod.impl.render;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRender2d;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.impl.combat.Aura;
import dev.uwuclient.util.Color;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TargetHUD extends Mod{

    public ModeSetting hudMode = new ModeSetting("Mode", this, "Flat", "Simple", "Fancy", "Flat");
    public float lastHealth = 0;
    public boolean nulltarget = false;
    public double healthBarWidth, healthBarWidth2, hudHeight;

    public TargetHUD(){
        super("TargetHUD", Category.Render);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender2d){
            ScaledResolution sr = new ScaledResolution(mc);
            if (this.hudMode.is("Simple")) {
                if (Aura.target != null) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    mc.fontRendererObj.drawStringWithShadow(Aura.target.getName(), sr.getScaledWidth() / 2f - (mc.fontRendererObj.getStringWidth(Aura.target.getName().replaceAll("\247.", "")) / 2f), sr.getScaledHeight() / 2f - 33, 0xffffffff);
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));

                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.depthMask(false);
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.color(1, 1, 1);
                    int i = 0;
                    while (i < Aura.target.getMaxHealth() / 2.0f) {
                        mc.ingameGUI.drawTexturedModalRect(
                                (sr.getScaledWidth() / 2) - Aura.target.getMaxHealth() / 2.0f * 9.5f / 2.0f + (i * 10),
                                (sr.getScaledHeight() / 2 - 20), 16, 0, 9, 9);
                        ++i;
                    }

                    i = 0;
                    while (i < Aura.target.getHealth() / 2.0f) {
                        mc.ingameGUI.drawTexturedModalRect(
                                (sr.getScaledWidth() / 2) - Aura.target.getMaxHealth() / 2.0f * 9.5f / 2.0f + (i * 10),
                                (sr.getScaledHeight() / 2 - 20), 52, 0, 9, 9);
                        ++i;
                    }

                    GlStateManager.depthMask(true);
                    GlStateManager.disableBlend();
                    GlStateManager.enableDepth();

                    GlStateManager.disableBlend();
                    GlStateManager.color(1, 1, 1);
                    RenderHelper.disableStandardItemLighting();
                }
            }

            if (hudMode.is("Fancy")) {
                if (Aura.target != null) {
                    int width = (sr.getScaledWidth() / 2) + 100;
                    int height = sr.getScaledHeight() / 2;

                    EntityLivingBase player = Aura.target;
                    Gui.drawRect(width - 70, height + 30, width + 80, height + 105, new Color(0, 0, 0, 140).getRGB());

                    mc.fontRendererObj.drawString(player.getName() + "             " /* + (Criticals.isReadyToCritical ? "Critical " : " ")*/, width - 65, height + 35, -1);
                    mc.fontRendererObj.drawString(player.onGround ? "On Ground" : "No Ground", width - 65, height + 50, -1);
                    mc.fontRendererObj.drawString("HP: " + player.getHealth(), width - 65 + mc.fontRendererObj.getStringWidth("off Ground") + 13, height + 50, -1);
                    mc.fontRendererObj.drawString("Distance: " + mc.thePlayer.getDistanceToEntity(player), width - 65, height + 60, -1);

                    mc.fontRendererObj.drawString(player.getHealth() > mc.thePlayer.getHealth() ? "Lower Health" : "Higher Health", width - 65, height + 80, player.getHealth() > mc.thePlayer.getHealth() ? Color.RED.getRGB() : Color.GREEN.brighter().getRGB());
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GlStateManager.scale(1.0f, 1.0f, 1.0f);
                    mc.getRenderItem().renderItemAndEffectIntoGUI(player.getHeldItem(), width + 50, height + 80);
                    GlStateManager.popMatrix();

                    float health = player.getHealth();
                    float healthPercentage = (health / player.getMaxHealth());
                    float targetHealthPercentage = 0;
                    if (healthPercentage != lastHealth) {
                        float diff = healthPercentage - this.lastHealth;
                        targetHealthPercentage = this.lastHealth;
                        this.lastHealth += diff / 8;
                    }
                    Color healthcolor = Color.WHITE;
                    if (healthPercentage * 100 > 75) {
                        healthcolor = Color.GREEN;
                    } else if (healthPercentage * 100 > 50 && healthPercentage * 100 < 75) {
                        healthcolor = Color.YELLOW;
                    } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                        healthcolor = Color.ORANGE;
                    } else if (healthPercentage * 100 < 25) {
                        healthcolor = Color.RED;
                    }
                    Gui.drawRect(width - 70, height + 104, (int) (width - 70 + (149 * targetHealthPercentage)), height + 106, healthcolor.getRGB());
                    Gui.drawRect(width - 70, height + 104, (int) (width - 70 + (149 * healthPercentage)), height + 106, Color.GREEN.getRGB());
                    GlStateManager.resetColor();
                    GuiInventory.drawEntityOnScreen(width + 60, height + 75, 20, Mouse.getX(), Mouse.getY(), player);
                }
            }

            if (hudMode.is("Flat")) {
                //Distance TH code by Mymylesaws
                int blackcolor = new Color(0, 0, 0, 180).getRGB();
                int blackcolor2 = new Color(200, 200, 200, 160).getRGB();
                ScaledResolution sr2 = new ScaledResolution(mc);
                float scaledWidth = sr2.getScaledWidth();
                float scaledHeight = sr2.getScaledHeight();

                nulltarget = Aura.target == null;

                float x = scaledWidth / 2.0f - 50;
                float y = scaledHeight / 2.0f + 32;
                float health;
                double hpPercentage;
                Color hurt;
                int healthColor;
                String healthStr;
                if (nulltarget) {
                    health = 0;
                    hpPercentage = health / 20;
                    hurt = Color.getHSBColor(300f / 360f, ((float) 0 / 10f) * 0.37f, 1f);
                    healthStr = String.valueOf((float) 0 / 2.0f);
                    healthColor = Color.pink.getRGB();
                    //healthColor = getHealthColor(0, 20).getRGB();
                } else {
                    health = Aura.target.getHealth();
                    hpPercentage = health / Aura.target.getMaxHealth();
                    hurt = Color.getHSBColor(310f / 360f, ((float) Aura.target.hurtTime / 10f), 1f);
                    healthStr = String.valueOf((float) (int) (Aura.target.getHealth()) / 2.0f);
                    healthColor = Color.pink.getRGB();
                    //healthColor = getHealthColor(Aura.target.getHealth(), Aura.target.getMaxHealth()).getRGB();
                }
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                double hpWidth = 140.0 * hpPercentage;

                if (nulltarget) {
                    this.healthBarWidth2 = getAnimationStateSmooth(0, this.healthBarWidth2, 6f / Minecraft.getDebugFPS());
                    this.healthBarWidth = getAnimationStateSmooth(0, this.healthBarWidth, 14f / Minecraft.getDebugFPS());

                    this.hudHeight = getAnimationStateSmooth(0.0, this.hudHeight, 8f / Minecraft.getDebugFPS());
                } else {
                    this.healthBarWidth2 = moveUD((float) this.healthBarWidth2, (float) hpWidth, 6f / Minecraft.getDebugFPS(), 3f / Minecraft.getDebugFPS());
                    this.healthBarWidth = getAnimationStateSmooth(hpWidth, this.healthBarWidth, 14f / Minecraft.getDebugFPS());

                    this.hudHeight = getAnimationStateSmooth(40.0, this.hudHeight, 8f / Minecraft.getDebugFPS());
                }

                if (hudHeight == 0) {
                    this.healthBarWidth2 = 140;
                    this.healthBarWidth = 140;
                }

                //Gui.prepareScissorBox(x, (float) ((double) y + 40 - hudHeight), x + 140.0f, (float) ((double) y + 40));
                Gui.drawRect(x, y, x + 140.0f, y + 40.0f, blackcolor);
                Gui.drawRect(x, y + 37.0f, (x) + 140, y + 40f, new Color(0, 0, 0, 49).getRGB());

                Gui.drawRect(x, y + 37.0f, (float) (x + this.healthBarWidth2), y + 40.0f, new Color(255, 0, 213, 220).getRGB());
                Gui.drawRect(x, y + 37.0f, (x + this.healthBarWidth), y + 40.0f, new Color(0, 81, 179).getRGB());

                mc.fontRendererObj.drawStringWithShadow(healthStr, x + 40.0f + 85.0f - (float) mc.fontRendererObj.getStringWidth(healthStr) / 2.0f + mc.fontRendererObj.getStringWidth("\u2764") / 1.9f, y + 23, blackcolor2);
                mc.fontRendererObj.drawStringWithShadow("\u2764", x + 40.0f + 85.0f - (float) mc.fontRendererObj.getStringWidth(healthStr) / 2.0f - mc.fontRendererObj.getStringWidth("\u2764") / 1.9f, y + 23, hurt.getRGB());

                if (nulltarget) {
                    mc.fontRendererObj.drawStringWithShadow("XYZ:" + 0 + " " + 0 + " " + 0, x + 37f, y + 15f, -1);
                    mc.fontRendererObj.drawStringWithShadow("No target", x + 36.0f, y + 5.0f, -1);
                } else {
                    mc.fontRendererObj.drawStringWithShadow("XYZ:" + (int) Aura.target.posX + " " + (int) Aura.target.posY + " " + (int) Aura.target.posZ, x + 37f, y + 15f, -1);

                    if ((Aura.target instanceof EntityPlayer)) {
                        mc.fontRendererObj.drawStringWithShadow("Blocking:" + " " + (((EntityPlayer) Aura.target).isBlocking() ? "True" : "False"), x + 37f, y + 25f, -1);
                    }

                    mc.fontRendererObj.drawStringWithShadow(Aura.target.getName(), x + 36f, y + 4.0f, -1);

                    if ((Aura.target instanceof EntityPlayer)) {
                        GlStateManager.resetColor();
                        mc.getTextureManager().bindTexture(((AbstractClientPlayer) Aura.target).getLocationSkin());

                        GlStateManager.color(1, 1, 1);
                        Gui.drawScaledCustomSizeModalRect((int) x + 3, (int) y + 3, 8.0F, 8.0F, 8, 8, 32, 32, 64, 64);
                    }
                }
                
            }
        }
    }

    public static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            if (current + factor > target) {
                current = target;
            } else {
                current += factor;
            }
        } else {
            if (current - factor < target) {
                current = target;
            } else {
                current -= factor;
            }
        }
        return current;
    }

    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }
    
}
