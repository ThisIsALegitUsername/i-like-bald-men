package dev.uwuclient.mod.impl.misc;

import java.util.Random;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.components.PingSpoofComponent;

public class PingSpoof extends Mod {
    public PingSpoof(){
        super("Ping spoof", Category.Misc, 0);
    }

    private final NumberSetting delayMin = new NumberSetting("Delay", this, 1000, 50, 30000, 1);
    private final NumberSetting delayMax = new NumberSetting("Delay", this, 1500, 50, 30000, 1);
    private final BooleanSetting teleports = new BooleanSetting("Delay Teleports", this, false);
    private final BooleanSetting velocity = new BooleanSetting("Delay Velocity", this, false);
    private final BooleanSetting world = new BooleanSetting("Delay Block Updates", this, false);
    private final BooleanSetting entities = new BooleanSetting("Delay Entity Movements", this, false);


    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            PingSpoofComponent.setSpoofing((int) getRandom(delayMin.getValue(), delayMax.getValue()),
            true, teleports.getValue(), velocity.getValue(), world.getValue(), entities.getValue());
        }
    }

    public double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }

        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }
}