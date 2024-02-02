package dev.uwuclient.visual.clickguis.ascgui.buttons;

import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;

public class AstolfoBooleanButton extends AstolfoButton {
    public BooleanSetting setting;
    public Color color;

    public AstolfoBooleanButton(float x, float y, float width, float height, BooleanSetting set, Color col) {
        super(x, y, width, height);
        setting = set;
        color = col;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);
        if(setting.getValue()) Gui.drawRect(x + 3, y, x + width - 3, y + height, color.getRGB());
        RenderUtils.drawCenteredStringVert(setting.name, x + 4, y + height/2 - 0.5f, 0xffffffff);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if(isHovered(mouseX, mouseY) && click) {
            setting.setValue(!setting.getValue());
        }
    }
}
