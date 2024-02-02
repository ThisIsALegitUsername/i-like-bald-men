package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2d extends Event{
    public EventRender2d(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }
    public float partialTicks;
    public ScaledResolution scaledResolution;
    public float getPartialTicks() {
        return partialTicks;
    }
    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
    
}
