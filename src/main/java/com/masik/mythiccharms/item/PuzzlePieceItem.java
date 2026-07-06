package com.masik.mythiccharms.item;

import com.masik.mythiccharms.component.*;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class PuzzlePieceItem extends ScriptItem {
    public PuzzlePieceItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        if (itemStack.contains(ModDataComponentTypes.RESONANCE_CONSTRAINTS)) {
            ResonanceConstraintsComponent constraintsComponent = itemStack.get(ModDataComponentTypes.RESONANCE_CONSTRAINTS);
            if (constraintsComponent != null && !constraintsComponent.rebuilt()) {
                rebuildComponents(itemStack);
            }
        }
    }

    private static void rebuildComponents(ItemStack itemStack) {
        // Rebuilding
        ResonanceColorComponent colorComponent = itemStack.get(ModDataComponentTypes.RESONANCE_COLOR);
        if (colorComponent != null) {
            ResonanceColorComponent newColorComponent = new ResonanceColorComponent(colorComponent.rgb() - 1);
            itemStack.set(ModDataComponentTypes.RESONANCE_COLOR, newColorComponent);
        }
        ResonanceShapeComponent shapeComponent = itemStack.get(ModDataComponentTypes.RESONANCE_SHAPE);
        if (shapeComponent != null) {
            ResonanceShapeComponent newShapeComponent = new ResonanceShapeComponent(
                    shapeComponent.shape(),
                    shapeComponent.xOrigin(),
                    shapeComponent.yOrigin(),
                    true
            );
            itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, newShapeComponent);
        }
        ResonanceConstraintsComponent constraintsComponent = itemStack.get(ModDataComponentTypes.RESONANCE_CONSTRAINTS);
        if (constraintsComponent != null) {
            ResonanceConstraintsComponent newConstraintsComponent = new ResonanceConstraintsComponent(
                    constraintsComponent.isReforgeable(),
                    constraintsComponent.isQuickReforgeable(),
                    constraintsComponent.isRotatable(),
                    constraintsComponent.startGrid(),
                    constraintsComponent.requiredTiles(),
                    constraintsComponent.requiredMaterialTag(),
                    constraintsComponent.mustBeJoined(),
                    true
            );
            itemStack.set(ModDataComponentTypes.RESONANCE_CONSTRAINTS, newConstraintsComponent);
        }
    }
}
