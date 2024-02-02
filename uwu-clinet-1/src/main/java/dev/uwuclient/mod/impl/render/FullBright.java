package dev.uwuclient.mod.impl.render;

import dev.uwuclient.mod.base.Mod;

public class FullBright extends Mod{
    public FullBright(){
        super("Fullbright", Category.Render, 0x00);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gammaSetting = 1000;
    }
    
    public void onDisable(){
        mc.gameSettings.gammaSetting = 1;
    }
    
}
