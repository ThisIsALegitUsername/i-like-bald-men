package dev.uwuclient.visual.clickguis.csgogui;

import java.util.ArrayList;

import org.teavm.jso.webgl.WebGLRenderingContext;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.ClientGui;
import dev.uwuclient.visual.clickguis.csgogui.component.Comp;
import dev.uwuclient.visual.clickguis.csgogui.component.components.CsgoCheckbox;
import dev.uwuclient.visual.clickguis.csgogui.component.components.CsgoCombo;
import dev.uwuclient.visual.clickguis.csgogui.component.components.CsgoSlider;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class CsgoGui extends GuiScreen implements ClientGui{
    public double posX, posY, width2, height2, dragX, dragY;
    public boolean dragging, binding;
    public Category selectedCategory;

    private Mod selectedModule, bindingModule;

    public final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

    int heightt = 0;

    public ArrayList<Comp> comps = new ArrayList<>();

    public static int
    rapidadapta = new Color(0xFF0F0F13).brighter().getRGB(),
    rapidadaptaDark = 0xFF0D0E11,
    nord = 0xFF3B4252,
    nordDark = 0xFF2E3440,
    background = rapidadapta,
    backgroundDark = rapidadaptaDark;

    public CsgoGui() {
        dragging = false;
        posX = 70;
        posY = 70;
        width2 = posX + 150 * 2;
        height2 = height2 + 200;
        selectedCategory = Category.Combat;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (dragging) {
            posX = mouseX - dragX;
            posY = mouseY - dragY;
        }
        width2 = posX + 200 * 2;
        height2 = posY + 250;

        //background
        Gui.drawRect(0, 0, width, height, 0x84000000);

        //outline
            Gui.drawRect(posX - 2.5, posY - 18.5, width2 + 2.5, height2 + 2.5, ModManager.clickGuiMod.getColor(0));

        Gui.drawRect(posX - 2, posY - 18, width2 + 2, height2 + 2, backgroundDark);
        Gui.drawRect(posX + 252, posY, width2, height2, background);

        font.drawString("UwU", (float)posX + 25-(font.getStringWidth("UwU Client"))/2, (float)posY - 13, ModManager.clickGuiMod.getColor(0), false);
        font.drawString(selectedCategory.name(), (float)posX + 150-(font.getStringWidth(selectedCategory.name()))/2, (float)posY - 13, -1, false);
        font.drawString(selectedModule != null ? selectedModule.name : "", (float)posX + 325-(font.getStringWidth(selectedModule != null ? selectedModule.name : ""))/2, (float)posY - 13, -1, false);

        int i = 0;
        int wheel = Mouse.getDWheel();
        for(Category c : Category.values()) {
            int size = 50;

            Gui.drawRect(posX, posY + i, posX + size, posY + 10 + i + size - 10, c.equals(selectedCategory) ? ModManager.clickGuiMod.getColor(0) : background);
            Gui.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, c.name().replace(c.name().substring(1), ""), (float)posX+10, (float)(posY + i)+5, c.equals(selectedCategory) ? ModManager.clickGuiMod.getColor(0) : background);
            i+= size;
        }

        GlStateManager.pushMatrix();
        //GL11.glPushMatrix();
        //GL11.glEnable(GL11.GL_SCISSOR_TEST);
        WebGLRenderingContext ctx =  PlatformRuntime.webgl;
        ctx.enable(ctx.SCISSOR_TEST);
        glScissor(posX, posY, width2 - posX, height2 - posY);

        i = 0;
        for(Mod m : UwUClient.INSTANCE.modManager.modsInCategory(selectedCategory)) {
            if(height2-posY > posY + i + heightt){
            Gui.drawRect(posX + 52, posY + i + heightt, posX + 250, posY + i + heightt + 23, background);
            font.drawString(m.name + (m.key == 0 ? (binding && m == bindingModule ? " &7Listening..." : "") : " &7[" + (binding && m == bindingModule ? "Listening...]" : Keyboard.getKeyName(m.key) + "]")), (float)posX + 60, (float)posY + (float)heightt + 8 + i, m.isEnabled() ? ModManager.clickGuiMod.getColor(i) : -1, false);

            if(isInside(mouseX, mouseY, posX + 52, posY, posX + 250, height2)) {
                if (wheel < 0) {
                    heightt -= 1;
                } else if (wheel > 0 && heightt != 0) {
                    heightt += 1;
                }
            }
            i += 26;
            }
        }
        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY);

            if(isInside(mouseX, mouseY, posX + 252, posY, width2, height2)) {
                if (wheel < 0) {
                    comp.scrollY -= 10;
                } else if (wheel > 0 && comp.scrollY != 0) {
                    comp.scrollY += 10;
                }
            }
        }
        ctx.disable(ctx.SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

        //GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //GL11.glPopMatrix();

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);

        if(binding) {
            bindingModule.key = (keyCode == 211 ? 0 : keyCode);
            binding = false;
        }

        for (Comp comp : comps)
            comp.keyTyped(typedChar, keyCode);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY, posX, posY - 18, width2, posY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }

        int i = 0;
        for (Category category : Category.values()) {
            if (isInside(mouseX, mouseY, posX, posY + i, posX + 50, posY + 10 + i + 40) && mouseButton == 0) {
                if(selectedCategory != category)
                    heightt = 0;

                selectedCategory = category;
            }
            i += 50;
        }
        i = 0;
        for(Mod m : UwUClient.INSTANCE.modManager.modsInCategory(selectedCategory)) {
            if(isInside(mouseX, mouseY, posX + 52, posY + i + heightt, posX + 250, posY + i + heightt + 23)) {
                if(isInside(mouseX, mouseY, posX + 52, posY, posX + 250, height2)) {
                    if(mouseButton == 0 && !m.name.startsWith("ClickGUI"))
                        m.toggle();
                else if(mouseButton == 2) {
                    bindingModule = m;
                    binding = true;
                    System.out.println("nice");
                } else if(mouseButton == 1) {
                    int sOffset = 3;
                    comps.clear();
                    if (m.settings != null)
                        for (Setting setting : m.settings) {
                            selectedModule = m;

                            if (setting instanceof BooleanSetting) {
                                comps.add(new CsgoCheckbox(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }
                            if (setting instanceof ModeSetting) {
                                comps.add(new CsgoCombo(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }

                            if (setting instanceof NumberSetting) {
                                comps.add(new CsgoSlider(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }
                        }
                    }
                }
            }
            i += 26;
        }
        for (Comp comp : comps) {
            if(isInside(mouseX, mouseY, posX + 252, posY, width2, height2))
                comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;

        for (Comp comp : comps)
            comp.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void initGui() {
        super.initGui();
        dragging = false;
    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public static void setBackground(int background) {
        CsgoGui.background = background;
    }

    public static void setBackgroundDark(int backgroundDark) {
        CsgoGui.backgroundDark = backgroundDark;
    }

    private void glScissor(double x, double y, double width, double height) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        y += height;
        PlatformRuntime.webgl.scissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth()), (int) (((scaledResolution.getScaledHeight() - y) * mc.displayHeight) / scaledResolution.getScaledHeight()), (int) (width * mc.displayWidth / scaledResolution.getScaledWidth()), (int) (height * mc.displayHeight / scaledResolution.getScaledHeight()));
    }
}