package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventStrafe;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.util.MoveUtil;

public class Strafe extends Mod {
    public Strafe(){
        super("Strafe", Category.Player);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventStrafe){
            MoveUtil.strafe();
        }
    }
}