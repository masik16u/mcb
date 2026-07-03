package com.masik.mythiccharms.datagen;

import com.masik.mythiccharms.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (EntityType<?> entityType : DROP_COOKED_FOOD) {
            getOrCreateTagBuilder(ModTags.ADVANCEMENT_DROP_COOKED_FOOD).add(entityType);
        }
        for (EntityType<?> entityType : PROJECTILES) {
            getOrCreateTagBuilder(ModTags.ADVANCEMENT_PROJECTILES).add(entityType);
        }
    }

    private static final List<EntityType<?>> DROP_COOKED_FOOD = List.of(
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.HUSK,
            EntityType.CHICKEN,
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.MOOSHROOM,
            EntityType.RABBIT,
            EntityType.PIG,
            EntityType.HOGLIN,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.DOLPHIN,
            EntityType.GUARDIAN,
            EntityType.ELDER_GUARDIAN,
            EntityType.POLAR_BEAR
    );

    private static final List<EntityType<?>> PROJECTILES = List.of(
            EntityType.ARROW,
            EntityType.FIREBALL,
            EntityType.SMALL_FIREBALL,
            EntityType.DRAGON_FIREBALL,
            EntityType.LLAMA_SPIT,
            EntityType.SHULKER_BULLET,
            EntityType.SNOWBALL,
            EntityType.POTION,
            EntityType.TRIDENT,
            EntityType.WIND_CHARGE,
            EntityType.WITHER_SKULL
    );
}
