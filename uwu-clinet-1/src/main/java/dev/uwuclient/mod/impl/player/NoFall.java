package dev.uwuclient.mod.impl.player;

import org.apache.commons.lang3.RandomUtils;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.util.MoveUtil;
import dev.uwuclient.util.PacketUtil;
import dev.uwuclient.util.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class NoFall extends Mod {

    public NoFall(){
        super("NoFall", Category.Player);
    }
    private final ModeSetting mode = new ModeSetting("Mode", this, "Ground Spoof",
            "Ground Spoof", "No Ground", "Tick", "Packet", "Collision", "Collision Silent",
            "Matrix", "Verus", "Math Ground", "Less Fall", "Vulcan");

    private final BooleanSetting offset = new BooleanSetting("Offset", this, true);
    private float lastTickFallDist, fallDist;
    private int offGroundTicks, tick;
    private boolean bool;

    @Override
    public void onEnable() {
        tick = 1;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            PreMotionEvent event = (PreMotionEvent)e;
            if (mc.thePlayer.onGround)
            offGroundTicks = 0;
        else
            offGroundTicks++;

        if (mc.thePlayer.fallDistance == 0)
            fallDist = 0;

        fallDist += mc.thePlayer.fallDistance - lastTickFallDist;
        lastTickFallDist = mc.thePlayer.fallDistance;

        final boolean isBlockUnder = PlayerUtil.isBlockUnder();
        if (/*this.getModule(HighJump.class).isEnabled() ||*/ !isBlockUnder) return;

        switch (mode.getValue()) {
            case "Ground Spoof":
                if (fallDist > 2) {
                    event.setGround(true);
                    fallDist = 0;
                }
                break;

            case "Packet":
                if (fallDist > 2) {
                    PacketUtil.sendPacket(new C03PacketPlayer(true));
                    fallDist = 0;
                }
                break;

            case "Tick":
                if (fallDist > 2 && mc.thePlayer.ticksExisted % 3 == 0) {
                    event.setGround(true);
                    fallDist = 0;
                }
                break;

            case "Vulcan":
                double mathGround = Math.round(event.getY() / 0.015625) * 0.015625;

                if (fallDist > 1.3 && mc.thePlayer.ticksExisted % 15 == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mathGround, mc.thePlayer.posZ);
                    event.setY(mathGround);

                    mathGround = Math.round(event.getY() / 0.015625) * 0.015625;
                    if (Math.abs(mathGround - event.getY()) < 0.01) {
                        if (mc.thePlayer.motionY < -0.4) mc.thePlayer.motionY = -0.4;

                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        mc.timer.timerSpeed = 0.9f;
                    }
                } else if (mc.timer.timerSpeed == 0.9f) {
                    mc.timer.timerSpeed = 1;
                }
                break;

            case "Collision":
                if (fallDist > 3) {
                    mc.thePlayer.motionY = -(mc.thePlayer.posY - (mc.thePlayer.posY - (mc.thePlayer.posY % (1.0 / 64.0))));
                    event.setGround(true);

                    fallDist = 0;
                }
                break;

            case "Collision Silent":
                if (fallDist > 2) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(
                            (mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2,
                            (mc.thePlayer.posY - (mc.thePlayer.posY % (1 / 64.0))),
                            (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.rotationPitch,
                            true)
                    );
                    fallDist = 0;
                }
                break;

            case "Matrix":
                event.setGround(false);

                if (!MoveUtil.isMoving())
                    offGroundTicks = 0;

                if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    MoveUtil.strafe(0.22);
                }

                if (!MoveUtil.isMoving() && mc.thePlayer.onGround) {
                    mc.thePlayer.motionX *= 0.6;
                    mc.thePlayer.motionZ *= 0.6;
                }

                if (MoveUtil.getSpeed() < 0.1)
                    MoveUtil.strafe(MoveUtil.getSpeed() * 1.7);

                if (mc.thePlayer.onGround)
                    MoveUtil.strafe();

                mc.thePlayer.onGround = false;
                break;

            case "Verus":
                if (fallDist > 3 && fallDist < 12) {
                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5)
                        event.setGround(true);
                }
                break;

            case "Artemis 2":
                if (mc.thePlayer.ticksExisted % 8 == 0 && fallDist > 2)
                    event.setGround(true);
                break;

            case "No Ground":
                if (mc.thePlayer.onGround && offset.getValue())
                    event.setY(event.getY() + RandomUtils.nextDouble(0.0001, 0.001));
                event.setGround(false);
                break;

            case "Hypixel":
                if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance - (tick * 2.8D) >= 0.0D) {
                    if (mc.thePlayer.ticksExisted > 150)
                        event.setGround(true);
                    tick++;
                } else if (mc.thePlayer.onGround) {
                    tick = 1;
                }
                break;

            case "Math Ground":
                if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5)
                    event.setGround(true);
                break;

            case "Less Fall":
                if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5 && bool) {
                    event.setGround(true);
                    bool = false;
                }

                if (mc.thePlayer.onGround)
                    bool = true;
                break;
        }
        }
    }
}