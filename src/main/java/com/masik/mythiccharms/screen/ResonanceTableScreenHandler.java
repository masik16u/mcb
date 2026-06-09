package com.masik.mythiccharms.screen;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.block.ModBlocks;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceConstraintsComponent;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.inventory.ResonanceGridSlot;
import com.masik.mythiccharms.inventory.ResonanceInputSlot;
import com.masik.mythiccharms.inventory.SyncedInventory;
import com.masik.mythiccharms.item.ModItems;
import com.masik.mythiccharms.item.PuzzleBoxItem;
import com.masik.mythiccharms.util.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResonanceTableScreenHandler extends ScreenHandler {
    private final Inventory input;
    private final Inventory grid;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private final PlayerInventory playerInventory;
    private final Inventory gridBeforeClear;
    private ItemStack lastInputStack = ItemStack.EMPTY;

    public ResonanceTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ResonanceTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.RESONANCE_TABLE, syncId);
        this.input = new SyncedInventory(this, 1);
        this.grid = new SyncedInventory(this, 25);
        this.gridBeforeClear = new SyncedInventory(this, 25);
        this.context = context;
        this.player = playerInventory.player;
        this.playerInventory = playerInventory;

        // Add one input slot - index 0
        this.addSlot(new ResonanceInputSlot(this.input, 0, 26, 54, this));

        // Add 25 grid slots - index 1-25
        for(int i = 0; i < 5; ++i) {
            for(int j = 0; j < 5; ++j) {
                this.addSlot(new ResonanceGridSlot(this.grid, j + i * 5, 62 + j * 18, 18 + i * 18, this));
            }
        }

        // Add player inventory (27 slots)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 122 + i * 18));
            }
        }
        // Add player hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 180));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemStack = stackInSlot.copy();

            boolean isInvNotFull = false;
            for (int i = 26; i < 62; i++) {
                if (this.slots.get(i).getStack().isEmpty()) {
                    isInvNotFull = true;
                    break;
                }
            }
            if (index == 0 && isInvNotFull) {
                int diff = restrictionDifference(this.input.getStack(0), this.grid);
                if (diff > 0) {
                    this.dropInventory(this.player, this.grid);
                } else if (diff == 0) {
                    saveShapeToItem(this.input.getStack(0), this.grid, player);
                }
            }

            // from input/grid
            if (index < 26) {
                if (!this.insertItem(stackInSlot, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // from player inv + hotbar
            // allow to quick move only to main input
            else if (!this.insertItem(stackInSlot, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, stackInSlot);
        }

        clearInventory(this.gridBeforeClear);

        return itemStack;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (player.getWorld().isClient) {
            super.onContentChanged(inventory);
            return;
        }

        if (inventory == this.playerInventory) {
            super.onContentChanged(inventory);
            return;
        }
        // Input slot changed
        if (inventory == this.input) {

            // Do some clean up when switching items
            ItemStack newInput = this.input.getStack(0);

            boolean lastWasTablet = lastInputStack.isIn(ModTags.TABLETS);
            boolean lastWasCharm = lastInputStack.isIn(ModTags.CHARMS);
            boolean nowIsTablet = newInput.isIn(ModTags.TABLETS);
            boolean nowIsCharm = newInput.isIn(ModTags.CHARMS);

            if (lastWasTablet && nowIsCharm) {
                clearInventory(this.grid);
                clearInventory(this.gridBeforeClear);
            }

            if (lastWasCharm && (nowIsCharm || nowIsTablet)) {
                this.dropInventory(this.player, this.grid);
            }

            lastInputStack = newInput.copy();

            // If the stack removed is a charm change its shape
            ItemStack cursorStack = this.getCursorStack();
            if (!cursorStack.isEmpty()) {
                int diff = restrictionDifference(this.getCursorStack(), this.gridBeforeClear);
                if (diff > 0) {
                    this.dropInventory(this.player, this.gridBeforeClear);
                } else if (diff == 0) {
                    saveShapeToItem(this.getCursorStack(), this.gridBeforeClear, player);
                    clearInventory(this.gridBeforeClear);
                }
            }

            ItemStack inputStack = this.input.getStack(0);
            // If in the result input slot is not empty, and it's not a charm
            // load grid from the item
            if (!inputStack.isEmpty()) {
                if (inputStack.isIn(ModTags.TABLETS) && !player.getWorld().isClient) {
                    loadGridFromItem(inputStack, this.grid, player.getWorld());
                }
            }
            // If in the result input slot is empty
            // clear the grid
            else {
                if (!this.grid.isEmpty()) {
                    for (int i = 0; i < this.grid.size(); i++) {
                        this.gridBeforeClear.setStack(i, this.grid.getStack(i).copy());
                    }
                }
                clearInventory(this.grid);
            }
        }
        // Grid slot changed
        else if (inventory == this.grid) {
            // Grid changed save it
            if (!this.input.isEmpty()) {
                if (!this.input.getStack(0).isIn(ModTags.CHARMS)) {
                    saveGridToItem(this.input.getStack(0), this.grid, this.player.getWorld(), player);
                }
            }
        }
        super.onContentChanged(inventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            // Drop grid if not enough
            int diff = restrictionDifference(this.input.getStack(0), this.grid);
            if (diff > 0) {
                this.dropInventory(this.player, this.grid);
            }
            else if (diff == 0) {
                saveShapeToItem(this.input.getStack(0), this.grid, player);
            }
            // Drop input always
            this.dropInventory(this.player, this.input);
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.RESONANCE_TABLE);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == 1) {
            ItemStack stackCarried = this.getCursorStack();
            if (!stackCarried.isEmpty() && stackCarried.isIn(ModTags.CHARMS)) {
                ResonanceConstraintsComponent constraintsComponent =
                        CharmInfoHelper.getConstraintsComponentOrThrow("ResonanceTableScreenHandler$onButtonClick", stackCarried);
                if (constraintsComponent.isRotatable()) {
                    CharmShapeHelper.rotateShapeComponent(stackCarried);
                    return true;
                }
            }
        }
        return super.onButtonClick(player, id);
    }

    public boolean canPutInInputSlot(ItemStack itemStack) {
        if (itemStack.isIn(ModTags.RESONANCE_FORGEABLE)) {
            if (!itemStack.isIn(ModTags.CHARMS)) {
                return true;
            }
            else {
                ResonanceConstraintsComponent constraintsComponent =
                        CharmInfoHelper.getConstraintsComponentOrThrow(
                                "ResonanceTableScreenHandler$canPutInInputSlot", itemStack);

                return constraintsComponent.isReforgeable();
            }
        }
        return false;
    }

    public boolean canPutInGridSlot(ItemStack itemStack, int index) {
        if (this.input.isEmpty()) {
            return false;
        }
        // If charm is in the input slot
        else if (this.input.getStack(0).isIn(ModTags.CHARMS)) {

            ResonanceConstraintsComponent constraintsComponent =
                    CharmInfoHelper.getConstraintsComponentOrThrow(
                            "ResonanceTableScreenHandler$canPutInGridSlot",
                            this.input.getStack(0));

            // Check if item in reforge material tag and restriction is not at limit
            if (!itemStack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(constraintsComponent.requiredMaterialTag()))) ||
                    restrictionDifference(this.input.getStack(0), this.grid) <= 0) {
                return false;
            }

            // Check if it has another tile nearby or grid is empty
            return this.grid.isEmpty() || !constraintsComponent.mustBeJoined() || hasNeighboringMaterial(index);
        }
        // If tablet in the input slot
        else if (this.input.getStack(0).isIn(ModTags.TABLETS)) {
            NbtCompound nbtCompound = NbtHelper.getNbtCompound(this.input.getStack(0), ModDataComponentTypes.TABLET_GRID);

            if (!nbtCompound.contains("acceptable_items_tag") ||
                    !itemStack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(nbtCompound.getString("acceptable_items_tag"))))) {
                return false;
            }

            // Don't allow duplicate charms
