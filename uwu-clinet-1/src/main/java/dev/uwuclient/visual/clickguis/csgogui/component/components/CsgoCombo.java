package dev.uwuclient.visual.clickguis.csgogui.component.components;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.visual.clickguis.csgogui.CsgoGui;
import dev.uwuclient.visual.clickguis.csgogui.component.Comp;

public class CsgoCombo extends Comp {
    public CsgoCombo(double x, double y, CsgoGui parent, Mod module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        parent.font.drawString(setting.name, (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6 + scrollY), -1);
        parent.font.drawString(((ModeSetting)setting).getValue(), (int)(parent.posX + x + 120) - parent.font.getStringWidth(((ModeSetting)setting).getValue()), (int)(parent.posY + y + 6) + (float)scrollY, -1, true);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY)) {
            if (mouseButton == 0) {
                ((ModeSetting)setting).cycle(true);
            } else if (mouseButton == 1) {
                ((ModeSetting)setting).cycle(false);
            }
        }
    }
}
