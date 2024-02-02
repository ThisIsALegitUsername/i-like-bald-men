package dev.uwuclient.mod.base.setting;

import dev.uwuclient.mod.base.Mod;

public class BooleanSetting extends Setting {

    public boolean value;

    public BooleanSetting(String name, String description, boolean value) {
        super(name, description);
        this.value = value;
    }

    public BooleanSetting(String name, Mod parent, boolean value) {
        super(name, "");
        parent.settings.add(this);
        parent.hasSetting = true;
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }
}