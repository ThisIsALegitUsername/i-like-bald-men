package dev.uwuclient.visual.clickguis.rapid.component.components;

import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.rapid.component.RButton;
import dev.uwuclient.visual.clickguis.rapid.component.RComponent;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class RKeybind extends RComponent {
	private boolean binding;
	private int offset, x, y;

	public RKeybind(RButton button, int offset) {
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;

	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		Minecraft.getMinecraft().fontRendererObj.drawString("Key", (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1, false);
		Minecraft.getMinecraft().fontRendererObj.drawString(binding ? "WAITING..." : Keyboard.getKeyName(parent.mod.key), (parent.parent.getX() + 100) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(binding ? "Listening..." : Keyboard.getKeyName(this.parent.mod.key)) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1, false);

	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && button == 0 && this.parent.open)
			this.binding = !binding;
	}
	
	public void keyTyped(char typedChar, int key) {
		if(this.binding) {
			this.parent.mod.key = key;
			this.binding = false;
		}
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
