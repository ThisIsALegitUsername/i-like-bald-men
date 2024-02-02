package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventStrafe;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.MoveUtil;
import dev.uwuclient.util.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetStrafe extends Mod{

    private Entity target;
    private float distance;
    private boolean direction, reset;

    public final NumberSetting range = new NumberSetting("Range", "", 3, 1, 6, 0.1f);
    public final BooleanSetting thirdPerson = new BooleanSetting("Third Person", "", false);
    public final BooleanSetting jumpOnly = new BooleanSetting("Jump Only", "", false);

    @Override
    public void onDisable() {
        EntityPlayer.movementYaw = null;
    }

    public TargetStrafe(){
        super("TargetStrafe", Category.Combat);
        addSetting(range, thirdPerson, jumpOnly);
    }
    
    @Override
    public void onEvent(Event e) {
        if(e instanceof EventStrafe){
            if (!mc.gameSettings.keyBindJump.isKeyDown() && jumpOnly.getValue()) {
                EntityPlayer.movementYaw = null;
                return;
            }
    
            final float range = (float) this.range.getValue();
            target = Aura.target;
            if (target == null) {
                if (reset) {
                    mc.gameSettings.thirdPersonView = 0;
                    reset = false;
                }
                EntityPlayer.movementYaw = null;
                return;
            }
    
            final float yaw = getYaw();
    
            distance = mc.thePlayer.getDistanceToEntity(target);
    
            if (thirdPerson.getValue()) {
                mc.gameSettings.thirdPersonView = 1;
                reset = true;
            }
    
            final double moveDirection = MoveUtil.getDirection(EntityPlayer.movementYaw == null ? yaw : EntityPlayer.movementYaw);
            final double posX = -Math.sin(moveDirection) * MoveUtil.getSpeed() * 5;
            final double posZ = Math.cos(moveDirection) * MoveUtil.getSpeed() * 5;
            if (!(PlayerUtil.getBlockRelativeToPlayer(posX, 0, posZ) instanceof BlockAir) || PlayerUtil.getBlockRelativeToPlayer(posX, -1, posZ) instanceof BlockAir) {
                direction = !direction;
            }
    
            if (distance > range) {
                EntityPlayer.movementYaw = yaw;
            } else {
                if (direction) {
                    EntityPlayer.movementYaw = yaw + 78 + (distance - range) * 2;
                } else {
                    EntityPlayer.movementYaw = yaw - 78 - (distance - range) * 2;
                }
            }
        }
    }

    private float getYaw() {
        final double x = (target.posX - (target.lastTickPosX - target.posX)) - mc.thePlayer.posX;
        final double z = (target.posZ - (target.lastTickPosZ - target.posZ)) - mc.thePlayer.posZ;

        return (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0F);
    }
}
