package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.MoveButtonEvent;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.BooleanSetting;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;

public class Eagle extends Mod {

    public Eagle() {
        super("Legit scaffold", Category.Player, 0);
        addSetting(slow, groundOnly, blocksOnly, backwardsOnly, onlyOnSneak);
    }

    private final NumberSetting slow = new NumberSetting("Sneak speed multiplier", "", 0.3f, 0.2f, 1, 0.05f);
    private final BooleanSetting groundOnly = new BooleanSetting("Only on ground", "", false);
    private final BooleanSetting blocksOnly = new BooleanSetting("Only when holding blocks", "", false);
    private final BooleanSetting backwardsOnly = new BooleanSetting("Only when moving backwards", "", false);
    private final BooleanSetting onlyOnSneak = new BooleanSetting("Only on Sneak", "", true);

    private boolean sneaked;
    private int ticksOverEdge;


    @Override
    public void onEvent(Event e) {
        if (e instanceof PreMotionEvent) {

            if (mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) &&
                blocksOnly.getValue()) {
                if (sneaked) {
                    sneaked = false;
                }
                return;
            }

            if ((mc.thePlayer.onGround || !groundOnly.getValue()) &&
                (PlayerUtil.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) &&
                (!mc.gameSettings.keyBindForward.isKeyDown() || !backwardsOnly.getValue())) {
                if (!sneaked) {
                    sneaked = true;
                }
            } else if (sneaked) {
                sneaked = false;
            }

            if (sneaked) {
                mc.gameSettings.keyBindSprint.pressed = false;
            }

            if (sneaked) {
                ticksOverEdge++;
            } else {
                ticksOverEdge = 0;
            }
        }

        if (e instanceof MoveButtonEvent) {
            MoveButtonEvent event = ((MoveButtonEvent) e);
            event.setSneak((sneaked && (mc.gameSettings.keyBindSneak.isKeyDown() || !onlyOnSneak.getValue())) ||
                (mc.gameSettings.keyBindSneak.isKeyDown() && !onlyOnSneak.getValue()));

            if (sneaked && ticksOverEdge <= 2) {
                event.setSneakSlowDownMultiplier(slow.getValue());
            }
        }
    }


    @Override
    public void onDisable() {
        if (sneaked) {
            sneaked = false;
        }
    }

}