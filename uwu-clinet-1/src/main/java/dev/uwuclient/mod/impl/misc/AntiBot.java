package dev.uwuclient.mod.impl.misc;

import java.util.ArrayList;
import java.util.List;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Mod{
    public AntiBot(){
        super("AntiBot", Category.Misc, 0x00);
        addSetting(ticksExistedCheck, ticksVisibleCheck, invalidNameCheck, duplicateIDCheck, negativeIDCheck, distanceCheck, pingCheck);
    }

    public final BooleanSetting duplicateNameCheck = new BooleanSetting("Duplicate Name Check", "", false);
    public final BooleanSetting ticksExistedCheck = new BooleanSetting("Ticks Existed Check", "", false);
    public final BooleanSetting ticksVisibleCheck = new BooleanSetting("Ticks Visible Check", "", false);
    public final BooleanSetting invalidNameCheck = new BooleanSetting("Invalid Name Check", "", false);
    public final BooleanSetting duplicateIDCheck = new BooleanSetting("Duplicate ID Check", "", false);
    public final BooleanSetting negativeIDCheck = new BooleanSetting("Negative ID Check", "", false);
    public final BooleanSetting distanceCheck = new BooleanSetting("Distance Check", "", false);
    public final BooleanSetting pingCheck = new BooleanSetting("Ping Check", "", false);

    public static List<Entity> bots = new ArrayList<>();

    

    @Override
    public void onEvent(Event e) {
        if(e instanceof PreMotionEvent){
        bots.clear();

        final List<String> names = new ArrayList<>();
        final List<Integer> ids = new ArrayList<>();

        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != mc.thePlayer) {
                    if (ticksVisibleCheck.getValue()) {
                        if ((player.ticksExisted <= 160 || player.ticksVisible <= 160)) {
                            player.bot = true;

                            if (player.isInvisibleToPlayer(mc.thePlayer) || player.isInvisible())
                                player.ticksVisible = 0;
                            else
                                player.ticksVisible++;
                        } else
                            player.bot = false;
                    }

                    if (duplicateNameCheck.getValue()) {
                        final String name = player.getName();

                        if (names.contains(name))
                            player.bot = true;

                        names.add(name);
                    }

                    if (invalidNameCheck.getValue()) {
                        final String valid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";
                        final String name = player.getName();

                        for (int i = 0; i < name.length(); i++) {
                            final String c = String.valueOf(name.charAt(i));
                            if (!valid.contains(c)) {
                                player.bot = true;
                                break;
                            }
                        }
                    }

                    if (ticksExistedCheck.getValue() && player.ticksExisted <= 0)
                        player.bot = true;

                    if (duplicateIDCheck.getValue()) {
                        final int id = player.getEntityId();

                        if (ids.contains(id))
                            player.bot = true;

                        ids.add(id);
                    }

                    if (negativeIDCheck.getValue() && player.getEntityId() < 0)
                        player.bot = true;

                    if (distanceCheck.getValue()) {
                        if (mc.thePlayer.getDistanceSq(player.posX, mc.thePlayer.posY, player.posZ) > 200)
                            player.bot = false;

                        if (player.ticksExisted < 5) 
                        player.bot = true;
                    }

                    if (pingCheck.getValue()) {
                        final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());

                        if (info != null && info.getResponseTime() < 0)
                            player.bot = true;
                    }

                if (player.bot)
                    bots.add(player);
            }
        }
        }
    }

    @Override
    public void onDisable() {
        bots.clear();

        for (final Entity e : mc.theWorld.playerEntities)
            e.bot = false;
    }

    @Override
    public void onEnable() {
        bots.clear();

        for (final Entity e : mc.theWorld.playerEntities)
            e.bot = true;
    }
}
