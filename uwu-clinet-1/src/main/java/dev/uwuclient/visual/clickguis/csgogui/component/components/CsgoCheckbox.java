package dev.uwuclient.visual.clickguis.csgogui.component.components;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.visual.clickguis.csgogui.CsgoGui;
import dev.uwuclient.visual.clickguis.csgogui.component.Comp;
import net.minecraft.client.gui.Gui;

public class CsgoCheckbox extends Comp {

    public CsgoCheckbox(double x, double y, CsgoGui parent, Mod module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        parent.font.drawString(setting.name, (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6) + (float)scrollY, -1, true);

        Gui.drawRect((int)(parent.posX + x + 107), (int)(parent.posY + y + 4) + scrollY, (int)(parent.posX + x + 120), (int)(parent.posY + y + 17) + scrollY, CsgoGui.backgroundDark);

        if(((BooleanSetting)setting).getValue())
            Gui.drawRect((int)(parent.posX + x + 109), (int)(parent.posY + y + 6) + scrollY, (int)(parent.posX + x + 118), (int)(parent.posY + y + 15) + scrollY, ModManager.clickGuiMod.getColor((long)y));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY) && mouseButton == 0)
            ((BooleanSetting)setting).toggle();
    }
}
