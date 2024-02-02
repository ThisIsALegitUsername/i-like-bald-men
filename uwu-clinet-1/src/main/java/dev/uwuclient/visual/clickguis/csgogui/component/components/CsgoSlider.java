package dev.uwuclient.visual.clickguis.csgogui.component.components;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.visual.clickguis.csgogui.CsgoGui;
import dev.uwuclient.visual.clickguis.csgogui.component.Comp;
import net.minecraft.client.gui.Gui;

public class CsgoSlider extends Comp {
    private boolean dragging = false;

    private double renderWidth;

    public CsgoSlider(double x, double y, CsgoGui parent, Mod module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        Gui.drawRect((int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY, CsgoGui.backgroundDark);
        Gui.drawRect((int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x - 20) + renderWidth, (int)(parent.posY + y + 20) + scrollY, ModManager.clickGuiMod.getColor((long)y));
        parent.font.drawString(setting.name, (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6) + (float)scrollY, -1, false);
        parent.font.drawString(String.valueOf(((NumberSetting)setting).getValue()), (int)(parent.posX + x + 120) - parent.font.getStringWidth(String.valueOf(((NumberSetting)setting).getValue())), (int)(parent.posY + y + 6) + (float)scrollY, -1, false);

        double
        diff = Math.min(142, Math.max(0, mouseX - (parent.posX + x) + 20)),
        min = ((NumberSetting)setting).getMin(),
        max = ((NumberSetting)setting).getMax();

        renderWidth = (142) * (((NumberSetting)setting).getValue() - min) / (max - min);

        if (dragging) {
            if (diff == 0)
                ((NumberSetting)setting).setValue(((NumberSetting)setting).getMin());

            else {
                double newValue = roundToPlace(((diff / 142) * (max - min) + min));
                ((NumberSetting)setting).setValue((float)newValue);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY) && mouseButton == 0)
            dragging = true;

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
