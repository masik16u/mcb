package com.masik.mythiccharms.inventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;

public class SyncedInventory extends SimpleInventory {
    private final ScreenHandler handler;

    public SyncedInventory(ScreenHandler handler, int size) {
        super(size);
        this.handler = handler;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        handler.onContentChanged(this);
    }
}