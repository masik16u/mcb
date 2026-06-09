package com.masik.mythiccharms.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;

public class AmethystBrushItem extends BrushItem {
    public AmethystBrushItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 100;
    }
}
