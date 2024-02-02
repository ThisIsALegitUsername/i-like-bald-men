package dev.uwuclient.mod.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.uwuclient.event.Event;
import dev.uwuclient.mod.base.setting.Setting;
import net.minecraft.client.Minecraft;

public abstract class Mod {
    
    public Minecraft mc = Minecraft.getMinecraft();
    public List<Setting> settings = new ArrayList<>();
    public String name, suffix = "", description = "insert description here";
    public Category category;
    public AnimationUtils animationUtils = new AnimationUtils();
    public AnimationUtils animationUtils2 = new AnimationUtils();
    public float animX, animY;
    public int key;
    public boolean enabled = false, hasSetting = false;
    public void onEnable(){ }
    public void onDisable(){ }
    
    public void toggle() {
        this.enabled = !this.enabled;
        onChange();
    }

    private void onChange() {
        if (enabled)
            onEnable();
        else
             onDisable();
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        onChange();
    }

    public void addSetting(final Setting... settings) {
    	this.hasSetting = true;
        this.settings.addAll(Arrays.asList(settings));
    }

    public Mod(String name, Category category, int key){
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public Mod(String name, Category category){
        this.name = name;
        this.category = category;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public enum Category {
        Combat,
        Player,
        Render,
        Misc;
    }

    /* EVENTS */
    public void onEvent(Event e) { }
    public void onUpdate(){ }
    public void onUpdateAlwaysGUI(){ }

}
