package dev.uwuclient.visual.clickguis.ascgui.buttons;

import java.util.ArrayList;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;

public class AstolfoCategoryPanel extends AstolfoButton {
    public Category category;
    public Color color;

    public boolean dragged, open;
    public int mouseX2, mouseY2;

    public float count;

    public ArrayList<AstolfoModuleButton> moduleButtons = new ArrayList<>();

    public static int getRainbow1(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.HSBtoRGB((float) (rainbowState / 360.0f), 0.8f, 0.7f);
    }

    public AstolfoCategoryPanel(float x, float y, float width, float height, Category cat, Color color) {
        super(x, y, width, height);
        category = cat;
        this.color = color;

        int count = 0;

        final float startY = y + height;

        for(Mod mod : UwUClient.INSTANCE.modManager.modules) {
            if(mod.category == category) {
                moduleButtons.add(new AstolfoModuleButton(x, startY + height*count, width, height, mod, color));
                count++;
            }
        }
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        if(dragged) {
            x = mouseX2 + mouseX;
            y = mouseY2 + mouseY;
        }


        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);
        RenderUtils.drawCenteredStringVert(String.valueOf(category).toLowerCase(), x + 4, y + height/2, 0xffffffff);

        count = 0;

        if(open) {

            final float startY = y + height;

            for (AstolfoModuleButton modulePanel : moduleButtons) {
                modulePanel.x = x;
                modulePanel.y = startY + count;
                modulePanel.drawPanel(mouseX, mouseY);
                count += modulePanel.finalHeight;
            }
        }

        Gui.drawRect(x, (y + count) + height, x + width, (y + count) + height + 2, 0xff181A17);

        RenderUtils.drawRectOutline((double)x, (double)y, (double)x + (double)width, (double)(y + count) + (double)height + 2, getRainbow1(50));
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if(isHovered(mouseX, mouseY)) {
            if(click) {
                if(button == 0) {
                    dragged = true;
                    mouseX2 = (int) (x - mouseX);
                    mouseY2 = (int) (y - mouseY);
                } else {
                    open = !open;
                }
            }
        }

        if(!click) dragged = false;
    }
}
