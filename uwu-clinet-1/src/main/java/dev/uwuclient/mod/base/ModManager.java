package dev.uwuclient.mod.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.mod.base.setting.ModeSetting;
import dev.uwuclient.mod.components.BadPacketsComponent;
import dev.uwuclient.mod.components.GUIDetectionComponent;
import dev.uwuclient.mod.components.PingSpoofComponent;
import dev.uwuclient.mod.impl.combat.Aura;
import dev.uwuclient.mod.impl.combat.AutoClicker;
import dev.uwuclient.mod.impl.combat.AutoPot;
import dev.uwuclient.mod.impl.combat.ComboOneHit;
import dev.uwuclient.mod.impl.combat.FixedAimAssist;
import dev.uwuclient.mod.impl.combat.LegitReach;
import dev.uwuclient.mod.impl.combat.Reach;
import dev.uwuclient.mod.impl.combat.TargetStrafe;
import dev.uwuclient.mod.impl.combat.Velocity;
import dev.uwuclient.mod.impl.combat.WTap;
import dev.uwuclient.mod.impl.misc.AntiBot;
import dev.uwuclient.mod.impl.misc.Crasher;
import dev.uwuclient.mod.impl.misc.Disabler;
import dev.uwuclient.mod.impl.misc.Freecam;
import dev.uwuclient.mod.impl.misc.Insults;
import dev.uwuclient.mod.impl.misc.InvMove;
import dev.uwuclient.mod.impl.misc.NoRot;
import dev.uwuclient.mod.impl.misc.PingSpoof;
import dev.uwuclient.mod.impl.player.AntiVoid;
import dev.uwuclient.mod.impl.player.AutoBuild;
import dev.uwuclient.mod.impl.player.AutoTool;
import dev.uwuclient.mod.impl.player.Blink;
import dev.uwuclient.mod.impl.player.Eagle;
import dev.uwuclient.mod.impl.player.Flight;
import dev.uwuclient.mod.impl.player.InventorySync;
import dev.uwuclient.mod.impl.player.KeepSprint;
import dev.uwuclient.mod.impl.player.Manager;
import dev.uwuclient.mod.impl.player.No003;
import dev.uwuclient.mod.impl.player.NoFall;
import dev.uwuclient.mod.impl.player.NoSlow;
import dev.uwuclient.mod.impl.player.SafeWalk;
import dev.uwuclient.mod.impl.player.Scaffold;
import dev.uwuclient.mod.impl.player.Sprint;
import dev.uwuclient.mod.impl.player.Stealer;
import dev.uwuclient.mod.impl.player.Strafe;
import dev.uwuclient.mod.impl.render.Animations;
import dev.uwuclient.mod.impl.render.Arraylist;
import dev.uwuclient.mod.impl.render.ClickGuiMod;
import dev.uwuclient.mod.impl.render.Esp;
import dev.uwuclient.mod.impl.render.FullBright;
import dev.uwuclient.mod.impl.render.Interface;
import dev.uwuclient.mod.impl.render.TargetHUD;
import dev.uwuclient.visual.clickguis.ClientGui;
import net.minecraft.client.Minecraft;

public class ModManager {

    public ArrayList<Mod> modules = new ArrayList<>();
    public ArrayList<Component> components = new ArrayList<>();

    public static Sprint sprint = new Sprint();
    public static Reach reach = new Reach();
    public static FullBright fullBright = new FullBright();
    public static FixedAimAssist fixedAimAssist = new FixedAimAssist();
    public static AntiBot antiBot = new AntiBot();
    public static Velocity velocity = new Velocity();
    public static AutoClicker autoClicker = new AutoClicker();
    public static WTap wTap =new WTap();
    public static Esp esp = new Esp();
    public static Scaffold scaffold = new Scaffold();
    public static TargetStrafe targetStrafe = new TargetStrafe();
    public static Aura aura = new Aura();
    public static Disabler disabler = new Disabler();
    public static Animations animations = new Animations();
    public static Blink blink = new Blink();
    public static NoRot noRot = new NoRot();
    public static NoSlow noSlow = new NoSlow();
    public static Eagle legitScaffold = new Eagle();
    public static InventorySync inventorySync = new InventorySync();
    public static KeepSprint keepSprint = new KeepSprint();
    public static LegitReach legitReach = new LegitReach();
    public static PingSpoof pingSpoof = new PingSpoof();
    public static ComboOneHit comboOneHit = new ComboOneHit();
    public static Stealer stealer = new Stealer();
    public static Manager invManager = new Manager();
    public static No003 no003 = new No003();
    public static Arraylist arraylist = new Arraylist();
    public static Freecam freecam = new Freecam();
    public static TargetHUD targetHUD = new TargetHUD();
    public static AutoTool autoTool = new AutoTool();
    public static InvMove invMove = new InvMove();
    public static AutoPot autoPot = new AutoPot();
    public static Crasher crasher = new Crasher();
    public static AntiVoid antiVoid = new AntiVoid();
    public static Insults insults = new Insults();
    public static Interface interface1 = new Interface();
    public static ClickGuiMod clickGuiMod = new ClickGuiMod();
    public static SafeWalk safeWalk = new SafeWalk();
    public static Strafe strafe = new Strafe();
    public static Flight flight = new Flight();
    public static NoFall noFall = new NoFall();

    public static GUIDetectionComponent guiDetectionComponent = new GUIDetectionComponent();
    public static PingSpoofComponent pingSpoofComponent = new PingSpoofComponent();
    public static BadPacketsComponent badPacketsComponent = new BadPacketsComponent();

    public ModManager(){
        addStuff();

        this.modules.stream().forEach(m ->{
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

    public void addStuff(){
        modules.add(noFall);
        modules.add(flight);
        modules.add(strafe);
        modules.add(safeWalk);
        modules.add(clickGuiMod);
        modules.add(interface1);
        modules.add(insults);
        //modules.add(autoBuild);
        modules.add(antiVoid);
        modules.add(crasher);
        modules.add(autoPot);
        modules.add(invMove);
        modules.add(autoTool);
        modules.add(targetHUD);
        modules.add(freecam);
        modules.add(arraylist);
        modules.add(no003);
        modules.add(invManager);
        modules.add(stealer);
        modules.add(comboOneHit);
        modules.add(pingSpoof);
        modules.add(legitReach);
        modules.add(keepSprint);
        modules.add(inventorySync);
        modules.add(legitScaffold);
        modules.add(noSlow);
        modules.add(noRot);
        modules.add(blink);
        modules.add(scaffold);
        modules.add(animations);
        modules.add(disabler);
        modules.add(targetStrafe);
        modules.add(sprint);
        modules.add(reach);
        modules.add(fullBright);
        modules.add(antiBot);
        modules.add(velocity);
        modules.add(autoClicker);
        modules.add(fixedAimAssist);
        modules.add(wTap);
        modules.add(esp);
        modules.add(aura);


        components.add(guiDetectionComponent);
        components.add(badPacketsComponent);
        components.add(pingSpoofComponent);
    }
    
    public void onEvent(Event e){
        if(e instanceof PreMotionEvent){
            for(final Mod m : modules){
                if(Minecraft.getMinecraft().currentScreen instanceof ClientGui){
                    m.onUpdateAlwaysGUI();
                }
            }
        }
        for(Mod m : modules){
            if(m.isEnabled()){
                m.onEvent(e);
            }
        }
        for(Component c : components){
            c.onEvent(e);
        }
    }

    public List<Mod> modsInCategory(Category c){
        return modules.stream()
            .filter(m -> m.category.equals(c))
            .collect(Collectors.toList());
    }
    
}
