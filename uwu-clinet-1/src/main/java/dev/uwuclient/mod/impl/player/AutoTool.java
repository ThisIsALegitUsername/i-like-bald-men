package dev.uwuclient.mod.impl.player;

import dev.uwuclient.event.Event;
import dev.uwuclient.event.impl.BlockDamageEvent;
import dev.uwuclient.event.impl.UpdateEvent;
import dev.uwuclient.mod.base.Mod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;

public class AutoTool extends Mod{
    public AutoTool(){
        super("AutoTool", Category.Player);
    }

    private int slot, lastSlot = -1;
    private int blockBreak;
    private BlockPos blockPos;

    @Override
    public void onEvent(Event e) {
        if(e instanceof BlockDamageEvent){
            blockBreak = 3;
            blockPos = ((BlockDamageEvent)e).getBlockPos();
        }

        if(e instanceof UpdateEvent){
            switch (mc.objectMouseOver.typeOfHit) {
                case BLOCK:
                    if (blockPos != null && blockBreak > 0) {
                        slot = findTool(blockPos);
                    } else {
                        slot = -1;
                    }
                    break;
    
                case ENTITY:
                    slot = findSword();
                    break;
    
                default:
                    slot = -1;
                    break;
            }
    
            if (lastSlot != -1) {
                setSlot(lastSlot);
            } else if (slot != -1) {
                setSlot(slot);
            }
    
            lastSlot = slot;
            blockBreak--;
        }
    }

    public static void setSlot(final int slot) {
        if (slot < 0 || slot > 8) {
            return;
        }

        Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
    }

    public int findSword() {
        int bestDurability = -1;
        float bestDamage = -1;
        int bestSlot = -1;

        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            if (itemStack.getItem() instanceof ItemSword) {
                final ItemSword sword = (ItemSword) itemStack.getItem();

                final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                final float damage = sword.getDamageVsEntity() + sharpnessLevel * 1.25F;
                final int durability = sword.getMaxDamage();

                if (bestDamage < damage) {
                    bestDamage = damage;
                    bestDurability = durability;
                    bestSlot = i;
                }

                if (damage == bestDamage && durability > bestDurability) {
                    bestDurability = durability;
                    bestSlot = i;
                }
            }
        }

        return bestSlot;
    }

    public int findTool(final BlockPos blockPos) {
        float bestSpeed = 1;
        int bestSlot = -1;

        final IBlockState blockState = mc.theWorld.getBlockState(blockPos);

        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            final float speed = itemStack.getStrVsBlock(blockState.getBlock());

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }
    
}
