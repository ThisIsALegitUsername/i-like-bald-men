package dev.uwuclient.visual.clickguis.ascgui.buttons;

import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;

public class AstolfoModeButton extends AstolfoButton {
    public ModeSetting setting;
    public Color color;

    public AstolfoModeButton(float x, float y, float width, float height, ModeSetting set, Color col) {
        super(x, y, width, height);
        setting = set;
        color = col;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);
        RenderUtils.drawCenteredStringVert(setting.name + " > " + setting.getValue(), x + 4, y + height/2 - 0.5f, 0xffffffff);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if(isHovered(mouseX, mouseY) && click) {
            if(button == 0) setting.cycle(true); else setting.cycle(false);
        }
    }
}
