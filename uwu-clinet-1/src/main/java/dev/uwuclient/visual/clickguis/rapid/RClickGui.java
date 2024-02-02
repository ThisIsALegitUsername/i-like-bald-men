package dev.uwuclient.visual.clickguis.rapid;

import java.io.IOException;
import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.ClientGui;
import dev.uwuclient.visual.clickguis.rapid.component.RComponent;
import dev.uwuclient.visual.clickguis.rapid.component.RFrame;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.gui.GuiScreen;

public class RClickGui extends GuiScreen implements ClientGui{
	public static ArrayList<RFrame> frames;
	public int scrollOffset = 0;
	public static int color = 0xFFCC4646,
	background = 0xFF0F0F13,
	backgroundDark = new Color(0xFF0F0F13).darker().getRGB();

	public RClickGui() {
		frames = new ArrayList<>();
		int frameX = 5;
		for(Category category : Category.values()) {
			RFrame frame = new RFrame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 5;
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Gui.drawRect(0, 0, width, height, 0xCC200000);

		for(RFrame frame : frames) {

			int scrollOffset = this.scrollOffset;
			int scrolledY = 0;
			if(frame.dragging){
				scrolledY = mouseY;
			}

			frame.setY(scrollOffset+scrolledY);

			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);

			for(RComponent comp : frame.getComponents())
				comp.updateComponent(mouseX, mouseY);
		}
		
		//Gui.drawRect(4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4, isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4) ? color : 0xFF0F0F0F);
		//Gui.drawRect(5, height - 25, 15 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 5, 0xFF1F1F1F);
		//mc.fontRendererObj.drawString("Draggable Hud", 10, height - 19, -1);
	}
	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		for(RFrame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
				frame.setDrag(true);
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1)
				frame.setOpen(!frame.isOpen());

			if(frame.isOpen() && !frame.getComponents().isEmpty()) {
				for(RComponent component : frame.getComponents())
					component.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		/*if(mouseButton == 0 && isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4)) {
    		if(position == null)
    			position = new GuiPosition();
    		mc.displayGuiScreen(position);
		}*/
	}
	
	protected void keyTyped(char typedChar, int keyCode) {
		for(RFrame frame : frames) {
			if(frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for(RComponent component : frame.getComponents())
					component.keyTyped(typedChar, keyCode);
			}
		}
		if (keyCode == 1)
            this.mc.displayGuiScreen(null);
	}

	
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(RFrame frame : frames)
			frame.setDrag(false);

		for(RFrame frame : frames) {
			if(frame.isOpen() && !frame.getComponents().isEmpty()) {
				for(RComponent component : frame.getComponents())
					component.mouseReleased(mouseX, mouseY, state);
			}
		}
	}
    
    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }
	
	public boolean doesGuiPauseGame() {
		return true;
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
}
