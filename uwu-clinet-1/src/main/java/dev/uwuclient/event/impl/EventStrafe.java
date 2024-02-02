package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.client.Minecraft;

public class EventStrafe extends Event{
    private float forward, strafe, friction;
    Minecraft mc = Minecraft.getMinecraft();

    public float getForward() {
        return forward;
    }

    public EventStrafe(float forward, float strafe, float friction) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setSpeedPartialStrafe(float friction, final float strafe) {
        final float remainder = 1 - strafe;

        if (forward != 0 && this.strafe != 0) {
            friction *= 0.91;
        }

        if (mc.thePlayer.onGround) {
            setSpeed(friction);
        } else {
            mc.thePlayer.motionX *= strafe;
            mc.thePlayer.motionZ *= strafe;
            setFriction(friction * remainder);
        }
    }

    public void setSpeed(final float speed, final double motionMultiplier) {
        setFriction(getForward() != 0 && getStrafe() != 0 ? speed * 0.99F : speed);
        mc.thePlayer.motionX *= motionMultiplier;
        mc.thePlayer.motionZ *= motionMultiplier;
    }

    public void setSpeed(final float speed) {
        setFriction(getForward() != 0 && getStrafe() != 0 ? speed * 0.99F : speed);
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
    }
    
}
