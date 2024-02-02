package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventStrafe;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.util.MoveUtil;

public class Sprint extends Mod{
    public Sprint(){
        super("Sprint", Category.Player, 0x00);
    }

    private final BooleanSetting legit = new BooleanSetting("Legit", this, true);

    public void onEvent(Event e){
        if(e instanceof EventStrafe){
            mc.gameSettings.keyBindSprint.pressed = true;
    
            if (mc.thePlayer.omniSprint && MoveUtil.isMoving() && !legit.getValue()) {
                mc.thePlayer.setSprinting(true);
            }
    
            mc.thePlayer.omniSprint = !legit.getValue() && MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally &&
                    !mc.thePlayer.isSneaking() && !mc.thePlayer.isUsingItem();
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.isKeyDown());
        mc.thePlayer.omniSprint = false;
    }

}
