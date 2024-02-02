package dev.uwuclient.mod.impl.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.teavm.jso.webgl.WebGLRenderingContext;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventAttack;
import dev.uwuclient.event.impl.EventRender3d;
import dev.uwuclient.event.impl.PostMotionEvent;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.event.impl.UpdateEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.mod.impl.misc.AntiBot;
import dev.uwuclient.util.Color;
import dev.uwuclient.util.PacketUtil;
import dev.uwuclient.util.PlayerUtil;
import dev.uwuclient.util.RotationUtil;
import dev.uwuclient.util.TimeUtil;
import dev.uwuclient.visual.clickguis.lemon.ClickGui;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public final class Aura extends Mod {
  public Aura() {
    super("Aura", Category.Combat, KeyboardConstants.KEY_K);
    addSetting(mode, rotationMode, blockMode, sortingMode, range, rotationRange, extendedRangeNumb, extendedRangeBool, minCps, maxCps, maxTargets, predict, random, maxRotation, minRotation, minYawRotation, maxYawRotation, minPitchRotation, maxPitchRotation, derpSpeed, predictedPosition, rayTrace, alwaysSwing, deadZone, throughWalls, silentRotations, keepSprint, onlyInAir, strafe, players, nonPlayers, teams, invisibles, dead, attackWithScaffold, attackInInterfaces, onClick);
  }

  private final TimeUtil timer = new TimeUtil();

  public static EntityLivingBase target;

  public static float yaw, pitch, lastYaw, lastPitch, serverYaw, serverPitch;
  private float randomYaw, randomPitch, derpYaw;
  private double targetPosX, targetPosY, targetPosZ;
  private Vec3 positionOnPlayer, lastPositionOnPlayer;
  public Random randomNum = new Random();

  private int hitTicks, cps, targetIndex;
  private boolean blocking;
  private final List < C03PacketPlayer.C04PacketPlayerPosition > packetList = new ArrayList < > ();
  private boolean targetstrafe;

  private final ModeSetting mode = new ModeSetting("Type", "Single", "Single", "Switch", "Multi").asSuffix();
  private final ModeSetting rotationMode = new ModeSetting("Rotation", "Custom", "Custom", "Custom Advanced", "Derp", "None");
  public final ModeSetting blockMode = new ModeSetting("Block", "Fake", "None", "Fake", "Vanilla", "Bypass", "NCP", "Interact");
  private final ModeSetting sortingMode = new ModeSetting("Sorting", "Distance", "Distance", "Health", "Hurttime");

  private final NumberSetting range = new NumberSetting("Range", "", 2.8F, 0, 6, 0.1F);
  private final NumberSetting rotationRange = new NumberSetting("Rotation Range", "", 6, 0, 12, 0.1F);
  private final NumberSetting extendedRangeNumb = new NumberSetting("Extended Range", "", 7, 6, 12, 0.1F);
  private final BooleanSetting extendedRangeBool = new BooleanSetting("Extended Range", "", false);
  private final NumberSetting minCps = new NumberSetting("Min CPS", "", 8.0F, 1, 20.0F, 1);
  private final NumberSetting maxCps = new NumberSetting("Max CPS", "", 13.0F, 1, 20.0F, 1);
  private final NumberSetting maxTargets = new NumberSetting("Max Targets", "", 25, 2, 50, 1);

  private final NumberSetting predict = new NumberSetting("Predict", "", 0, 0, 4, 0.1F);
  private final NumberSetting random = new NumberSetting("Random", "", 0, 0, 18, 0.1F);
  private final NumberSetting maxRotation = new NumberSetting("Max Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting minRotation = new NumberSetting("Min Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting minYawRotation = new NumberSetting("Min Yaw Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting maxYawRotation = new NumberSetting("Max Yaw Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting minPitchRotation = new NumberSetting("Min Pitch Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting maxPitchRotation = new NumberSetting("Max Pitch Rot", "", 180, 1, 180, 0.1F);
  private final NumberSetting derpSpeed = new NumberSetting("Derp Speed", "", 30, 1, 180, 1);
  private final BooleanSetting predictedPosition = new BooleanSetting("Predicted Position", "", false);
  private final BooleanSetting rayTrace = new BooleanSetting("Raytrace", "", true);
  private final BooleanSetting alwaysSwing = new BooleanSetting("Realistic Swings", "", false);
  private final BooleanSetting deadZone = new BooleanSetting("DeadZone", "", false);
  private final BooleanSetting throughWalls = new BooleanSetting("Through Walls", "", false);
  private final BooleanSetting silentRotations = new BooleanSetting("Silent Rotations", "", true);
  private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", "", false);
  private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", "", false);
  private final BooleanSetting strafe = new BooleanSetting("Movement Fix", "", true);

  private final BooleanSetting players = new BooleanSetting("Players", "", true);
  private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", "", true);
  private final BooleanSetting teams = new BooleanSetting("Ignore Teammates", "", false);
  private final BooleanSetting invisibles = new BooleanSetting("Invisibles", "", false);
  private final BooleanSetting dead = new BooleanSetting("Attack Dead", "", false);

  private final BooleanSetting attackWithScaffold = new BooleanSetting("Attack with Scaffold", "", false);
  private final BooleanSetting attackInInterfaces = new BooleanSetting("Attack in Interfaces", "", true);
  private final BooleanSetting onClick = new BooleanSetting("On Click", "", false);

  @Override
  public void onUpdateAlwaysGUI() {
    if (this.maxCps.getValue() < this.minCps.getValue()) {
      maxCps.setValue(minCps.getValue());
    }

    if (this.rotationRange.getValue() < this.range.getValue()) {
      this.rotationRange.setValue(this.range.getValue());
    }

    maxTargets.hidden = !this.mode.is("Multi");

    minRotation.hidden = this.maxRotation.hidden = !this.rotationMode.is("Custom") && !this.rotationMode.is("Custom Simple");

    minYawRotation.hidden = maxYawRotation.hidden = minPitchRotation.hidden = maxPitchRotation.hidden = !this.rotationMode.is("Custom Advanced");

    derpSpeed.hidden = !rotationMode.is("Derp");

    extendedRangeNumb.hidden = !extendedRangeBool.getValue();

    predictedPosition.hidden = rotationMode.is("Derp") || rotationMode.is("None");

    onlyInAir.hidden = !keepSprint.getValue();
  }

  @Override
  public void onEvent(Event e) {
    if (e instanceof PreMotionEvent) {
      PreMotionEvent event = ((PreMotionEvent) e);
      ++this.hitTicks;
      targetstrafe = ModManager.targetStrafe.isEnabled();

      handle: {
        if (target == null) {
          if (!targetstrafe)
            EntityPlayer.movementYaw = null;

          unblock();

          break handle;
        } else {
          switch (blockMode.getValue()) {
          case "NCP":
          case "Interact":
            unblock();
            break;
          }
        }
        if (this.silentRotations.getValue() && !rotationMode.is("None")) {
          event.setYaw(serverYaw);
          event.setPitch(serverPitch);

          mc.thePlayer.renderYawOffset = serverYaw;
          mc.thePlayer.rotationYawHead = serverYaw;
        } else {
          mc.thePlayer.rotationYaw = serverYaw;
          mc.thePlayer.rotationPitch = serverPitch;
        }

        final Vec3 rayCast = Objects.requireNonNull(PlayerUtil.getMouseOver(serverYaw, serverPitch, (float) range.getValue())).hitVec;
        if (rayCast == null) return;
        lastPositionOnPlayer = positionOnPlayer;
        positionOnPlayer = rayCast;
      }

    }
    if (e instanceof PostMotionEvent) {
      if (target != null && PlayerUtil.isHoldingSword()) {
        switch (blockMode.getValue()) {
        case "NCP":
          PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
          break;

        case "Interact":
          mc.thePlayer.inventory.getCurrentItem().useItemRightClick(mc.theWorld, mc.thePlayer);
          mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
          PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
          break;
        }
      }
    }
    if (e instanceof UpdateEvent) {
      if (!(!onClick.getValue() || Mouse.isButtonDown(0))) {
        target = null;
        return;
      }

      if (target == null) {
        if (!targetstrafe) EntityPlayer.movementYaw = null;
        unblock();

        return;
      }

      double ping = 250;
      ping /= 50;
      if (predictedPosition.getValue()) {
        final double deltaX = (target.posX - target.lastTickPosX) * 2;
        final double deltaY = (target.posY - target.lastTickPosY) * 2;
        final double deltaZ = (target.posZ - target.lastTickPosZ) * 2;
        targetPosX = target.posX + deltaX * ping;
        targetPosY = target.posY + deltaY * ping;
        targetPosZ = target.posZ + deltaZ * ping;
      } else {
        targetPosX = target.posX;
        targetPosY = target.posY;
        targetPosZ = target.posZ;
      }

      if ((ModManager.scaffold.isEnabled() && !attackWithScaffold.getValue()) ||
        ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGui)) && !attackInInterfaces.getValue())) {
        unblock();
        target = null;
        return;
      }

      serverYaw = yaw;
      serverPitch = pitch;

      if (this.strafe.getValue() && this.silentRotations.getValue()) {
        EntityPlayer.movementYaw = serverYaw;
      } else if (!targetstrafe) EntityPlayer.movementYaw = null;

      double delayValue = -1;

      boolean attack = false;

      if (this.timer.hasReached(this.cps)) {
        final int maxValue = (int)((this.minCps.getMax() - this.maxCps.getValue()) * 20);
        final int minValue = (int)((this.minCps.getMin() - this.minCps.getValue()) * 20);

        this.cps = (int)(randomBetween(minValue, maxValue) - randomNum.nextInt(10) + randomNum.nextInt(10));

        this.timer.reset();

        attack = true;
      } else if (blockMode.is("Bypass"))
        this.unblock();

      derpYaw += derpSpeed.getValue() - (((Math.random() - 0.5) * random.getValue()) / 2);

      if (attack) {
        final boolean rayCast = PlayerUtil.isMouseOver(serverYaw, serverPitch, target, (float) range.getValue()) || predictedPosition.getValue();
        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;
        final double y = mc.thePlayer.posY;
        final double endPositionX = targetPosX;
        final double endPositionZ = targetPosZ;
        double distanceX = x - endPositionX;
        double distanceZ = z - endPositionZ;
        double distanceY = y - targetPosY;
        double distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;
        if (extendedRangeBool.getValue()) {
          int packets = 0;

          while (distance > (range.getValue() - 0.5657) * 6.5 && packets < 100) {
            final C03PacketPlayer.C04PacketPlayerPosition c04 = new C03PacketPlayer.C04PacketPlayerPosition(x, mc.thePlayer.posY, z, true);

            if (mc.thePlayer.ticksExisted > 10)
              PacketUtil.sendPacket(c04);

            packetList.add(c04);

            distanceX = x - endPositionX;
            distanceZ = z - endPositionZ;
            distanceY = y - targetPosY;
            distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;

            final double v = (x * distance + endPositionX) / (distance + 1) - x;
            final double v1 = (z * distance + endPositionZ) / (distance + 1) - z;
            mc.thePlayer.addChatMessage(new ChatComponentText(MathHelper.sqrt_double(v * v + v1 * v1) + ""));

            x = (x * distance + endPositionX) / (distance + 1);
            z = (z * distance + endPositionZ) / (distance + 1);

            packets++;
          }
        }

        if ((mc.thePlayer.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeBool.getValue()) ? this.extendedRangeNumb.getValue() : this.range.getValue()) && !rayCast) ||
          (this.rayTrace.getValue() && !rayCast)) {
          if (this.alwaysSwing.getValue()) {
            PacketUtil.sendPacket(new C0APacketAnimation());
            return;
          }
        }

        if (mc.thePlayer.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeBool.getValue()) ? this.extendedRangeNumb.getValue() : this.range.getValue()) ||
          (this.rayTrace.getValue() && !rayCast)) return;

        if (!this.throughWalls.getValue() && !mc.thePlayer.canEntityBeSeen(target)) return;

        mc.thePlayer.swingItem();

        switch (this.blockMode.getValue()) {
        case "Interact": {
          this.unblock();
          break;
        }
        }

        switch (mode.getValue()) {
        case "Single": {
          final EventAttack attackEvent = new EventAttack(target);
          UwUClient.INSTANCE.modManager.onEvent(attackEvent);

          if (attackEvent.isCancelled())
            return;

          if (this.keepSprint.getValue() && (!mc.thePlayer.onGround || !onlyInAir.getValue())) {
            PacketUtil.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
          } else {
            mc.playerController.attackEntity(mc.thePlayer, target);
          }

          if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(target);
          break;
        }

        case "Switch": {
          final List < EntityLivingBase > entities = getTargets();

          if (entities.size() >= targetIndex)
            targetIndex = 0;

          if (entities.isEmpty()) {
            targetIndex = 0;
            return;
          }

          final EntityLivingBase entity = entities.get(targetIndex);

          final EventAttack attackEvent = new EventAttack(entity);
          attackEvent.call();

          if (attackEvent.isCancelled())
            return;

          if (this.keepSprint.getValue() && (!mc.thePlayer.onGround || !onlyInAir.getValue())) {
            PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
          } else {
            mc.playerController.attackEntity(mc.thePlayer, entity);
          }

          if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(target);

          targetIndex++;
          break;
        }

        case "Multi": {
          for (final EntityLivingBase entity: getTargets()) {
            final EventAttack attackEvent = new EventAttack(target);
            attackEvent.call();

            if (attackEvent.isCancelled())
              return;

            if (this.keepSprint.getValue() && (!mc.thePlayer.onGround || !onlyInAir.getValue())) {
              PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            } else {
              mc.playerController.attackEntity(mc.thePlayer, entity);
            }

            if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(entity);
          }
          break;
        }
        }

        if (extendedRangeBool.getValue()) {
          Collections.reverse(packetList);
          packetList.forEach(PacketUtil::sendPacket);
          packetList.clear();
        }

        this.hitTicks = 0;
      }

      if (PlayerUtil.isHoldingSword() && !ModManager.scaffold.isEnabled()) {
        switch (this.blockMode.getValue()) {

        case "Bypass":
        case "Vanilla": {
          PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
          break;
        }
        }
      }
    }
    if (e instanceof EventRender3d) {
      this.update();

      if (target != null) {
        drawCircle(target, 0.67, new Color(159, 24, 242).getRGB() , true);
    }
    }
  }

  @Override
  public void onEnable() {
    lastYaw = mc.thePlayer.rotationYaw;
    lastPitch = mc.thePlayer.rotationPitch;
    yaw = mc.thePlayer.rotationYaw;
    pitch = mc.thePlayer.rotationPitch;
    blocking = mc.gameSettings.keyBindUseItem.isKeyDown();
  }

  @Override
  public void onDisable() {
    if (!targetstrafe) EntityPlayer.movementYaw = null;

    targetIndex = 0;

    timer.reset();

    target = null;

    unblock();
  }

  private void update() {
    if ((ModManager.scaffold.isEnabled() && !attackWithScaffold.getValue()) ||
      ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGui)) && !attackInInterfaces.getValue())) {
      unblock();
      target = null;
      return;
    }

    this.updateTarget();

    if (target == null) {
      lastYaw = mc.thePlayer.rotationYaw;
      lastPitch = mc.thePlayer.rotationPitch;
    } else {
      this.updateRotations();
    }
  }

  private void block() {
    sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
    mc.gameSettings.keyBindUseItem.pressed = true;
    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
    blocking = true;
  }

  private void unblock() {
    if (blocking) {
      mc.gameSettings.keyBindUseItem.pressed = false;
      mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      blocking = false;
    }
  }

  public void sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
    if (!(mc.playerController.currentGameType == WorldSettings.GameType.SPECTATOR)) {
      mc.playerController.syncCurrentPlayItem();
      int i = itemStackIn.stackSize;
      ItemStack itemstack = itemStackIn.useItemRightClick(worldIn,
        playerIn);

      if (itemstack != itemStackIn || itemstack.stackSize != i) {
        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;

        if (itemstack.stackSize == 0) {
          playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
        }

      }
    }
  }

  private void updateRotations() {
    lastYaw = yaw;
    lastPitch = pitch;

    final float[] rotations = this.getRotations();

    yaw = rotations[0];
    pitch = rotations[1];

    if (deadZone.getValue()) {
      if (rayTrace(lastYaw, lastPitch, rotationRange.getValue(), target)) {
        yaw = lastYaw;
        pitch = lastPitch;
      }
    }
  }

  private float[] getRotations() {
    final double predictValue = predict.getValue();

    final double x = (targetPosX - (target.lastTickPosX - targetPosX) * predictValue) + 0.01 - mc.thePlayer.posX;
    final double z = (targetPosZ - (target.lastTickPosZ - targetPosZ) * predictValue) - mc.thePlayer.posZ;

    double minus = (mc.thePlayer.posY - targetPosY);

    if (minus < -1.4) minus = -1.4;
    if (minus > 0.1) minus = 0.1;

    final double y = (targetPosY - (target.lastTickPosY - targetPosY) * predictValue) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;

    final double xzSqrt = MathHelper.sqrt_double(x * x + z * z);

    float yaw = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(z, x)) - 90.0F);
    float pitch = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(-Math.atan2(y, xzSqrt)));

    final double randomAmount = random.getValue();

    if (randomAmount != 0) {
      randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
      randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
      randomPitch += ((Math.random() - 0.5) * randomAmount) / 2;

      if (mc.thePlayer.ticksExisted % 5 == 0) {
        randomYaw = (float)(((Math.random() - 0.5) * randomAmount) / 2);
        randomPitch = (float)(((Math.random() - 0.5) * randomAmount) / 2);
      }

      yaw += randomYaw;
      pitch += randomPitch;
    }

    final int fps = (int)(Minecraft.getDebugFPS() / 20.0F);

    switch (this.rotationMode.getValue()) {
    case "Custom": {
      if (this.maxRotation.getValue() != 180.0F && this.minRotation.getValue() != 180.0F) {
        final float distance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());

        final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
        final float deltaPitch = pitch - lastPitch;

        final float distanceYaw = MathHelper.clamp_float(deltaYaw, -distance, distance) / fps * 4;
        final float distancePitch = MathHelper.clamp_float(deltaPitch, -distance, distance) / fps * 4;

        yaw = MathHelper.wrapAngleTo180_float(lastYaw) + distanceYaw;
        pitch = MathHelper.wrapAngleTo180_float(lastPitch) + distancePitch;
      }
      break;
    }

    case "Custom Simple": {
      final float yawDistance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());
      final float pitchDistance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());

      final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
      final float deltaPitch = pitch - lastPitch;

      final float distanceYaw = MathHelper.clamp_float(deltaYaw, -yawDistance, yawDistance) / fps * 4;
      final float distancePitch = MathHelper.clamp_float(deltaPitch, -pitchDistance, pitchDistance) / fps * 4;

      yaw = lastYaw + distanceYaw;
      pitch = lastPitch + distancePitch;
      break;
    }

    case "Custom Advanced": {
      final float advancedYawDistance = (float) randomBetween(this.minYawRotation.getValue(), this.maxYawRotation.getValue());
      final float advancedPitchDistance = (float) randomBetween(this.minPitchRotation.getValue(), this.maxPitchRotation.getValue());

      final float advancedDeltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
      final float advancedDeltaPitch = pitch - lastPitch;

      final float advancedDistanceYaw = MathHelper.clamp_float(advancedDeltaYaw, -advancedYawDistance, advancedYawDistance) / fps * 4;
      final float advancedDistancePitch = MathHelper.clamp_float(advancedDeltaPitch, -advancedPitchDistance, advancedPitchDistance) / fps * 4;

      yaw = lastYaw + advancedDistanceYaw;
      pitch = lastPitch + advancedDistancePitch;
      break;
    }

    case "Smooth": {
      final float yawDelta = (float)(((((yaw - lastYaw) + 540) % 360) - 180) / (fps / 3 * (1 + Math.random())));
      final float pitchDelta = (float)((pitch - lastPitch) / (fps / 3 * (1 + Math.random())));

      yaw = lastYaw + yawDelta;
      pitch = lastPitch + pitchDelta;

      break;
    }

    case "Down": {
      pitch = RandomUtils.nextFloat(89, 90);
      break;
    }

    case "Derp": {
      pitch = RandomUtils.nextFloat(89, 90);
      yaw = derpYaw;
      break;
    }

    }

    final float[] rotations = new float[] {
      yaw,
      pitch
    };
    final float[] lastRotations = new float[] {
      Aura.yaw, Aura.pitch
    };

    final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

    yaw = fixedRotations[0];
    pitch = fixedRotations[1];

    if (this.rotationMode.is("None")) {
      yaw = mc.thePlayer.rotationYaw;
      pitch = mc.thePlayer.rotationPitch;
    }

    pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);

    return new float[] {
      yaw,
      pitch
    };
  }

  private List < EntityLivingBase > getTargets() {
    final List < EntityLivingBase > entities = mc.theWorld.loadedEntityList
      .stream()

      .filter(entity -> entity instanceof EntityLivingBase)

      .map(entity -> ((EntityLivingBase) entity))

      .filter(entity -> {
        if (entity instanceof EntityPlayer && !players.getValue()) return false;

        if (!(entity instanceof EntityPlayer) && !nonPlayers.getValue()) return false;

        if (entity.isInvisible() && !invisibles.getValue()) return false;

        if (PlayerUtil.isOnSameTeam(entity) && teams.getValue()) return false;

        if (entity.isDead && !dead.getValue()) return false;

        if (entity.deathTime != 0 && !dead.getValue()) return false;

        if (entity.ticksExisted < 2) return false;

        if (AntiBot.bots.contains(entity)) return false;

        if (entity instanceof EntityPlayer) {
          final EntityPlayer player = ((EntityPlayer) entity);
        }

        return mc.thePlayer != entity;
      })

      .filter(entity -> {
        final double girth = 0.5657;

        return mc.thePlayer.getDistanceToEntity(entity) - girth < rotationRange.getValue();
      })

      .sorted(Comparator.comparingDouble(entity -> {
        switch (sortingMode.getValue()) {
        case "Distance":
          return mc.thePlayer.getDistanceSqToEntity(entity);
        case "Health":
          return entity.getHealth();
        case "Hurttime":
          return entity.hurtTime;

        default:
          return -1;
        }
      }))

      .sorted(Comparator.comparing(entity -> entity instanceof EntityPlayer))

      .collect(Collectors.toList());

    final int maxTargets = (int) Math.round(this.maxTargets.getValue());

    if (mode.is("Multi") && entities.size() > maxTargets) {
      entities.subList(maxTargets, entities.size()).clear();
    }

    return entities;
  }

  private void updateTarget() {
    final List < EntityLivingBase > entities = getTargets();

    target = entities.size() > 0 ? entities.get(0) : null;
  }

  public double randomBetween(final double min, final double max) {
    return min + (randomNum.nextDouble() * (max - min));
  }

  private boolean rayTrace(final float yaw, final float pitch, final double reach, final Entity target) {
    final Vec3 vec3 = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
    final Vec3 vec31 = mc.thePlayer.getVectorForRotation(MathHelper.clamp_float(pitch, -90.F, 90.F), yaw % 360);
    final Vec3 vec32 = vec3.addVector(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach);

    final MovingObjectPosition objectPosition = target.getEntityBoundingBox().calculateIntercept(vec3, vec32);

    return (objectPosition != null && objectPosition.hitVec != null);
  }

