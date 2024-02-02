package dev.uwuclient.event.impl;

public class Button {
    public Button(boolean button, double offset) {
        this.button = button;
        this.offset = offset;
    }
    boolean button;
    double offset;
    public boolean isButton() {
        return button;
    }
    public void setButton(boolean button) {
        this.button = button;
    }
    public double getOffset() {
        return offset;
    }
    public void setOffset(double offset) {
        this.offset = offset;
    }
}
