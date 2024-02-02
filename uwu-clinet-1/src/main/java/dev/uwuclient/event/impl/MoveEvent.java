package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;

public class MoveEvent extends Event{
    public double posX, posY, posZ;

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public MoveEvent(double posX, double posY, double z) {
        this.posX = posX;
        this.posY = posY;
        posZ = z;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double z) {
        posZ = z;
    }

}
