package dev.uwuclient.mod.impl.combat;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventAttack;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.NumberSetting;
import dev.uwuclient.util.PacketUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class ComboOneHit extends Mod{

    private final NumberSetting packets = new NumberSetting("Packets", "", 50, 1, 1000, 25);

    public ComboOneHit(){
        super("Combo one hit", Category.Combat);
        addSetting(packets);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventAttack){
            for(int i = 0; i < packets.getValue(); i++){
                PacketUtil.sendPacket(new C02PacketUseEntity(((EventAttack)e).getTarget(), C02PacketUseEntity.Action.ATTACK));
            }
        }
    }
    
}
