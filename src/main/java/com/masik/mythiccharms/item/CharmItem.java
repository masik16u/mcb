package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.component.ShapeVariantsComponent;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.TextHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
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
                ResonanceShapeComponent newShapeComponent = newRandomShape(itemStack, world, false);
                itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, newShapeComponent);
                ShapeVariantsComponent newVariantsComponent = new ShapeVariantsComponent(variantsComponent.variants(), true);
                itemStack.set(ModDataComponentTypes.SHAPE_VARIANTS, newVariantsComponent);
            }
        }
    }

    @Override
    public void onCraft(ItemStack itemStack, World world) {
        super.onCraft(itemStack, world);

        ResonanceShapeComponent newShapeComponent = newRandomShape(itemStack, world, false);
        itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, newShapeComponent);
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
