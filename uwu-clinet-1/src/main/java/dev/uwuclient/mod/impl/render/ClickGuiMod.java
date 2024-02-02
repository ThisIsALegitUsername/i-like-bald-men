package dev.uwuclient.mod.impl.render;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.Color;
import dev.uwuclient.visual.clickguis.ClientGui;
import dev.uwuclient.visual.clickguis.ascgui.AstolfoClickGui;
import dev.uwuclient.visual.clickguis.csgogui.CsgoGui;
import dev.uwuclient.visual.clickguis.lemon.ClickGui;
import dev.uwuclient.visual.clickguis.rapid.RClickGui;
import dev.uwuclient.visual.clickguis.strike.StrikeGUI;

public class ClickGuiMod extends Mod{
    public ClickGuiMod(){
        super("ClickGuiMod", Category.Render);
    }
    
	public ModeSetting gui = new ModeSetting("ClickGUI", this, "uwu", "uwu", "lemon", "astolfo", "csgo", "strike");
    public NumberSetting red = new NumberSetting("Red", this, 255, 0, 255, 1);
    public NumberSetting green = new NumberSetting("Green", this, 100, 0, 255, 1);
    public NumberSetting blue = new NumberSetting("Blue", this, 100, 0, 255, 1);
    public NumberSetting red1 = new NumberSetting("Second red", this, 255, 0, 255, 1);
    public NumberSetting green1 = new NumberSetting("Second green", this, 100, 0, 255, 1);
    public NumberSetting blue1 = new NumberSetting("Second blue", this, 100, 0, 255, 1);
    public NumberSetting rainbow = new NumberSetting("Rainbow", this, 10, 0, 100, 1);
    public ModeSetting colorMode = new ModeSetting("Color", this, "Custom", "Rainbow", "Fade", "Gradient", "Custom");

	@Override
	public void onEnable(){
		if(mc.currentScreen instanceof ClientGui)
			return;

		switch(gui.getValue()){
			case "strike":
				mc.displayGuiScreen(new StrikeGUI());
				break;
			case "uwu":
				mc.displayGuiScreen(new RClickGui());
				break;
			case "lemon":
				mc.displayGuiScreen(new ClickGui());
				break;
			case "astolfo":
				mc.displayGuiScreen(new AstolfoClickGui());
				break;
			case "csgo":
				mc.displayGuiScreen(new CsgoGui());
				break;
			default:
				mc.displayGuiScreen(new RClickGui());
		}
	}

    public int getColor(long wave) {
		Color
		custom = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()),
		custom1 = new Color((int) red1.getValue(), (int) green1.getValue(), (int) blue1.getValue());
		double offset = (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (wave * 49.8) / rainbow.getValue() / 50;

		switch (colorMode.getValue()) {
		case "Custom":
			return custom.getRGB();
		case "Rainbow":
			return getRainbow(4, 0.5f, 1f, wave * 10);
		case "Fade":
			return getGradient(custom, custom.darker().darker(), offset);
		case "Gradient":
			return getGradient(custom, custom1, offset);
		}
		return -1;
	}

    public static int getRainbow(float seconds, float saturation, float brightness, float index) {
		return Color.HSBtoRGB(((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000), saturation, brightness);
	}

    public static int getGradient(Color first, Color second, double index) {
        if (index > 1)
        	index = (int)index % 2 == 0 ? index % 1 : 1 - index % 1;
        
        return new Color(
		(int) (first.getRed() * (1 - index) + second.getRed() * index),
		(int) (first.getGreen() * (1 - index) + second.getGreen() * index),
		(int) (first.getBlue() * (1 - index) + second.getBlue() * index)).getRGB();
    }
    
}
