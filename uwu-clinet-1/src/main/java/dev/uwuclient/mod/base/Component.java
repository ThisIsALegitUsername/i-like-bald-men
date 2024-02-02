package dev.uwuclient.mod.base;

import dev.uwuclient.event.Event;
import net.minecraft.client.Minecraft;

public class Component {

    public Minecraft mc = Minecraft.getMinecraft();
    public void onEvent(Event e){ }
}
