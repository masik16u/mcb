package com.masik.mythiccharms.datagen;

import com.masik.mythiccharms.block.ModBlocks;
import com.masik.mythiccharms.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RESONANCE_TABLE)
                .pattern("AIA")
                .pattern("TCT")
                .pattern("SSS")
                .input('A', Items.AMETHYST_SHARD)
                .input('I', Items.IRON_INGOT)
                .input('T', Items.CHISELED_TUFF)
                .input('C', ModItems.AMETHYST_CORE)
                .input('S', Items.SMOOTH_STONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.DIAMOND_CORE)
                .input(ModItems.AMETHYST_CORE).input(Items.DIAMOND, 2)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BLANK_RUNE)
                .input(ModItems.AMETHYST_CORE).input(Items.COBBLESTONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CARVING_TEMPLATE, 2)
                .input(ModItems.BLANK_RUNE)
                .input(ModItems.CARVING_TEMPLATE)
                .input(ModItems.RESONANCE_INSCRIBER)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARVING_TEMPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CARVING_TEMPLATE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CHARM_BASE)
                .pattern(" C ")
                .pattern("CDC")
                .pattern(" C ")
                .input('C', Items.COBBLED_DEEPSLATE)
                .input('D', Items.DIAMOND_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(Items.COBBLED_DEEPSLATE),
                        FabricRecipeProvider.conditionsFromItem(Items.COBBLED_DEEPSLATE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.DIAMOND_TABLET)
                .pattern("DDD")
                .pattern("DCD")
                .pattern("DAD")
                .input('D', Items.DIAMOND)
                .input('A', Items.AMETHYST_SHARD)
                .input('C', ModItems.AMETHYST_CORE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter);
        offerNetheriteUpgradeRecipe(recipeExporter, ModItems.DIAMOND_TABLET,
                RecipeCategory.MISC, ModItems.NETHERITE_TABLET);
        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(ModItems.CARVING_TEMPLATE),
                Ingredient.ofItems(ModItems.NETHERITE_TABLET),
                Ingredient.ofItems(Items.NETHER_STAR),
                RecipeCategory.MISC,
                ModItems.STARFORGED_TABLET)
                .criterion(FabricRecipeProvider.hasItem(Items.NETHER_STAR),
                        FabricRecipeProvider.conditionsFromItem(Items.NETHER_STAR))
                .offerTo(recipeExporter, RecipeProvider.getItemPath(ModItems.STARFORGED_TABLET) + "_smithing");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RESONANCE_INSCRIBER)
                .pattern("  A")
                .pattern(" IC")
                .pattern("S  ")
                .input('I', Items.IRON_INGOT)
                .input('A', Items.AMETHYST_SHARD)
                .input('S', Items.STICK)
                .input('C', ModItems.AMETHYST_CORE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter);
        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(ModItems.CARVING_TEMPLATE),
                        Ingredient.ofItems(Items.BRUSH),
                        Ingredient.ofItems(ModItems.AMETHYST_CORE),
                        RecipeCategory.MISC,
                        ModItems.AMETHYST_BRUSH)
                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
                .offerTo(recipeExporter, RecipeProvider.getItemPath(ModItems.AMETHYST_BRUSH) + "_smithing");

//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RESONANCE_KEY)
//                .pattern("C")
//                .pattern("I")
//                .pattern("A")
//                .input('C', ModItems.AMETHYST_CORE)
//                .input('I', Items.IRON_INGOT)
//                .input('A', Items.AMETHYST_SHARD)
//                .criterion(FabricRecipeProvider.hasItem(ModItems.AMETHYST_CORE),
//                        FabricRecipeProvider.conditionsFromItem(ModItems.AMETHYST_CORE))
//                .offerTo(recipeExporter);

        ModItems.RUNE_TO_MATERIAL.forEach((rune, material) ->
                SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(ModItems.CARVING_TEMPLATE),
                                Ingredient.ofItems(ModItems.BLANK_RUNE),
                                Ingredient.ofItems(material),
                                RecipeCategory.MISC,
                                rune)
                        .criterion(FabricRecipeProvider.hasItem(material),
                                FabricRecipeProvider.conditionsFromItem(material))
                        .offerTo(recipeExporter,RecipeProvider.getItemPath(rune) + "_smithing"));

        ModItems.CHARM_TO_RUNES.forEach((charm, runes) -> {
            ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder
                    .create(RecipeCategory.MISC, charm)
                    .input(ModItems.CHARM_BASE)
                    .input(ModItems.RESONANCE_INSCRIBER);

            runes.forEach(builder::input);

            builder.criterion(FabricRecipeProvider.hasItem(ModItems.CHARM_BASE),
                            FabricRecipeProvider.conditionsFromItem(ModItems.CHARM_BASE))
                    .offerTo(recipeExporter);
        });

//        ModItems.CHARMS.forEach(charm ->
//                ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, charm)
//                        .input(charm)
//                        .input(ModItems.DIAMOND_CORE)
//                        .input(ModItems.RESONANCE_INSCRIBER)
//                        .criterion(FabricRecipeProvider.hasItem(charm),
//                                FabricRecipeProvider.conditionsFromItem(charm))
//                        .offerTo(recipeExporter, RecipeProvider.getItemPath(charm) + "_quick_reshape"));
    }
}
