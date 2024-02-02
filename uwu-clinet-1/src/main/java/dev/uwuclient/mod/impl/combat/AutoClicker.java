package dev.uwuclient.mod.impl.combat;

import java.util.Random;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventRender3d;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.NumberSetting;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;

public class AutoClicker extends Mod{
    public AutoClicker(){
        super("AutoClicker", Category.Combat, 0);
    }


    private final NumberSetting minCps = new NumberSetting("Min CPS", this, 10, 1, 20, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 14, 1, 20, 1);

    @Override
    public void onUpdateAlwaysGUI(){
        if(this.maxCps.getValue() < this.minCps.getValue()){
            this.maxCps.setValue(this.minCps.getValue());
        }
    }

    Random r = new Random();
    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender3d){
            if (Mouse.isButtonDown(0) && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                mc.gameSettings.keyBindAttack.setKeyPressed(true);
                return;
            }

            if (mc.currentScreen == null && !mc.thePlayer.isBlocking()) {
                if (Mouse.isButtonDown(0) && Math.random() * 50 <= minCps.getValue() + (r.nextDouble() * (maxCps.getValue() - minCps.getValue()))) {
                    sendClick(0, true);
                    sendClick(0, false);
                }
            }
        }
    }

    private void sendClick(final int button, final boolean state) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode(), state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }
    
}