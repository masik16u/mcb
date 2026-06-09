package com.masik.mythiccharms.datagen;

import com.masik.mythiccharms.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        ModItems.CHARMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        ModItems.TOKENS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        ModItems.RUNES.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        ModItems.TABLETS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        itemModelGenerator.register(ModItems.AMETHYST_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLANK_RUNE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHARM_BASE, Models.GENERATED);
        itemModelGenerator.register(ModItems.TOTEM_OF_PRESERVATION, Models.GENERATED);
//        itemModelGenerator.register(ModItems.RESONANCE_KEY, Models.GENERATED);
        itemModelGenerator.register(ModItems.PUZZLE_PIECE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PUZZLE_BOX, Models.GENERATED);
        itemModelGenerator.register(ModItems.RESONANCE_INSCRIBER, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CARVING_TEMPLATE, Models.GENERATED);
    }
}
