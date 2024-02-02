package dev.uwuclient.visual;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

public class HUD {
    
	public void renderWaterMark() {
        if(ModManager.interface1.watermark.getValue()){
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int men = fr.getStringWidth(getText1() + " | "+getMinutes()+"m "+getSeconds()+"s "+"Elapsed" + " | " + Minecraft.getDebugFPS() + "FPS") + 20;
            Gui.drawRect(0, 0, men, 15, Color.black.getRGB());
            RenderUtils.drawChromaString(getText1() + " | "+getMinutes()+"m "+getSeconds()+"s "+"Elapsed" + " | " + Minecraft.getDebugFPS() + "FPS", 5, 4, false);
        }
	}

    public long getMinutes(){
        return (System.currentTimeMillis()-UwUClient.INSTANCE.startTime)/1000/60;
    }

    public long getSeconds(){
        return (System.currentTimeMillis()-UwUClient.INSTANCE.startTime)/1000-((System.currentTimeMillis()-UwUClient.INSTANCE.startTime)/1000/60)*60;
    }


    public String getText(){
        return UwUClient.name + EnumChatFormatting.RED + "sense " + EnumChatFormatting.WHITE + UwUClient.version;
    }

    public String getText1(){
        return UwUClient.name + "sense " + UwUClient.version;
    }
	
    public void renderArrayList(){
        /*UwUClient.INSTANCE.modManager.modules.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Mod)m).name)).reversed());

        List<Mod> enabledMods = new ArrayList<Mod>();
        for(int i = 0; i < UwUClient.INSTANCE.modManager.modules.size(); i++) {
            if(UwUClient.INSTANCE.modManager.modules.get(i).isEnabled()) {
                enabledMods.add(UwUClient.INSTANCE.modManager.modules.get(i));
            }
        }
        //List<Mod> enabledMods = (List<Mod>) UwUClient.INSTANCE.modManager.modules.stream().filter(m -> m.isEnabled());
        
        int count = 0;
        for(Mod m : enabledMods) {
            if(m.isEnabled()) {
                Gui.drawRect(GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-6, count*12, GuiScreen.width, count*12+12, 0x70000000);
                RenderUtils.renderChromaString(m.name, GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-2, count*12+12/2-Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT/2);
                RenderUtils.drawChromaRectangle(GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-7+1/2, count*12, GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-6, count*12+12);
                if(count+1 == enabledMods.size()) {
                    RenderUtils.drawChromaRectangle(GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-7+1/2, count*12+12, GuiScreen.width, count*12+13);
                    break;
                }
                RenderUtils.drawChromaRectangle(GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name)-7+1/2, count*12+12, GuiScreen.width-Minecraft.getMinecraft().fontRendererObj.getStringWidth(enabledMods.get(count+1).name)-6, count*12+12+1);
                ++count;
            }
        }*/
    }
    
}
