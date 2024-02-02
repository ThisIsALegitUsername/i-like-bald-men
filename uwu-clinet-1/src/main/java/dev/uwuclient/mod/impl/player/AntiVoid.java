package dev.uwuclient.mod.impl.player;

import java.util.LinkedList;
import java.util.Queue;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventSendPacket;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.MoveUtil;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class AntiVoid extends Mod{
    public AntiVoid(){
        super("AntiVoid", Category.Player);
    }

    private final ModeSetting mode = new ModeSetting("Mode", this, "Flag", "Flag", "Position", "Jump", "Ground", "Matrix", "Boost", "Blink");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", this, 5, 1, 10, 0.1f);
    private final BooleanSetting voidCheck = new BooleanSetting("Void Check", this, true);
    private final BooleanSetting showNotifications = new BooleanSetting("Show Notifications", this, false);

    private Vec3 lastGround = new Vec3(0, 0, 0);
    private boolean saved;
    private boolean aBoolean;

    private static final Queue<Packet<?>> packets = new LinkedList<>();
    private Vec3 lastServerPosition = null;

    @Override
    public void onDisable() {
        saved = false;
        lastServerPosition = null;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
            PreMotionEvent event = (PreMotionEvent)e;
            if (mc.thePlayer.onGround)
            lastGround = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        final boolean isBlockUnder = isBlockUnder();

        if ((!isBlockUnder || !voidCheck.getValue()) && mc.thePlayer.fallDistance > fallDistance.getValue() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.capabilities.isFlying /* && !this.getModule(Fly.class).isEnabled() && !this.getModule(HighJump.class).isEnabled()*/) {
            if (!saved) {
                switch (mode.getValue()) {
                    case "Flag":
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition());
                        break;

                    case "Jump":
                        mc.thePlayer.jump();
                        break;

                    case "Ground":
                        event.setGround(true);
                        break;

                    case "Matrix":
                        if ((mc.thePlayer.motionY + mc.thePlayer.posY) < Math.floor(mc.thePlayer.posY)) {
                            mc.thePlayer.motionY = Math.floor(mc.thePlayer.posY) - mc.thePlayer.posY;

                            if (mc.thePlayer.motionY == 0)
                                event.setGround(true);
                        }
                        break;

                    case "Position":
                        event.setY(event.getY() + mc.thePlayer.fallDistance);
                        break;

                    case "Boost":
                        mc.thePlayer.motionY = 1;
                        mc.thePlayer.fallDistance = 0;
                        event.setGround(true);
                        break;

                    case "Blink":
                        if (lastServerPosition == null)
                            return;

                        mc.thePlayer.setPosition(lastServerPosition.xCoord, lastServerPosition.yCoord, lastServerPosition.zCoord);
                        mc.thePlayer.fallDistance = 0;
                        mc.thePlayer.motionY = 0;
                        MoveUtil.stop();

                        aBoolean = true;
                        break;

                    case "Hypixel":
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            event.setX(event.getX() + Math.max(MoveUtil.getSpeed(), 0.2 + Math.random() / 100));
                            event.setZ(event.getZ() + Math.max(MoveUtil.getSpeed(), Math.random() / 100));
                        } else {
                            event.setX(event.getX() - Math.max(MoveUtil.getSpeed(), 0.2 + Math.random() / 100));
                            event.setZ(event.getZ() - Math.max(MoveUtil.getSpeed(), Math.random() / 100));
                        }
                        break;
                }

                if (!saved) {
                    if (showNotifications.getValue())
                        UwUClient.INSTANCE.notificationManager.registerNotification("Attempted to save you from the void.");

                    saved = true;
                }
            } else
                saved = false;
            }
        }

        if(e instanceof EventSendPacket){
            EventSendPacket event = (EventSendPacket)e;
            final Packet<?> p = event.getPacket();

            switch (mode.getValue()) {
                case "Blink": {
                    if (!(p instanceof C03PacketPlayer || p instanceof C0FPacketConfirmTransaction))
                        return;
    
                    final boolean blink = mc.thePlayer.fallDistance <= 6 && !isBlockUnder() /*&& !this.getModule(Fly.class).isEnabled() && !this.getModule(HighJump.class).isEnabled()*/ && !mc.gameSettings.keyBindSneak.isKeyDown();
    
                    if (blink) {
                        if (lastServerPosition == null)
                            lastServerPosition = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    
                        event.setCancelled(true);
                        packets.add(p);
                    } else if (p instanceof C03PacketPlayer) {
                        if (!(isBlockUnder() && !aBoolean))
                            packets.removeIf(packet -> packet instanceof C03PacketPlayer);
                        packets.forEach(PacketUtil::sendPacketWithoutEvent);
                        packets.clear();
                        lastServerPosition = null;
                        aBoolean = false;
                    }
                    break;
                }
            }
        }
    }

    public static boolean isBlockUnder() {
        Minecraft mc = Minecraft.getMinecraft();
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }
    
}
