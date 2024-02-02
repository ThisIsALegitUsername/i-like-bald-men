package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;

public class HitSlowDownEvent extends Event{

    public HitSlowDownEvent(double slowDown, boolean sprint) {
        this.slowDown = slowDown;
        this.sprint = sprint;
    }

    public double slowDown;
    public boolean sprint;
    public double getSlowDown() {
        return slowDown;
    }
    public void setSlowDown(double slowDown) {
        this.slowDown = slowDown;
    }
    public boolean isSprint() {
        return sprint;
    }
    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }
    
}
