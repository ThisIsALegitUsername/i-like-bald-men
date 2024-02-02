package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.NumberSetting;

public class Reach extends Mod{

    public static NumberSetting min = new NumberSetting("Minimum", "", 3.2f, 1, 6, 0.1f);
    public static NumberSetting max = new NumberSetting("Maximum", "", 3.2f, 1, 6, 0.1f);

    @Override
    public void onUpdateAlwaysGUI(){
        if (max.getValue() < min.getValue()) {
            max.setValue(min.getValue());
        }
    }

    public Reach(){
        super("Reach", Category.Combat, 0x00);
        addSetting(min, max);
    }
    
}
