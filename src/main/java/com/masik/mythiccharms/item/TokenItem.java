package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TokenItem extends Item {
    public TokenItem(Settings settings) {
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
        NbtComponent abilitiesComponent = itemStack.get(ModDataComponentTypes.CHARM_ABILITIES);
        if (abilitiesComponent != null) {
            NbtCompound compound = abilitiesComponent.copyNbt();
            compound.put("rebuilt", NbtByte.of(true));
            NbtComponent newAbilitiesComponent = NbtComponent.of(compound);
            itemStack.set(ModDataComponentTypes.CHARM_ABILITIES, newAbilitiesComponent);
        }
        NbtComponent combinationsComponent = itemStack.get(ModDataComponentTypes.CHARM_COMBINATIONS);
        if (combinationsComponent != null) {
            NbtCompound compound = combinationsComponent.copyNbt();
            compound.put("rebuilt", NbtByte.of(true));
            NbtComponent newCombinationsComponent = NbtComponent.of(compound);
            itemStack.set(ModDataComponentTypes.CHARM_COMBINATIONS, newCombinationsComponent);
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        if (Screen.hasAltDown()) {
            return Text.translatable(stack.getTranslationKey() + ".alt").setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script")));
        }
        return Text.empty()
                .append(Text.translatable("item.mythic_charms.token_prefix"))
                .append(super.getName(stack))
                .append(Text.translatable("item.mythic_charms.token_suffix"));
    }
}
