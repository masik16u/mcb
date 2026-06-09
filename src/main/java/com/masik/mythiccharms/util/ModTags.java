package com.masik.mythiccharms.util;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> CHARMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "charms"));
    public static final TagKey<Item> TABLETS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "tablets"));
    public static final TagKey<Item> TOKENS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "tokens"));
    public static final TagKey<Item> WEARABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "wearable"));
    public static final TagKey<Item> CHARM_REFORGE_MATERIAL = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "charm_reforge_material"));
    public static final TagKey<Item> RESONANCE_FORGEABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "resonance_forgeable"));

    public static final TagKey<Item> TABLET_ACCEPTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "tablet_acceptable"));
    public static final TagKey<Item> PUZZLE_ACCEPTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MythicCharms.MOD_ID, "puzzle_acceptable"));

    public static final TagKey<Block> SUSPICIOUS_BLOCKS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(MythicCharms.MOD_ID, "suspicious_blocks"));
}
