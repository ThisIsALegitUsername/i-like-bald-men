package dev.uwuclient.visual.clickguis.csgogui.component;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.visual.clickguis.csgogui.CsgoGui;

public class Comp {
    public double x, y, scrollY;
    public CsgoGui parent;
    public Mod module;
    public Setting setting;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int state) {}
    public void drawScreen(int mouseX, int mouseY) {}
    public void keyTyped(char typedChar, int keyCode) {}

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }


}
