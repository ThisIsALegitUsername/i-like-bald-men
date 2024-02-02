package dev.uwuclient.visual.clickguis.ascgui;

import java.io.IOException;
import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.ClientGui;
import dev.uwuclient.visual.clickguis.ascgui.buttons.AstolfoButton;
import dev.uwuclient.visual.clickguis.ascgui.buttons.AstolfoCategoryPanel;
import dev.uwuclient.visual.clickguis.ascgui.buttons.AstolfoModuleButton;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.gui.GuiScreen;

public class AstolfoClickGui extends GuiScreen implements ClientGui{

    public ArrayList<AstolfoCategoryPanel> categoryPanels = new ArrayList<>();
    public int scrollOffset = 0;

    public AstolfoClickGui() {

        int count = 4;

        for(Category cat : Category.values()) {
            switch (cat) {
                case Combat:
                    categoryPanels.add(new AstolfoCategoryPanel(count, 4, 100, 18, cat, new Color(255, 230, 77)));
                    break;
                case Player:
                    categoryPanels.add(new AstolfoCategoryPanel(count, 4, 100, 18, cat, new Color(255, 142, 69)));
                    break;
                case Misc:
                    categoryPanels.add(new AstolfoCategoryPanel(count, 4, 100, 18, cat, new Color(255, 242, 157)));
                    break;
                case Render:
                    categoryPanels.add(new AstolfoCategoryPanel(count, 4, 100, 18, cat, new Color(255, 34, 1)));
                    break;
            }

            count += 120;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(AstolfoCategoryPanel catPanel : categoryPanels) {

            int scrollOffset = this.scrollOffset;
			int scrolledY = 0;
			if(catPanel.dragged){
				scrolledY = mouseY;
			}

			catPanel.y = (scrollOffset+scrolledY);

            catPanel.drawPanel(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            for(AstolfoCategoryPanel catPan : categoryPanels) {
                catPan.mouseAction(mouseX, mouseY, true, mouseButton);

                if(catPan.open) {
                    for(AstolfoModuleButton modPan : catPan.moduleButtons) {
                        modPan.mouseAction(mouseX, mouseY, true, mouseButton);
                        if(modPan.extended) {
                            for(AstolfoButton pan : modPan.astolfoButtons) {
                                pan.mouseAction(mouseX, mouseY, true, mouseButton);
                            }
                        }
                    }
                }
            }
    }
    
	@Override
	public void handleMouseInput() throws IOException {
        int scroll = Mouse.getEventDWheel();

        if (scroll > 0) {
            scrollOffset += 5;
        } else if (scroll < 0) {
            scrollOffset -= 5;
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
            for(AstolfoCategoryPanel catPan : categoryPanels) {
                catPan.mouseAction(mouseX, mouseY, false, state);

                if(catPan.open) {
                    for(AstolfoModuleButton modPan : catPan.moduleButtons) {
                        modPan.mouseAction(mouseX, mouseY, false, state);

                        if(modPan.extended) {
                            for(AstolfoButton pan : modPan.astolfoButtons) {
                                pan.mouseAction(mouseX, mouseY, false, state);
                            }
                        }
                    }
                }
            }
    }
}
