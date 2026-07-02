package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.*;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.TextHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class CharmItem extends Item {
    public CharmItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (Screen.hasAltDown()) {
            return Text.translatable(stack.getTranslationKey() + ".alt").setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script")));
        }
        return Text.empty()
                .append(Text.translatable("item.mythic_charms.charm_prefix"))
                .append(super.getName(stack))
                .append(Text.translatable("item.mythic_charms.charm_suffix"));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (Screen.hasAltDown()) {
            tooltip.add(Text.translatable(stack.getTranslationKey() + ".desc.alt")
                    .setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script")))
                    .formatted(Formatting.GRAY));
        }
        else {
            List<String> descLines = TextHelper.wrapText(Text.translatable(stack.getTranslationKey() + ".desc").getString(), 28);

            for (String line : descLines) {
                tooltip.add(Text.literal(line).formatted(Formatting.GRAY));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        if (itemStack.contains(ModDataComponentTypes.SHAPE_VARIANTS)) {
            ShapeVariantsComponent variantsComponent = itemStack.get(ModDataComponentTypes.SHAPE_VARIANTS);
            if (variantsComponent != null && !variantsComponent.generated()) {
                rebuildComponents(itemStack, world);
            }
        }
    }

//    @Override
//    public void onCraft(ItemStack itemStack, World world) {
//        super.onCraft(itemStack, world);
//
//        rebuildComponents(itemStack, world);
//    }

    private static void rebuildComponents(ItemStack itemStack, World world) {
        ResonanceShapeComponent newShapeComponent = newRandomShape(itemStack, world, false);
        itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, newShapeComponent);
        ShapeVariantsComponent variantsComponent = itemStack.get(ModDataComponentTypes.SHAPE_VARIANTS);
        if (variantsComponent != null) {
            ShapeVariantsComponent newVariantsComponent = new ShapeVariantsComponent(variantsComponent.variants(), true);
            itemStack.set(ModDataComponentTypes.SHAPE_VARIANTS, newVariantsComponent);
        }

        // Rebuilding
        ResonanceColorComponent colorComponent = itemStack.get(ModDataComponentTypes.RESONANCE_COLOR);
        if (colorComponent != null) {
            ResonanceColorComponent newColorComponent = new ResonanceColorComponent(colorComponent.rgb() - 1);
            itemStack.set(ModDataComponentTypes.RESONANCE_COLOR, newColorComponent);
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

    public static ResonanceShapeComponent newRandomShape(ItemStack itemStack, World world, boolean checkIfNew) {
        ResonanceShapeComponent currentShapeComponent = CharmInfoHelper.getShapeComponentOrThrow("CharmItem$randomShape", itemStack);
        ShapeVariantsComponent variantsComponent = itemStack.get(ModDataComponentTypes.SHAPE_VARIANTS);

        if (variantsComponent == null || variantsComponent.variants().isEmpty()) {
            return currentShapeComponent;
        }

        List<ResonanceShapeComponent> filteredList = variantsComponent.variants().stream()
                .filter(shapeComponent -> !shapeComponent.shape().equals(currentShapeComponent.shape()) || !checkIfNew).toList();

//        itemStack.remove(ModDataComponentTypes.SHAPE_VARIANTS);
        if (filteredList.isEmpty()) {
            return currentShapeComponent;
        }
        return filteredList.get(world.random.nextInt(filteredList.size()));
    }
}
