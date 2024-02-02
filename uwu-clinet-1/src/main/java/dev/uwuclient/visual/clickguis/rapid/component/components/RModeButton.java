package dev.uwuclient.visual.clickguis.rapid.component.components;

import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.rapid.component.RButton;
import dev.uwuclient.visual.clickguis.rapid.component.RComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class RModeButton extends RComponent {
	private int offset, x, y;

	public RModeButton(Setting set, RButton button, int offset) {
		this.set = set;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		Minecraft.getMinecraft().fontRendererObj.drawString(set.name, (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1, false);
		Minecraft.getMinecraft().fontRendererObj.drawString(((ModeSetting)set).getValue(), (parent.parent.getX() + 100) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(((ModeSetting)set).getValue()) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1, false);
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && this.parent.open) {
			if(button == 0) {
				((ModeSetting)set).cycle(true);
			} else if(button == 1) {
				((ModeSetting)set).cycle(false);
			}
		}
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
