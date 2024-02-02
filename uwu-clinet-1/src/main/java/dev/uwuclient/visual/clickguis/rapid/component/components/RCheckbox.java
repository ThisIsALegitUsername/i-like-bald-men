package dev.uwuclient.visual.clickguis.rapid.component.components;

import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.rapid.component.RButton;
import dev.uwuclient.visual.clickguis.rapid.component.RComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class RCheckbox extends RComponent {
	private int offset, x, y;

	public RCheckbox(Setting option, RButton button, int offset) {
		this.set = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		Minecraft.getMinecraft().fontRendererObj.drawString(this.set.name, (parent.parent.getX() + 2) + 2, (parent.parent.getY() + offset + 2) + 1.5f, -1, false);
		Gui.drawRect(parent.parent.getX() + 95, parent.parent.getY() + offset + 2, parent.parent.getX() + 103, parent.parent.getY() + offset + 10, new Color(0xFF0F0F13).brighter().brighter().getRGB());

		if(((BooleanSetting)set).getValue())
			Gui.drawRect(parent.parent.getX() + 96, parent.parent.getY() + offset + 3, parent.parent.getX() + 102, parent.parent.getY() + offset + 9, ModManager.clickGuiMod.getColor(offset * 9L));
	}

	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && button == 0 && this.parent.open)
			((BooleanSetting)set).toggle();
	}

	public void setOff(int newOff) {
		offset = newOff;
	}
}
