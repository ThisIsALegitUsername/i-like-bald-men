package dev.uwuclient.mod.impl.render;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;

public class Animations extends Mod{
    public final ModeSetting mode = new ModeSetting("Animation", "Small",
    "None", "1.7", "Skidding", "Spin", "Forward", "Smooth", "Small", "Down", "Old", "Leaked");
    public final NumberSetting x = new NumberSetting("X", "", 2.6f, 0.1f, 4, 0.1f);
    public final NumberSetting y = new NumberSetting("Y", "", 1.9f, 0.1f, 4, 0.1f);
    public final NumberSetting z = new NumberSetting("Z", "", 1.6f, 0.1f, 4, 0.1f);
    public final NumberSetting scale = new NumberSetting("Scale", "", 0.1f, 0.1f, 2, 0.1f);

    public Animations(){
        super("Animations", Category.Render, 0);
        addSetting(mode, x, y, z, scale);
    }

    public static double xO, yO, zO, scale0 = 1;

    public void onDisable(){
        xO = yO = zO = 0;
        scale0 = 1;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            xO = (x.getValue()-2.1f) / 3;
            yO = (y.getValue()-2.1f) / 3;
            zO = (z.getValue()-2.1f) / 3;
    
            scale0 = scale.getValue();    
        }
    }
    
}
