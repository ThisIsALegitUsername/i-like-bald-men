package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.impl.misc.AntiBot;
import dev.uwuclient.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class FixedAimAssist extends Mod{
	public FixedAimAssist() {
		super("AimAssist", Category.Combat);
		addSetting(strength, range, players, nonPlayers, teams, invisibles, dead, vertical);
	}

    public static EntityLivingBase target;

    private final NumberSetting strength = new NumberSetting("Strength", "", 40, 1, 50, 0.1f);
    private final NumberSetting range = new NumberSetting("Range", "", 6, 0.1f, 10, 0.1f);

    private final BooleanSetting players = new BooleanSetting("Players","", true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players","", true);
    private final BooleanSetting teams = new BooleanSetting("Teams","", true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles","", false);
    private final BooleanSetting dead = new BooleanSetting("Dead","", false);
    private final BooleanSetting vertical = new BooleanSetting("Vertical","", true);
    
    public static int deltaX, deltaY;
    

    //PREMOTION
    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            target = getClosest(range.getValue());

            final float s = (float) (strength.getMax() - strength.getValue()) + 1;

            if (target == null || !mc.thePlayer.canEntityBeSeen(target)) {
                deltaX = deltaY = 0;
                return;
            }

            final float[] rotations = getRotations();

            final float targetYaw = (float) (rotations[0] + Math.random());
            final float targetPitch = (float) (rotations[1] + Math.random());

            final float uwuYaw = (targetYaw - mc.thePlayer.rotationYaw) / Math.max(2, s);
            final float uwuPitch = (targetPitch - mc.thePlayer.rotationPitch) / Math.max(2, s);

            final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            final float gcd = f * f * f * 8.0F;

            deltaX = Math.round(uwuYaw / gcd);

            if (vertical.getValue()) deltaY = Math.round(uwuPitch / gcd);
                else deltaY = 0;
        }
    }

    @Override
	public void onDisable() {
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * Gets the closest target in a range for the aura to decide which entity it will attack.
     *
     * @param range The maximum range the closest entity will be searched for.
     * @return The closest entity in a range.
     */
    private EntityLivingBase getClosest(final double range) {
        if (mc.theWorld == null) return null;

        double dist = range;
        EntityLivingBase target = null;

        for (final Entity entity : mc.theWorld.loadedEntityList) {

            if (entity instanceof EntityLivingBase && !AntiBot.bots.contains(entity)) {
                final EntityLivingBase livingBase = (EntityLivingBase) entity;

                if (canAttack(livingBase)) {
                    final double currentDist = mc.thePlayer.getDistanceToEntity(livingBase);

                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = livingBase;
                    }
                }
            }
        }

        return target;
    }
    
    private boolean canAttack(final EntityLivingBase player) {
        if (player instanceof EntityPlayer && !players.getValue()) {
            return false;
        }

        if (player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (!nonPlayers.getValue())
                return false;
        }

        if (player.isInvisible() && !invisibles.getValue())
            return false;

        if (player.isDead && !dead.getValue())
            return false;

        if (AntiBot.bots.contains(player))
            return false;

        if (player.isOnSameTeam(mc.thePlayer) && teams.getValue())
            return false;

        if (player.ticksExisted < 2)
            return false;

        return mc.thePlayer != player;
    }

    private float[] getRotations() {
        final double var4 = (target.posX - (target.lastTickPosX - target.posX)) + 0.01 - mc.thePlayer.posX;
        final double var6 = (target.posZ - (target.lastTickPosZ - target.posZ)) - mc.thePlayer.posZ;
        final double var8 = (target.posY - (target.lastTickPosY - target.posY)) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);

        float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);

        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);


        /*
         * Gets the current and last rotations and smooths them for aura to be harder to flag.
         */
        final float[] rotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{Aura.yaw, Aura.pitch};
        //final float[] lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];


        // Clamps the pitch so that aura doesn't flag everything with an illegal rotation.
        pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);

        return new float[]{yaw, pitch};
    }
    
}
