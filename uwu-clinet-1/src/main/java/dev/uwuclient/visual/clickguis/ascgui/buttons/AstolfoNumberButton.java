package dev.uwuclient.visual.clickguis.ascgui.buttons;

import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class AstolfoNumberButton extends AstolfoButton {
    public NumberSetting setting;
    public Color color;

    public boolean dragged;

    public AstolfoNumberButton(float x, float y, float width, float height, NumberSetting set, Color col) {
        super(x, y, width, height);

        color = col;
        setting = set;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        double diff = setting.max - setting.min;

        double percentWidth = (setting.getValue() - setting.min) / (setting.max - setting.min);

        if (dragged) {
            double val = setting.min + (MathHelper.clamp_double((double) (mouseX - x) / width, 0, 1)) * diff;
            setting.setValue(Math.round(val * 100F)/ 100F);
        }

        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);
        Gui.drawRect(x, y, (x+percentWidth*width)-3, y + height, color.getRGB());
        RenderUtils.drawCenteredStringVert(setting.name + ": " + Math.round(setting.getValue() * 100D)/ 100D, x + 4, y + height / 2 - 0.5f, 0xffffffff);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (isHovered(mouseX, mouseY)) {
            dragged = true;
        }

        if(!click) dragged = false;
    }
}
