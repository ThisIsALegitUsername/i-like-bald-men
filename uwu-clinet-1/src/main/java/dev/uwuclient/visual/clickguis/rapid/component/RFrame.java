package dev.uwuclient.visual.clickguis.rapid.component;

import java.util.ArrayList;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.visual.clickguis.rapid.RClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class RFrame {

	public ArrayList<RComponent> components;
	public Category category;
	public boolean open, dragging;
	private final int width;
	public int x, y, dragX, dragY;
	private final int barHeight;
	
	public RFrame(Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 105;
		this.x = 5;
		this.y = 5;
		this.barHeight = 16;
		this.dragX = 0;
		this.open = false;
		this.dragging = false;
		int tY = this.barHeight;
		
		for(Mod mod : UwUClient.INSTANCE.modManager.modsInCategory(category)) {
			RButton modButton = new RButton(mod, this, tY);
			this.components.add(modButton);
			tY += 12;
		}
	}
	
	public void renderFrame() {
		//outline
		Gui.drawRect(this.x - 1.5, this.y - 0.5, this.x + this.width + 1.5, this.y + this.barHeight + 0.5, ModManager.clickGuiMod.getColor(barHeight));

		Gui.drawRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.barHeight, RClickGui.backgroundDark);
		Minecraft.getMinecraft().fontRendererObj.drawString(this.category.name(), (this.x + 2) + 5, this.y + 5, -1);


		if(this.open && !this.components.isEmpty()) {
			for(RComponent component : components)
				component.renderComponent();
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(RComponent comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.dragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

	public ArrayList<RComponent> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.dragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
