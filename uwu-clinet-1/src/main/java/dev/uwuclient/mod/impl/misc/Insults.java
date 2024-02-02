package dev.uwuclient.mod.impl.misc;

import org.apache.commons.lang3.RandomUtils;

import dev.uwuclient.UwUClient;
import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.EventAttack;
import dev.uwuclient.event.impl.PreMotionEvent;
import dev.uwuclient.mod.base.Mod;
import dev.uwuclient.mod.base.setting.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Insults extends Mod{
    public Insults(){
        super("Insults", Category.Misc);
    }

    private EntityPlayer target;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventAttack){
            final Entity entity = ((EventAttack)e).getTarget();

            if (entity instanceof EntityPlayer)
                target = (EntityPlayer) entity;
        }
        
        if(e instanceof PreMotionEvent){
            if (target != null) {
                if (!mc.theWorld.playerEntities.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 900) {
                    if (mc.thePlayer.ticksExisted > 20) {
                                mc.thePlayer.sendChatMessage(insults[RandomUtils.nextInt(0, insults.length)].replaceAll(":user:", target.getName()));
                    }
                    target = null;
                }
            }
        }
    }

    private final String[] insults = {
                    "Did :user: forget to left click?",
            ":user: takes up 2 seats on the bus",
            "It is impossible to miss :user: with their man boobs",
            "Come on :user:, report me to the obese staff",
            ":user: is the type to overdose on Benadryl for a Tiktok video",
            "No wonder :user: dropped out of college",
            "Here's your ticket to spectator",
            "Did :user: pay for that loss?",
            "Did :user:'s dad not come back after he wanted to buy some milk?",
            "Are you afraid of me",
            "Why not use UwU client?",
            "#SwitchToUwUClient" + UwUClient.version,
            ":user: said they would never give me up and never let me down, I am sad",
            ":user: became Transgender just to join the 50% a day later",
                        ":user: is the type of person who would brute force interpolation",
            ":user: go drown in your own salt",
            ":user: is literally heavier than Overflow",
            "Excuse me :user:, I don't speak retard",
            "Hmm, the problem :user:'s having looks like a skin color issue",
            ":user: I swear I'm on Lunar Client",
            "Hey! Wise up :user:! Don't waste your time without UwU client",
            ":user: didn't even stand a chance",
            "If opposites attract I hope :user: finds someone who is intelligent, honest and cultured",
            "If laughter is the best medicine, :user:'s face must be curing the world",
            ":user: is the type of person to climb over a glass wall to see what's on the other side",
            "What does :user:'s IQ and their girlfriend have in common? They're both below 5."
            "Drink hand sanitizer so we can get rid of :user:",
            "Even the MC Virgins are less virgin than :user:",
            ":user:'s free trial of life has expired",
            ":user: is socially awkward",
            "I bet :user: believes in the flat earth",
            ":user: is the reason why society is failing",
            "Free to lose",
            "Why would I be cheating when I am recording?",
            ":user: is such a that degenerate :user: believes EQ has more value than IQ",
            "The air could've took :user: away because of how weak :user: is",
            "Even Kurt Cobain is more alive than :user: with his wounds from a shotgun and heroin in his veins",
            ":user: is breaking down more than Nirvana after Kurt Cobain's death",
            "Does :user: buy their groceries at the dollar store?",
            "Does :user: need some pvp advice?",
            "I'd smack :user:, but that would be animal abuse",
            "I don't cheat, :user: just needs to click faster",
            "Welcome to my rxpe dungeon! population: :user:!",
            ":user: pressed the wrong button when they installed Minecraft?",
            "If the body is 70% water than how is :user:'s body 100% salt?",
            "UwU client " + UwUClient.version + " is sexier than :user:",
            "Oh, :user: is recording? Well I am too",
    };
}
