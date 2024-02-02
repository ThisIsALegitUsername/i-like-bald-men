package dev.uwuclient.visual.clickguis.lemon.component.components;

import java.util.ArrayList;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.base.setting.Setting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.lemon.ClickGui;
import dev.uwuclient.visual.clickguis.lemon.component.Component;
import dev.uwuclient.visual.clickguis.lemon.component.Frame;
import dev.uwuclient.visual.clickguis.lemon.component.components.sub.Checkbox;
import dev.uwuclient.visual.clickguis.lemon.component.components.sub.Keybind;
import dev.uwuclient.visual.clickguis.lemon.component.components.sub.ModeButton;
import dev.uwuclient.visual.clickguis.lemon.component.components.sub.Slider;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Button extends Component {

	public Mod mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	
	public Button(Mod mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		int opY = offset + 12;

		if(!mod.settings.isEmpty()){
			for(Setting s : mod.settings){
				if(s instanceof ModeSetting && !s.hidden){
					this.subcomponents.add(new ModeButton(((ModeSetting)s), this, opY));
					opY += 12;
				}
				if(s instanceof NumberSetting && !s.hidden){
					Slider slider = new Slider(((NumberSetting)s), this, opY);
					this.subcomponents.add(slider);
					opY += 12;
				}
				if(s instanceof BooleanSetting && !s.hidden){
					Checkbox check = new Checkbox(((BooleanSetting)s), this, opY);
					this.subcomponents.add(check);
					opY += 12;
				}
			}
		}

		this.subcomponents.add(new Keybind(this, opY));
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (mod.isEnabled() ? new Color(230, 230, 230).getRGB() : 0xFF222222) : (this.mod.isEnabled() ? -1 : 0xFF111111));
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5f,0.5f, 0.5f);
		Minecraft.getMinecraft().fontRendererObj.drawString(this.mod.name, (parent.getX() + 2) * 2, (parent.getY() + offset + 2) * 2 + 4, this.mod.isEnabled() ? 0 : -1);
		if(this.mod.hasSetting)
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 10) * 2, (parent.getY() + offset + 2) * 2 + 4, -1);
		GlStateManager.popMatrix();
		if(this.open) {
			if(!this.subcomponents.isEmpty()) {
				for(Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				Gui.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), ClickGui.color);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		return 12;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset) {
			return true;
		}
		return false;
	}
}
