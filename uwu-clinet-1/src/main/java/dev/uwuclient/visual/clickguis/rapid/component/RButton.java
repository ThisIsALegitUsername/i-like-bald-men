package dev.uwuclient.visual.clickguis.rapid.component;

import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.rapid.component.components.RCheckbox;
import dev.uwuclient.visual.clickguis.rapid.component.components.RKeybind;
import dev.uwuclient.visual.clickguis.rapid.component.components.RModeButton;
import dev.uwuclient.visual.clickguis.rapid.component.components.RSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class RButton extends RComponent {
	public Mod mod;
	public RFrame parent;
	private boolean hovered;
	public boolean open;
	private final ArrayList<RComponent> subcomponents;
	public int offset;

	public RButton(Mod mod, RFrame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<>();
		this.open = false;
		int opY = offset + 12;

		if(mod.settings != null) {
			for(Setting s : mod.settings){
				if(s instanceof ModeSetting)
					this.subcomponents.add(new RModeButton(s, this, opY));

				if(s instanceof NumberSetting)
					this.subcomponents.add(new RSlider(s, this, opY));

				if(s instanceof BooleanSetting)
					this.subcomponents.add(new RCheckbox(s, this, opY));

				opY += 12;
			}
		}
		this.subcomponents.add(new RKeybind(this, opY));

	}
	
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(RComponent comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	public void renderComponent() {
		//outline
			Gui.drawRect(parent.getX() - 1.5, this.parent.getY() + this.offset, parent.getX() + parent.getWidth() + 1.5, this.parent.getY() + 13.5 + this.offset, ModManager.clickGuiMod.getColor(offset * 9L));
	
		Gui.drawRect(parent.getX() - 1, this.parent.getY() + this.offset, parent.getX() + parent.getWidth() + 1, this.parent.getY() + 13 + this.offset, 0xFF0D0E11);
		Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.hovered ? (mod.isEnabled() ? new Color(ModManager.clickGuiMod.getColor(offset * 9L)).darker().getRGB() : 0xFF0D0E11) : (mod.isEnabled() ? ModManager.clickGuiMod.getColor(offset * 9L) : new Color(0xFF0F0F13).brighter().getRGB()));
		Minecraft.getMinecraft().fontRendererObj.drawString(this.mod.name, (parent.getX() + 2) + 2, (parent.getY() + offset + 2) + 1, -1);

		if(this.subcomponents.size() > 1) {
			//Gui.drawModalRectWithCustomSizedTexture((parent.getX() + parent.getWidth() - 12), (parent.getY() + offset + 14) - 12, 0, 0, 9, 9, 9, 9);
		}

		if(this.open && !this.subcomponents.isEmpty()) {
			//outline
				Gui.drawRect(parent.getX() - 1.5, parent.getY() + this.offset + 12, parent.getX() + 106.5, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12) + 0.5, ModManager.clickGuiMod.getColor(offset * 9L));
			for(RComponent comp : this.subcomponents)
				comp.renderComponent();
		}
	}
	
	public int getHeight() {
		if(this.open)
			return (12 * (this.subcomponents.size() + 1));
		return 12;
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(RComponent comp : this.subcomponents)
				comp.updateComponent(mouseX, mouseY);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0)
			this.mod.toggle();

		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(RComponent comp : this.subcomponents)
			comp.mouseClicked(mouseX, mouseY, button);
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(RComponent comp : this.subcomponents)
			comp.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	public void keyTyped(char typedChar, int key) {
		for(RComponent comp : this.subcomponents)
			comp.keyTyped(typedChar, key);
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
	}

}
