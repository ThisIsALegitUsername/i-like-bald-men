package dev.uwuclient.mod.impl.render;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRender2d;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class Arraylist extends Mod{

    private final ModeSetting mode = new ModeSetting("Color mode", "Rainbow", "Rainbow", "Color");
    private final NumberSetting r = new NumberSetting("R", this, 120, 0, 255, 1);
    private final NumberSetting g = new NumberSetting("G", this, 120, 0, 255, 1);
    private final NumberSetting b = new NumberSetting("B", this, 120, 0, 255, 1);
    private final NumberSetting saturation = new NumberSetting("Saturation", this, 0.5f, 0.1f, 1, 0.01f);
    private final NumberSetting brightness = new NumberSetting("Brightness", this, 0.5f, 0.1f, 1, 0.01f);

    public Arraylist(){
        super("ArrayList", Category.Render);
        this.addSetting(mode);
    }

    private int rainbowTick;
    private int rainbowTick2;
    private TimerUtil2 timer = new TimerUtil2();

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender2d){
            drawObject();
        }
    }

    public void drawObject() {
        if(ModManager.interface1.arraylist.getValue()){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float x1 = sr.getScaledWidth(), y1 = 0;
        if (Minecraft.getMinecraft().thePlayer.ticksExisted % 20 == 0) {
            UwUClient.INSTANCE.modManager.modules.sort(((o1, o2) -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.getSuffix().isEmpty() ? o2.name : String.format("%s %s", o2.name, o2.getSuffix())) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.getSuffix().isEmpty() ? o1.name : String.format("%s %s", o1.name, o1.getSuffix()))));
        }

        rainbowTick = 0;
        rainbowTick2 = 0;

        java.util.ArrayList<Mod> mods = new java.util.ArrayList<>();
        for (Mod m : UwUClient.INSTANCE.modManager.modules) {
            if (m.isEnabled()) {
                mods.add(m);
            } else {
                m.animX = x1;
            }
        }

        float ys = y1;
        if (timer.delay(5)) {
            for (Mod mod : mods) {
                mod.animY = mod.animationUtils.animate(ys, mod.animY, 0.35f);
                mod.animX = mod.animationUtils2.animate(x1 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod.name + (mod.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + mod.getSuffix()) - 4), mod.animX, 0.35f);
                ys += 12;
            }
            timer.reset();
        }

        float arrayListY = y1;
        int i = 0;
        for (Mod mod : mods) {
            
            if (!mod.isEnabled())
                return;
            if (++rainbowTick2 > 50) {
                rainbowTick2 = 0;
            }
            Color arrayRainbow2 = new Color((int)r.getValue(), (int)g.getValue(), (int)b.getValue());
            if (mode.getValue().equals("Rainbow")) {
                arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, saturation.getValue(), brightness.getValue()));
            } else if (mode.getValue().equals("Colored rainbow")) {
                Color temp = new Color(Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, saturation.getValue(), 1));
                arrayRainbow2 = new Color((int)r.getValue(), (int)g.getValue(), (int)b.getValue(), temp.getRed());
            } else if (mode.getValue().equals("Color")) {
                arrayRainbow2 = new Color((int)r.getValue(), (int)g.getValue(), (int)b.getValue());
            }

            {
                if (i + 1 <= mods.size() - 1) {
                    Mod m2 = mods.get(i + 1);
                    Gui.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, (int) mod.animX - 9 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod.name + (mod.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + mod.getSuffix()) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(m2.name + (m2.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 12, arrayRainbow2.getRGB());
                } else if (i == mods.size() - 1) {
                    Gui.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, x1, (int) mod.animY + 12, arrayRainbow2.getRGB());
                }
                Gui.drawRect((int) mod.animX - 10, ((float) mod.animY), ((int) mod.animX - 9), (int) mod.animY + 11, arrayRainbow2.getRGB());
                Gui.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, new Color(0, 0, 0, 100).getRGB());
//            Gui.drawRect((x1) - 1f, mod.animY, (x1), mod.animY + 12, arrayRainbow2.getRGB());
            }

            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(mod.name + (mod.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
            arrayListY += 12f;
            i++;
        }
        if (rainbowTick++ > 50) {
            rainbowTick = 0;
        }
    }
    }
}
