package dev.uwuclient.visual.clickguis.lemon;

import java.io.IOException;
import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.visual.clickguis.ClientGui;
import dev.uwuclient.visual.clickguis.lemon.component.Component;
import dev.uwuclient.visual.clickguis.lemon.component.Frame;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;


public class ClickGui extends GuiScreen implements ClientGui{

	public static ArrayList<Frame> frames = new ArrayList<>();
	public static int color = 0xFF5A5A5A;
	public int scrollOffset = 0;
	
	public ClickGui() {
		int frameX = 5;
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}
	
	@Override
	public void initGui() {
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		for(Frame frame : frames) {
			int scrollOffset = this.scrollOffset;
			int scrolledY = 0;
			if(frame.isDragging){
				scrolledY = mouseY;
			}

			frame.setY(scrollOffset+scrolledY);
			frame.renderFrame(Minecraft.getMinecraft().fontRendererObj);
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton){
		for(Frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		
		if (keyCode == KeyboardConstants.KEY_RSHIFT) {
            this.mc.displayGuiScreen(null);
        }
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void onGuiClosed(){
		frames.clear();
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
