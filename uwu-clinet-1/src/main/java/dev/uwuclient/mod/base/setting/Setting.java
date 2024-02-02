package dev.uwuclient.mod.base.setting;

public class Setting {

    public String name;
    public String description;
    public boolean focused, hidden;

    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void draw(int x, int y) {}
    public void onChange(){ }
}
