package com.masik.mythiccharms.util;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementHelper {

    public static void grantAdvancement(LivingEntity entity, String advancementId, String criterion) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        AdvancementEntry advancementEntry = server.getAdvancementLoader().get(Identifier.of(advancementId));
        if (advancementEntry == null) return;

        player.getAdvancementTracker().grantCriterion(advancementEntry, criterion);
    }

}
