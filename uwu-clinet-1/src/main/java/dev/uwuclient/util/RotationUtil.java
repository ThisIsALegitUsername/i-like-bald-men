package dev.uwuclient.util;

import net.minecraft.client.Minecraft;

public class RotationUtil {

	public static float serverYaw, serverPitch;

    public static int uwu = 5;//0;

    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }

    public float[] limitAngleChange(final float[] currRot, final float[] targetRot, final float turnSpeed) {
        final float currentYaw = currRot[0];
        final float currentPitch = currRot[1];

        final float targetYaw = targetRot[0];
        final float targetPitch = targetRot[1];

        final float yawDifference = getAngleDifference(targetYaw, currentYaw);
        final float pitchDifference = getAngleDifference(targetPitch, currentPitch);

        final float limitedYaw = currentYaw + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed));
        final float limitedPitch = currentPitch + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed));

        return new float[]{limitedYaw, limitedPitch};
    }

    public float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }
}
