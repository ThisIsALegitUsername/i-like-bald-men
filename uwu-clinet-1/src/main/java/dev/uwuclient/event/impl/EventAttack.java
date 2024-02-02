package dev.uwuclient.event.impl;

import dev.uwuclient.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event{
    public Entity target;

    public EventAttack(Entity target){
        this.target = target;
    }

    public Entity getTarget(){
        return this.target;
    }

    public void setTarget(Entity target){
        this.target = target;
    }
    
}
