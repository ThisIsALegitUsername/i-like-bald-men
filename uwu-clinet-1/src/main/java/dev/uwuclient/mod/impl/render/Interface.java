package dev.uwuclient.mod.impl.render;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;

public class Interface extends Mod{
    public Interface(){
        super("Interface", Category.Render);
    }

    public BooleanSetting arraylist = new BooleanSetting("ArrayList", this, true);
    public BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
    public ModeSetting cape = new ModeSetting("Cape", this, "Electric Sky", "Electric Sky");
    
}
