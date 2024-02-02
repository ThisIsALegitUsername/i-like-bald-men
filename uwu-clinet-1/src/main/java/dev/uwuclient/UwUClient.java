package dev.uwuclient;

import dev.uwuclient.mod.base.ModManager;
import dev.uwuclient.visual.HUD;
import dev.uwuclient.visual.font.CustomFontRenderer;
import dev.uwuclient.visual.notifications.NotificationManager;

public class UwUClient {

//TODO: fix badpackets (nethanderlplayclient)
//TODO: fix font thignhgi
//TODO: un-fuck autoclicker
//TODO: circle sex in aura
//TODO: kill myself
//TODO: remove some gui's
//TODO: esp un-fuck
//TODO: fix strafe low priority

//TODO: wokr on Winter clinetetttt seks arch bypass
//TODO: i watn tof uck jolin

    static {
        INSTANCE = new UwUClient();
    }

    public static String name = "UwU", version = "b2";
    public static UwUClient INSTANCE;
    //efipuuubrhejnlllfvihuohbrvihuobhjnsdblkoipshibwekjnfhp
    public static CustomFontRenderer customFontRenderer;
    // TODO: un fuck this |
    //                    V
                public HUD hud;
    public ModManager modManager;
    //TODO: also unf-uck this |
    //                         V
    public NotificationManager notificationManager;

    public long startTime = System.currentTimeMillis();

    public void init(){
        hud = new HUD();
        notificationManager = new NotificationManager();
        modManager = new ModManager();
        customFontRenderer = new CustomFontRenderer();
    }
    
    //i want to fucking shoot myself
}
