package com.masik.mythiccharms.util;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class NbtHelper {

    public static NbtCompound getNbtCompound(ItemStack itemStack, ComponentType<NbtComponent> type) {
        NbtComponent component = itemStack.get(type);
        return component != null ? component.copyNbt() : new NbtCompound();
    }

}
