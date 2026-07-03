package com.masik.mythiccharms.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TagHelper {

    public static List<Item> getItemsFromTag(World world, TagKey<Item> tagKey) {
        DynamicRegistryManager registryManager = world.getRegistryManager();

        RegistryEntryList.Named<Item> registryEntries = registryManager.get(RegistryKeys.ITEM).getOrCreateEntryList(tagKey);
        return listFromRegistry(registryEntries);
    }

    public static List<EntityType<?>> getEntitiesFromTag(World world, TagKey<EntityType<?>> tagKey) {
        DynamicRegistryManager registryManager = world.getRegistryManager();

        RegistryEntryList.Named<EntityType<?>> registryEntries = registryManager.get(RegistryKeys.ENTITY_TYPE).getOrCreateEntryList(tagKey);
        return listFromRegistry(registryEntries);
    }

    public static <T> List<T> listFromRegistry(RegistryEntryList.Named<T> registryEntries) {
        List<T> list = new ArrayList<>();

        for (RegistryEntry<T> entry : registryEntries) {
            list.add(entry.value());
        }
        return list;
    }

}
