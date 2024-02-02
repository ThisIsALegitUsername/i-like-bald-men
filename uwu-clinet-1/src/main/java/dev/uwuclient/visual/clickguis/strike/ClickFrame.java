package dev.uwuclient.visual.clickguis.strike;

import java.util.ArrayList;
import java.util.List;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ClickFrame {

    private static FontRenderer big;
    private static FontRenderer norm;

    public static float entryWidth = 132;

    public static float entryHeight = 22;

    public static int maximumEntries = 15;

    public static float scale = 0.8F;

    private final Category category;

    private final List<Mod> modules;

    private float frameX, frameY, mouseX, mouseY, dragX, dragY;

    private float scrollVertical, lastScrollVertical;

    private float previousHeight;

    private boolean expanded, dragged;

    private boolean leftClick, rightClick;

    private final List<Mod> expandedModuleIndices;

    private int customHue;

    private Color background, backgroundDarker, backgroundDarkest, accent, accentDarker, accentDarkest, shadow;


    private void scissor(double x, double y, double width, double height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        y += height;
        PlatformRuntime.webgl.scissor((int) ((x * Minecraft.getMinecraft().displayWidth) / scaledResolution.getScaledWidth()), (int) (((scaledResolution.getScaledHeight() - y) * Minecraft.getMinecraft().displayHeight) / scaledResolution.getScaledHeight()), (int) (width * Minecraft.getMinecraft().displayWidth / scaledResolution.getScaledWidth()), (int) (height * Minecraft.getMinecraft().displayHeight / scaledResolution.getScaledHeight()));
    }

    public ClickFrame(final Category category, final float frameX, final float frameY) {
        this.modules = UwUClient.INSTANCE.modManager.modsInCategory(category);
        this.expandedModuleIndices = new ArrayList<>();
        this.norm = Minecraft.getMinecraft().fontRendererObj;

        this.category = category;
        this.frameX = frameX;
        this.frameY = frameY;
    }

    public void draw(final StrikeGUI parent, final int mouseX, final int mouseY) {
        scale = 1;
        entryWidth = 132 * scale;
        entryHeight = 22 * scale;

        accent = new Color(65, 68, 217);
        accentDarker = new Color(46, 48, 153);
        accentDarkest = new Color(34, 35, 115);
        background = new Color(38, 35, 41);
        backgroundDarker = new Color(27, 24, 30);
        backgroundDarkest = new Color(15, 12, 18);

        customHue = 330;

        shadow = new Color(backgroundDarker.getRed(), backgroundDarker.getGreen(), backgroundDarker.getBlue(), 100);

        /*
         * Scrolling
         */
        if (mouseX >= frameX + StrikeGUI.scrollHorizontal && mouseX <= frameX + StrikeGUI.scrollHorizontal + entryWidth && !GuiInventory.isCtrlKeyDown()) {
            final float partialTicks = Minecraft.getMinecraft() == null || Minecraft.getMinecraft().timer == null ? 1.0F : Minecraft.getMinecraft().timer.renderPartialTicks;

            final float lastLastScrollHorizontal = lastScrollVertical;
            lastScrollVertical = scrollVertical;
            final float wheel = Mouse.getDWheel();
            scrollVertical += wheel / 10.0F;
            if (wheel == 0) scrollVertical -= (lastLastScrollHorizontal - scrollVertical) * 0.6 * partialTicks;

            final float minScroll = maximumEntries * entryHeight - previousHeight;
            if (scrollVertical < minScroll) scrollVertical = minScroll;
            if (scrollVertical > 0.0F) scrollVertical = 0.0F;
        }

        /*
         * Now the ClickGUI itself
         */
        final float textOffsetBig = 9 / 2.0F;
        final float textOffsetNormal = 9 / 2.0F;

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (dragged) {
            this.frameX += this.mouseX - this.dragX;
            this.frameY += this.mouseY - this.dragY;
            this.dragX = this.mouseX;
            this.dragY = this.mouseY;
        }

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
        final float frameY = this.frameY;

        // Draw the top bar
        RenderUtils.drawRect(frameX, frameY, entryWidth, entryHeight, backgroundDarker.getRGB());
        Gui.drawCenteredString(Minecraft.getMinecraft().fontRendererObj,
                category.name(),
                frameX + entryWidth / 2.0F, frameY + entryHeight / 2.0F - textOffsetBig, Color.WHITE.getRGB()
        );

        if (expanded) {
            float currentY = frameY;

            PlatformRuntime.webgl.enable(WebGL2RenderingContext.SCISSOR_TEST);
            scissor(frameX, currentY + entryHeight, entryWidth, maximumEntries * entryHeight);

                for (final Mod module : modules) {
                    currentY += entryHeight;

                    float moduleY = currentY + scrollVertical;

                    final Color backgroundColor = module.isEnabled()
                            ? getAccentColor(frameX, moduleY, entryWidth, entryHeight)
                            : getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, false);

                    RenderUtils.drawRect(frameX, moduleY, entryWidth, entryHeight, backgroundColor.getRGB());

                    float titleY = moduleY + entryHeight / 2.0F - textOffsetNormal;
                    norm.drawString(module.name, frameX + 4.0F, titleY, Color.WHITE.getRGB(), false);

                    // Draw the settings
                    final List<Setting> settings = module.settings;
                    if (settings.size() > 0) {
                        final boolean settingsExpanded = expandedModuleIndices.contains(module);
                        norm.drawString(settingsExpanded ? "-" : "+", frameX + entryWidth - 8.0F, titleY, Color.WHITE.getRGB(), false);

                        if (settingsExpanded) {
                            boolean isFirst = true;
                            for (final Setting setting : settings) {
                                if (setting.hidden) continue;

                                moduleY += entryHeight;
                                titleY += entryHeight;

                                if (setting instanceof BooleanSetting) {
                                    final Color settingColor = ((BooleanSetting) setting).getValue()
                                            ? getAccentColor(frameX, moduleY, entryWidth, entryHeight)
                                            : getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true);
                                    RenderUtils.drawRect(frameX, moduleY, entryWidth, entryHeight, settingColor.getRGB());

                                    norm.drawString(setting.name, frameX + 8.0F, titleY, Color.WHITE.getRGB(), false);
                                } else if (setting instanceof ModeSetting) {
                                    RenderUtils.drawRect(frameX, moduleY, entryWidth, entryHeight, getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true).getRGB());

                                    final ModeSetting arraySetting = (ModeSetting) setting;
                                    norm.drawString(arraySetting.name + ": " + arraySetting.getValue(), frameX + 8.0F, titleY, Color.WHITE.getRGB(), false);
                                } else if (setting instanceof NumberSetting) {
                                    final NumberSetting numberSetting = (NumberSetting) setting;

                                    final double fromZeroValue = numberSetting.getValue() - Math.abs(numberSetting.getMin());
                                    final float normalizedValue = (float) (fromZeroValue / (float) (numberSetting.getMax() - numberSetting.getMin()));
                                    final float screenSpaceValue = normalizedValue * entryWidth;

                                    RenderUtils.drawRect(frameX, moduleY, screenSpaceValue, entryHeight, getAccentColor(frameX, moduleY, entryWidth, entryHeight).getRGB());
                                    final float backgroundWidth = entryWidth - screenSpaceValue;

                                    if (backgroundWidth > 0.0F) {
                                        final Color color = getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true);
                                        RenderUtils.drawRect(frameX + screenSpaceValue, moduleY, backgroundWidth, entryHeight, color.getRGB());
                                    }

                                    norm.drawString(
                                            numberSetting.name + ": " + getTruncatedDouble(numberSetting),
                                            frameX + 8.0F, titleY, Color.WHITE.getRGB(), false
                                    );
                                }

                                if (isFirst) {
                                    isFirst = false;

                                    //gradient
                                    RenderUtils.drawRect(frameX, moduleY, entryWidth, 10, shadow.getRGB());
                                }
                            }

                            //gradient
                            RenderUtils.drawRect(frameX, moduleY + entryHeight - 10, entryWidth, 10, shadow.getRGB());
                        }
                    }

                    currentY = moduleY - scrollVertical;
                }

            previousHeight = currentY - frameY;
            PlatformRuntime.webgl.disable(PlatformRuntime.webgl.SCISSOR_TEST);
        }
    }

    public void drawDescriptions(final int mouseX, final int mouseY, final float partialTicks) {
        if (expanded) {
            final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
            float currentY = frameY;

            if (mouseY < currentY + entryHeight || mouseY > currentY + entryHeight + maximumEntries * entryHeight)
                return;
                for (final Mod module : modules) {

                    currentY += entryHeight;
                    final float moduleY = currentY + scrollVertical;

                    if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                        final String description = module.description;
                        final float descriptionWidth = norm.getStringWidth(description);
                        RenderUtils.drawRect(mouseX + 4, mouseY - 6 - entryHeight / 2.0F, descriptionWidth + 6.0F, entryHeight / 2.0F + 2.0F, backgroundDarker.getRGB());
                        RenderUtils.drawRect(mouseX + 5, mouseY - 5 - entryHeight / 2.0F, descriptionWidth + 4.0F, entryHeight / 2.0F, background.getRGB());
                        norm.drawString(description, mouseX + 7, mouseY - 5 - entryHeight / 2.0F + 2.0F, Color.WHITE.getRGB(), false);
                    }

                    final List<Setting> settings = module.settings;
                    if (settings.size() > 0) {
                        final boolean settingsExpanded = expandedModuleIndices.contains(module);
                        if (settingsExpanded) {
                            for (final Setting setting : settings) {
                                if (setting.hidden) continue;
                                currentY += entryHeight;
                            }
                        }
                    }
                }
            }
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        switch (mouseButton) {
            case 0: {
                leftClick = true;
                break;
            }
            case 1: {
                rightClick = true;
                break;
            }
        }

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;

        if (isHovering(frameX, frameY, entryWidth, entryHeight)) {
            if (leftClick) {
                dragged = true;
                dragX = mouseX;
                dragY = mouseY;
            }
        }
    }

    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;

        if (leftClick) dragged = false;
        else if (isHovering(frameX, frameY, entryWidth, entryHeight)) expanded = !expanded;

        if (expanded) {
            float currentY = frameY;{
                float moduleY = currentY + scrollVertical;

                for (final Mod module : modules) {

                    moduleY += entryHeight;

                    if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                        switch (mouseButton) {
                            case 0:
                                module.toggle();
                                break;
                            case 1: {
                                if (expandedModuleIndices.contains(module)) expandedModuleIndices.remove(module);
                                else expandedModuleIndices.add(module);
                                break;
                            }
                        }
                    }

                    if (expandedModuleIndices.contains(module)) {
                        for (final Setting setting : module.settings) {
                            if (setting.hidden) continue;

                            moduleY += entryHeight;

                            if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                                if (setting instanceof BooleanSetting) {
                                    ((BooleanSetting) setting).toggle();
                                } else if (setting instanceof ModeSetting) {
                                    final ModeSetting modeSetting = (ModeSetting) setting;
                                    switch (mouseButton) {
                                        case 0: {
                                            modeSetting.cycle(true);
                                            break;
                                        }
                                        case 1: {
                                            modeSetting.cycle(false);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    currentY = moduleY - scrollVertical;
                }
            }
        }

        switch (mouseButton) {
            case 0: {
                leftClick = false;
                break;
            }
            case 1: {
                rightClick = false;
                break;
            }
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int mouseButton, final long timeSinceLastClick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (expanded) {
            final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
            float currentY = frameY + scrollVertical;

            for (final Mod module : modules) {
                //if (module.isHidden()) continue;

                currentY += entryHeight;

                if (expandedModuleIndices.contains(module)) {
                    for (final Setting setting : module.settings) {
                        if (setting.hidden) continue;

                        currentY += entryHeight;

                        if (isHovering(frameX, currentY, entryWidth, entryHeight)) {
                            if (setting instanceof NumberSetting) {
                                final NumberSetting numberSetting = (NumberSetting) setting;

                                final float mouseRelative = mouseX - frameX;
                                final float mouseNormalized = mouseRelative / entryWidth;
                                final double range = numberSetting.getMax() - numberSetting.getMin();
                                final double value = (mouseNormalized * range) + numberSetting.getMin();

                                if (numberSetting.getIncrement() != 0)
                                    numberSetting.value = (float)round(value, (float) numberSetting.increment);
                                else numberSetting.value = (float)value;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    private boolean isHovering(final float x, final float y, final float width, final float height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private Color getBackgroundColor(final float x, final float y, final float width, final float height, final boolean setting) {
        Color color = isHovering(x, y, width, height) ? ((leftClick || rightClick) ? backgroundDarkest : backgroundDarker) : background;
        if (setting) color = color.darker();
        return color;
    }

    private Color getAccentColor(final float x, final float y, final float width, final float height) {
        return isHovering(x, y, width, height) ? ((leftClick || rightClick) ? accentDarkest : accentDarker) : accent;
    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

    public static Color changeHue(Color c, final float hue) {

        // Get saturation and brightness.
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);

        // Pass .5 (= 180 degrees) as HUE
        c = new Color(Color.HSBtoRGB(hue, hsbVals[1], hsbVals[2]));

        return c;
    }

    private static String getTruncatedDouble(final NumberSetting setting) {
        String str = String.valueOf((float) round(setting.value, (float) setting.increment));

        if (setting.increment == 1) {
            str = str.replace(".0", "");
        }

        /*if (setting.getReplacements() != null) {
            for (final String replacement : setting.getReplacements()) {
                final String[] split = replacement.split("-");
                str = str.replace(split[0], split[1]);
            }
        }*/

        return str;
    }
}
