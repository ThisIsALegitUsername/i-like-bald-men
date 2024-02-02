package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.HitSlowDownEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;

public class KeepSprint extends Mod{
    public KeepSprint(){
        super("KeepSprint", Category.Player, 0);
        addSetting(slowDownVelocity, slowDownNormal, bufferAbuse, bufferDecrease, maxBuffer, sprintSlowDownVelocity, sprintSlowDownNormal, onlyInAir);
    }

    private final NumberSetting slowDownVelocity = new NumberSetting("Hit Slow Down During Velocity", "", 0.6f, 0, 1, 0.05f);
    private final NumberSetting slowDownNormal = new NumberSetting("Hit Slow Down Normal", "", 0.6f, 0, 1, 0.05f);
    private final NumberSetting bufferDecrease = new NumberSetting("Buffer Decrease", "", 1, 0.1f, 10, 0.1f);
    private final NumberSetting maxBuffer = new NumberSetting("Max Buffer", "", 5, 1, 10, 1);
    private final BooleanSetting sprintSlowDownVelocity = new BooleanSetting("Velocity Hit Sprint", "", false);
    private final BooleanSetting sprintSlowDownNormal = new BooleanSetting("Normal Hit Sprint", "", false);
    private final BooleanSetting bufferAbuse = new BooleanSetting("Buffer Abuse", "", false);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", "", false);

    private boolean resetting;
    private double combo;

    @Override
    public void onEvent(Event e) {
        if(e instanceof HitSlowDownEvent){
            HitSlowDownEvent event = ((HitSlowDownEvent)e);
        if (mc.thePlayer.onGround && this.onlyInAir.getValue()) {
            return;
        }

        if (this.bufferAbuse.getValue()) {
            if (this.combo < this.maxBuffer.getValue() && !this.resetting) {
                this.combo++;
            } else {
                if (this.combo > 0) {
                    this.combo = Math.max(0, this.combo - this.bufferDecrease.getValue());
                    this.resetting = true;
                    return;
                } else {
                    this.resetting = false;
                }
            }
        } else {
            this.combo = 0;
        }

        if (mc.thePlayer.hurtTime > 0) {
            event.setSlowDown(this.slowDownVelocity.getValue());
            event.setSprint(this.sprintSlowDownVelocity.getValue());
        } else {
            event.setSlowDown(this.slowDownNormal.getValue());
            event.setSprint(this.sprintSlowDownNormal.getValue());
        }
    }
    }
    
}