//            if (this.grid.containsAny(Collections.singleton(itemStack.getItem()))) {
//                return false;
//            }

            // Get current tablet matrix
            List<String> matrix = getMatrix();
            int stackX = (index - 1) % 5;
            int stackY = (index - 1) / 5;

            ResonanceShapeComponent shapeComponent =
                    CharmInfoHelper.getShapeComponentOrThrow(
                            "ResonanceTableScreenHandler$canPutInGridSlot", itemStack);

            List<String> shape = new ArrayList<>(Arrays.asList(shapeComponent.shape().split("")));
            int xOrigin = shapeComponent.xOrigin();
            int yOrigin = shapeComponent.yOrigin();

            // Check if charm's shape overlaps with border/other charms
            for (int k = 0; k < shape.size(); k++) {
                if (!shape.get(k).equals("0")) {
                    int offsetX = (k % 5) - xOrigin + stackX;
                    int offsetY = (k / 5) - yOrigin + stackY;

                    if (offsetX > 4 || offsetX < 0 || offsetY > 4 || offsetY < 0) {
                        return false;
                    }

                    if (matrix.get(offsetY * 5 + offsetX).equals("1")) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean hasNeighboringMaterial(int index) {
        int x = (index - 1) % 5;
        int y = (index - 1) / 5;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                // Skip itself
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                // Skip if out of bounds
                if (nx < 0 || ny < 0 || nx >= 5 || ny >= 5) continue;

                int neighborIndex = ny * 5 + nx;
                if (!this.grid.getStack(neighborIndex).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    // 0 - perfect
    // 1 - not enough
    // -1 - too much
    private int restrictionDifference(ItemStack itemStack, Inventory grid) {
        if (itemStack.isIn(ModTags.CHARMS)) {
            ResonanceConstraintsComponent constraintsComponent =
                    CharmInfoHelper.getConstraintsComponentOrThrow(
                            "ResonanceTableScreenHandler$restrictionDifference", itemStack);

            for (int i = 0; i < grid.size(); i++) {
                if (!grid.getStack(i).isEmpty() &&
                        !grid.getStack(i).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(constraintsComponent.requiredMaterialTag())))) {
                    return -1;
                }
            }

            int actualTiles = (int) IntStream.range(0, grid.size())
                    .filter(i -> !grid.getStack(i).isEmpty())
                    .count();

            return constraintsComponent.requiredTiles() - actualTiles;
        }
        else {
            return -1;
        }
    }

    public List<String> getMatrix() {
        List<String> matrix = new ArrayList<>(
                IntStream.range(0, this.grid.size())
                        .mapToObj(i -> isSlotEnabled(i + 1) ? "0" : "1")
                        .toList()
        );

        for (int i = 0; i < this.grid.size(); i++) {
            ItemStack itemStack = this.grid.getStack(i);
            if (!itemStack.isEmpty()) {

                int stackX = i % 5;
                int stackY = i / 5;

                ResonanceShapeComponent shapeComponent =
                        CharmInfoHelper.getShapeComponentOrThrow(
                                "ResonanceTableScreenHandler$getMatrix", itemStack);

                List<String> shape = new ArrayList<>(Arrays.asList(shapeComponent.shape().split("")));
                int xOrigin = shapeComponent.xOrigin();
                int yOrigin = shapeComponent.yOrigin();

                for (int k = 0; k < shape.size(); k++) {
                    if (!shape.get(k).equals("0")) {
                        int offsetX = (k % 5) - xOrigin;
                        int offsetY = (k / 5) - yOrigin;

                        matrix.set((stackY + offsetY) * 5 + (stackX + offsetX), "1");
                    }
                }
            }
        }

        return matrix;
    }

    public boolean isSlotEnabled(int index) {
        if (this.input.isEmpty()) {
            return false;
        }
        ItemStack itemStack = this.input.getStack(0);
        if (itemStack.isIn(ModTags.TABLETS)) {
            NbtCompound nbtCompound = NbtHelper.getNbtCompound(itemStack, ModDataComponentTypes.TABLET_GRID);

            if (!nbtCompound.contains("shape")) {
                return true;
            }
            return Arrays.asList(nbtCompound.getString("shape").split("")).get(index - 1).equals("0");
        }
        else if (itemStack.isIn(ModTags.CHARMS)) {
            ResonanceConstraintsComponent constraintsComponent =
                    CharmInfoHelper.getConstraintsComponentOrThrow(
                            "ResonanceTableScreenHandler$isSlotEnabled", itemStack);

            return Arrays.asList(constraintsComponent.startGrid().split("")).get(index - 1).equals("0");
        }
        return false;
    }

    private static void saveGridToItem(ItemStack itemStack, Inventory grid, World world, PlayerEntity player) {
        NbtCompound nbtCompound = new NbtCompound();

        for (int i = 0; i < grid.size(); i++) {
            ItemStack gridStack = grid.getStack(i);
            if (!gridStack.isEmpty()) {
                nbtCompound.put("grid_slot_" + i, gridStack.encode(world.getRegistryManager()));
            } else {
                nbtCompound.remove("grid_slot_" + i);
            }
        }

        if (!player.getWorld().isClient) {
            itemStack.set(ModDataComponentTypes.TABLET_CONTENTS, NbtComponent.of(nbtCompound));
        }

        if (itemStack.isOf(ModItems.STARFORGED_TABLET) && PuzzleBoxItem.isFull(itemStack, world)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/starforged_tablet_full", "impossible");
        }
    }

    private static void saveShapeToItem(ItemStack itemStack, Inventory grid, PlayerEntity player) {

        if (grid.isEmpty()) return;

        ResonanceConstraintsComponent constraintsComponent =
                CharmInfoHelper.getConstraintsComponentOrThrow(
                        "ResonanceTableScreenHandler$saveShapeToItem", itemStack);

        for (int i = 0; i < grid.size(); i++) {
            if (!grid.getStack(i).isEmpty() &&
                    !grid.getStack(i).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(constraintsComponent.requiredMaterialTag())))) {
                return;
            }
        }

        String shape = IntStream.range(0, grid.size())
                .mapToObj(i -> !grid.getStack(i).isEmpty() ? "1" : "0")
                .collect(Collectors.joining());

        List<Point> filled = IntStream.range(0, grid.size())
                .filter(i -> !grid.getStack(i).isEmpty())
                .mapToObj(i -> new Point(i % 5, i / 5))
                .toList();

        double centerX = filled.stream().mapToInt(p -> p.x).average().orElse(2);
        double centerY = filled.stream().mapToInt(p -> p.y).average().orElse(2);

        Point origin = filled.stream()
                .min(Comparator.comparingDouble(p -> p.distance(centerX, centerY)))
                .orElse(filled.getFirst());

        if (!player.getWorld().isClient) {
            itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, new ResonanceShapeComponent(shape, origin.x, origin.y));
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/charm_reshape", "impossible");
    }

    private static void loadGridFromItem(ItemStack itemStack, Inventory grid, World world) {

        if (!itemStack.contains(ModDataComponentTypes.TABLET_CONTENTS)) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(itemStack, ModDataComponentTypes.TABLET_CONTENTS);

        for (int i = 0; i < grid.size(); i++) {

            if (nbtCompound.contains("grid_slot_" + i)) {

                grid.setStack(i, ItemStack.fromNbt(world.getRegistryManager(), nbtCompound.get("grid_slot_" + i))
                        .orElse(ItemStack.EMPTY));
            }
            else {
                grid.setStack(i, ItemStack.EMPTY);
            }
        }
    }

    private void clearInventory(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) inventory.setStack(i, ItemStack.EMPTY);
    }
}
