package com.masik.mythiccharms.inventory;

import com.masik.mythiccharms.screen.ResonanceTableScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ResonanceInputSlot extends Slot {
    private final ResonanceTableScreenHandler screenHandler;

    public ResonanceInputSlot(Inventory inventory, int index, int x, int y, ResonanceTableScreenHandler screenHandler) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.screenHandler.canPutInInputSlot(stack) &&
                super.canInsert(stack);
    }
}
