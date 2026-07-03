package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(ServerPlayerEntity.class)
public class CollectorGiftServerMixin {

    @Inject(method = "playerTick", at = @At("RETURN"))
    private void collectorGift(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "collector_gift");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("collector_gift");

        float radius = abilityCompound.getFloat("radius");
        float radiusSquared = radius * radius;
        float velocityMultiplier = abilityCompound.getFloat("velocity_multiplier");

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "weightless_flow");
        boolean weightlessFlow = comboCompound != null && (!player.isSneaking() || !comboCompound.getBoolean("disable_with_sneaking"));

        Box box = player.getBoundingBox().expand(radius);
        List<ItemEntity> itemEntities = player.getWorld().getEntitiesByType(
                EntityType.ITEM,
                box, entity -> entity.squaredDistanceTo(player) < radiusSquared);
        itemEntities.forEach(item -> {
            if (weightlessFlow) {
                item.setVelocity(new Vec3d(0, 0.04, 0));
            }
            else {
                item.addVelocity(player.getPos().subtract(item.getPos()).multiply(velocityMultiplier));
            }
            item.velocityModified = true;
        });

        if (weightlessFlow) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "cg_wf");
        }
        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "collector_gift");
        if (containsAllItems(itemEntities, player.getWorld())) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/collector_gift_magnet", "impossible");
        }
    }

    @Unique
    private static boolean containsAllItems(List<ItemEntity> entities, World world) {
        Set<Item> presentItems = entities.stream()
                .map(entity -> entity.getStack().getItem())
                .collect(Collectors.toSet());

        List<Item> items = TagHelper.getItemsFromTag(world, ModTags.ADVANCEMENT_IRON_ITEMS);
        return presentItems.containsAll(items);
    }

}
