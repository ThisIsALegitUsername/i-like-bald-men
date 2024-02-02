package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;

public class MouseOverEvent extends Event{

    public MouseOverEvent(double range, float expand) {
        this.range = range;
        this.expand = expand;
    }
    public double range;
    private float expand;
    public double getRange() {
        return range;
    }
    public void setRange(double range) {
        this.range = range;
    }
    public float getExpand() {
        return expand;
    }
    public void setExpand(float expand) {
        this.expand = expand;
    }
    
}
