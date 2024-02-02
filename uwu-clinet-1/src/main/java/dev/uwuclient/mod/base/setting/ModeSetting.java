package dev.uwuclient.mod.base.setting;

import java.util.Arrays;
import java.util.List;

import dev.uwuclient.UwUClient;
import dev.uwuclient.mod.base.Mod;

public class ModeSetting extends Setting {

    public int index;
    public int howMany = 0;
    public List<String> modes;
    public boolean suffix;

    public ModeSetting(final String name, final String defaultMode, final String... modes) {
        super(name, "");
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(defaultMode);
    }

    public ModeSetting(final String name, final Mod parent, final String defaultMode, final String... modes) {
        super(name, "");
        parent.settings.add(this);
        parent.hasSetting = true;
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(defaultMode);
    }

    public ModeSetting asSuffix(){
        this.suffix = true;
        return this;
    }

    public String getValue() {
        return modes.get(index);
    }

    public void setValue(final String mode) {
        if (modes.contains(mode)) index = this.modes.indexOf(mode);
    }

    public boolean is(String mode) {
        return index == modes.indexOf(mode);
    }

    public void cycle(boolean forwards) {
        if (forwards) {
            if (index < modes.size() - 1) {
                index++;
            } else {
                index = 0;
            }
        }
        if (!forwards) {
            if (index > 0) {
                index--;
            } else {
                index = modes.size() - 1;
            }
        }


        UwUClient.INSTANCE.modManager.modules.stream().forEach(m ->{
            m.settings.stream().filter(ModeSetting.class::isInstance).map(t -> ((ModeSetting)t)).forEach(s -> {
                if(s.suffix){
                    m.setSuffix(s.getValue());
                }
                if(m.settings.stream().filter(ModeSetting.class::isInstance).count() == 1){
                    m.setSuffix(s.getValue());
                }
            });
        });
    }

    public void onChange(){}
}