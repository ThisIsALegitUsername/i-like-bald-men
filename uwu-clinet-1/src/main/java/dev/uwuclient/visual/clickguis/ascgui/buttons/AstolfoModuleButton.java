package dev.uwuclient.visual.clickguis.ascgui.buttons;

import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;

public class AstolfoModuleButton extends AstolfoButton {
    public Mod module;
    public Color color;

    public boolean extended;

    public float finalHeight;

    public ArrayList<AstolfoButton> astolfoButtons = new ArrayList<>();

    public AstolfoModuleButton(float x, float y, float width, float height, Mod mod, Color col) {
        super(x, y, width, height);

        module = mod;

        color = col;

        final float startY = y + height;

        int count = 0;

        for(Setting set : module.settings) {
            if(set instanceof BooleanSetting) astolfoButtons.add(new AstolfoBooleanButton(x, startY + 18*count, width, 18, (BooleanSetting)set, color));
            if(set instanceof ModeSetting) astolfoButtons.add(new AstolfoModeButton(x, startY + 18*count, width, 18, (ModeSetting)set, color));
            if(set instanceof NumberSetting) astolfoButtons.add(new AstolfoNumberButton(x, startY + 18*count, width, 18, (NumberSetting)set, color));
            count++;
        }
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);

        if(!extended)
            Gui.drawRect(x + 2, y, x + width - 2, y + height, module.isEnabled() ? color.getRGB() : 0xff232623);
        else
            Gui.drawRect(x + 2, y, x + width - 2, y + height, 0xff181A17);

        RenderUtils.drawCenteredStringVert(module.name.toLowerCase(), x + 4, y + height/2, extended ? module.isEnabled() ? color.getRGB() : 0xffffffff : 0xffffffff);

        int count = 0;

        float hehe = 0;

        if(extended) {
            final float startY = y + height;
            for(AstolfoButton pan : astolfoButtons) {
                pan.x = x;
                pan.y = startY + pan.height*count;
                pan.drawPanel(mouseX, mouseY);
                count++;

                hehe = pan.height;
            }
        }

        finalHeight = hehe * count + height;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if(isHovered(mouseX, mouseY) && click) {
            if(button == 0) {
                module.toggle();
            } else if(module.settings.size() > 0) extended = !extended;
        }
    }
}