/*private void drawCircle(final Entity entity, final double rad) {
  Tessellator tessellator = Tessellator.getInstance();
  WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.disableTexture2D();
    EaglercraftGPU.glLineWidth(1);
    worldrenderer.begin(5, DefaultVertexFormats.POSITION_TEX);

    final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
    final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY) + mc.thePlayer.getEyeHeight() - 0.7;
    final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

    Color c =  new Color(159, 24, 242);
    for (int i = 0; i <= 90; ++i) {
        GlStateManager.color(c.getRed()/255, c.getGreen()/255, c.getBlue()/255);

        worldrenderer.pos(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0)).endVertex();;
    }

    tessellator.draw();
    GlStateManager.enableDepth();
    GlStateManager.enableTexture2D();
    GlStateManager.resetColor();
}

  private void drawCircle(final Entity entity, final double rad, final int color, final boolean shade) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    WebGLRenderingContext ctx =  PlatformRuntime.webgl;

    GlStateManager.pushMatrix();
    GlStateManager.disableTexture2D();
    ctx.enable(ctx.LINES);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    GlStateManager.disableDepth();
    GlStateManager.depthMask(false);
    GlStateManager.alphaFunc(RealOpenGLEnums.GL_GREATER, 0.0F);
    if (shade)
      GlStateManager.shadeModel(RealOpenGLEnums.GL_SMOOTH);
    GlStateManager.disableCull();
    worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);

    final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
    final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY) + Math.sin(System.currentTimeMillis() / 2E+2) + 1;
    final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;

    Color c;

    for (float i = 0; i < Math.PI * 2; i += Math.PI * 2 / 64.F) {
      final double vecX = x + rad * Math.cos(i);
      final double vecZ = z + rad * Math.sin(i);

      c = new Color(159, 24, 242);

      if (shade) {
        GlStateManager.color(c.getRed() / 255.F,
          c.getGreen() / 255.F,
          c.getBlue() / 255.F,
          0
        );
        worldrenderer.pos(vecX, y - Math.cos(System.currentTimeMillis() / 2E+2) / 2.0F, vecZ);
        GlStateManager.color(c.getRed() / 255.F,
          c.getGreen() / 255.F,
          c.getBlue() / 255.F,
          0.85F
        );
      }
      worldrenderer.pos(vecX, y, vecZ);
    }

    tessellator.draw();
    if (shade) GlStateManager.shadeModel(RealOpenGLEnums.GL_FLAT);
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.alphaFunc(RealOpenGLEnums.GL_GREATER, 0.1F);
    GlStateManager.enableCull();
    ctx.disable(ctx.LINES);
    GlStateManager.enableTexture2D();
    GlStateManager.popMatrix();
    GlStateManager.color(255, 255, 255);
  }

  private Color color;
  public Color getThemeColor() {
    if (timer.hasReached(50 * 5)) {
      timer.reset();
      color = new Color(159, 24, 242);
    }
    return color;
  }*/
}