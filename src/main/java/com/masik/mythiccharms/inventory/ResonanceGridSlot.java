package com.masik.mythiccharms.inventory;

import com.masik.mythiccharms.screen.ResonanceTableScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ResonanceGridSlot extends Slot {
    private final ResonanceTableScreenHandler screenHandler;

    public ResonanceGridSlot(Inventory inventory, int index, int x, int y, ResonanceTableScreenHandler screenHandler) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.screenHandler.isSlotEnabled(this.id) &&
                this.screenHandler.canPutInGridSlot(stack, this.id) &&
                super.canInsert(stack);
    }
}
