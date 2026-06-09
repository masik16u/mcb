package com.masik.mythiccharms.datagen;

import com.masik.mythiccharms.item.ModItems;
import com.masik.mythiccharms.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

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
    }
}
