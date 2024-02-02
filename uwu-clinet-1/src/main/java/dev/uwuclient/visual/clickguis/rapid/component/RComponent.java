package dev.uwuclient.visual.clickguis.rapid.component;

import dev.uwuclient.mod.base.setting.Setting;

public class RComponent {
	protected RButton parent;
	protected boolean hovered;
	protected Setting set;

	public void renderComponent() {}
	public void updateComponent(int mouseX, int mouseY) {}
	public void mouseClicked(int mouseX, int mouseY, int button) {}
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
	public void keyTyped(char typedChar, int key) {}
	public void setOff(int newOff) {}
	public int getHeight() {
		return 0;
	}

	public boolean isMouseOnButton(int mouseX, int mouseY, int x, int y) {
		return x > mouseX && x < mouseX + 105 && y > mouseY && y < mouseY + 12;
	}
}
