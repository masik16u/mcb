package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> MYTHIC_CHARMS_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),
            Identifier.of(MythicCharms.MOD_ID, "mythic_charms_group"));
    public static final ItemGroup MYTHIC_CHARMS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.RESONANCE_TABLE))
            .displayName(Text.translatable("itemGroup.mythic_charms"))
            .build();

    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, MYTHIC_CHARMS_GROUP_KEY, MYTHIC_CHARMS_GROUP);

        ItemGroupEvents.modifyEntriesEvent(MYTHIC_CHARMS_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModBlocks.RESONANCE_TABLE);
            itemGroup.add(ModItems.AMETHYST_CORE);
            itemGroup.add(ModItems.DIAMOND_CORE);

            itemGroup.add(ModItems.BLANK_RUNE);
            ModItems.RUNES.forEach(itemGroup::add);

            itemGroup.add(ModItems.CHARM_BASE);
            itemGroup.add(ModItems.CARVING_TEMPLATE);

            ModItems.CHARMS.forEach(itemGroup::add);
            ModItems.TOKENS.forEach(itemGroup::add);
            ModItems.TABLETS.forEach(itemGroup::add);

            itemGroup.add(ModItems.RESONANCE_INSCRIBER);
            itemGroup.add(ModItems.AMETHYST_BRUSH);
            itemGroup.add(ModItems.TOTEM_OF_PRESERVATION);
//            itemGroup.add(ModItems.RESONANCE_KEY);
            itemGroup.add(ModItems.PUZZLE_BOX);
            itemGroup.add(ModItems.PUZZLE_PIECE);
        });
    }
}
