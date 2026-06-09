package com.masik.mythiccharms.block;

import com.masik.mythiccharms.MythicCharms;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block RESONANCE_TABLE = registerBlock("resonance_table", new ResonanceTableBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

    private static Block registerBlock(String name, Block block) {

        registerBlockItem(name, block);

        return Registry.register(Registries.BLOCK, Identifier.of(MythicCharms.MOD_ID, name), block);

    }

    private static void registerBlockItem(String name, Block block) {

        Registry.register(Registries.ITEM, Identifier.of(MythicCharms.MOD_ID, name), new BlockItem(block,
                new Item.Settings()));

    }

    public static void registerModBlocks() {

    }

}
