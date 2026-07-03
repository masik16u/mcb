package com.masik.mythiccharms.datagen;

import com.masik.mythiccharms.item.ModItems;
import com.masik.mythiccharms.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (Item charm : ModItems.CHARMS) {
            getOrCreateTagBuilder(ModTags.CHARMS)
                    .add(charm);
            getOrCreateTagBuilder(ModTags.TABLET_ACCEPTABLE)
                    .add(charm);
        }
        for (Item token : ModItems.TOKENS) {
            getOrCreateTagBuilder(ModTags.CHARMS)
                    .add(token);
            getOrCreateTagBuilder(ModTags.TABLET_ACCEPTABLE)
                    .add(token);
            getOrCreateTagBuilder(ModTags.TOKENS)
                    .add(token);
        }
        for (Item tablet : ModItems.TABLETS) {
            getOrCreateTagBuilder(ModTags.TABLETS)
                    .add(tablet);
            getOrCreateTagBuilder(ModTags.WEARABLE)
                    .add(tablet);
        }
        getOrCreateTagBuilder(ModTags.TABLETS).add(ModItems.PUZZLE_BOX);
        getOrCreateTagBuilder(ModTags.PUZZLE_ACCEPTABLE).add(ModItems.PUZZLE_PIECE);
        getOrCreateTagBuilder(ModTags.CHARMS).add(ModItems.PUZZLE_PIECE);

        getOrCreateTagBuilder(ModTags.CHARM_REFORGE_MATERIAL).add(ModItems.DIAMOND_CORE);

        for (Item item : IRON_ITEMS) {
            getOrCreateTagBuilder(ModTags.ADVANCEMENT_IRON_ITEMS).add(item);
        }
        for (Item item : SMELT_RESULTS) {
            getOrCreateTagBuilder(ModTags.ADVANCEMENT_SMELT_RESULTS).add(item);
        }
    }

    private static final List<Item> IRON_ITEMS = List.of(
            Items.IRON_INGOT,
            Items.IRON_NUGGET,
            Items.IRON_HELMET,
            Items.IRON_CHESTPLATE,
            Items.IRON_LEGGINGS,
            Items.IRON_BOOTS,
            Items.IRON_BLOCK,
            Items.IRON_BARS,
            Items.SHEARS,
            Items.CAULDRON,
            Items.MINECART,
            Items.BUCKET,
            Items.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Items.IRON_DOOR,
            Items.IRON_TRAPDOOR,
            Items.CHAIN
    );

    private static final List<Item> SMELT_RESULTS = List.of(
            Items.CHARCOAL,
            Items.LIME_DYE,
            Items.GREEN_DYE,
            Items.POPPED_CHORUS_FRUIT,
            Items.DRIED_KELP
    );
}
