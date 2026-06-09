package com.masik.mythiccharms.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record ResonanceConstraintsComponent(boolean isReforgeable, boolean isQuickReforgeable, boolean isRotatable, String startGrid, int requiredTiles, String requiredMaterialTag, boolean mustBeJoined) {
    public static final Codec<ResonanceConstraintsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("is_reforgeable", true).forGetter(ResonanceConstraintsComponent::isReforgeable),
            Codec.BOOL.optionalFieldOf("is_quick_reforgeable", true).forGetter(ResonanceConstraintsComponent::isQuickReforgeable),
            Codec.BOOL.optionalFieldOf("is_rotatable", true).forGetter(ResonanceConstraintsComponent::isRotatable),
            Codec.STRING.optionalFieldOf("start_grid", "0000000000000000000000000").forGetter(ResonanceConstraintsComponent::startGrid),
            Codec.INT.optionalFieldOf("required_tiles", 0).forGetter(ResonanceConstraintsComponent::requiredTiles),
            Codec.STRING.optionalFieldOf("required_material_tag", "mythic_charms:charm_reforge_material").forGetter(ResonanceConstraintsComponent::requiredMaterialTag),
            Codec.BOOL.optionalFieldOf("must_be_joined", true).forGetter(ResonanceConstraintsComponent::mustBeJoined)
    ).apply(instance, ResonanceConstraintsComponent::new));

//    public static final PacketCodec<ByteBuf, ResonanceConstraintsComponent> PACKET_CODEC = PacketCodec.tuple(
//            PacketCodecs.BOOL, ResonanceConstraintsComponent::isReforgeable,
//            PacketCodecs.BOOL, ResonanceConstraintsComponent::isQuickReforgeable,
//            PacketCodecs.BOOL, ResonanceConstraintsComponent::isRotatable,
//            PacketCodecs.STRING, ResonanceConstraintsComponent::startGrid,
//            PacketCodecs.INTEGER, ResonanceConstraintsComponent::requiredTiles,
//            PacketCodecs.STRING, ResonanceConstraintsComponent::requiredMaterialTag,
//            PacketCodecs.BOOL, ResonanceConstraintsComponent::mustBeJoined,
//            ResonanceConstraintsComponent::new
//    );

    public static final PacketCodec<PacketByteBuf, ResonanceConstraintsComponent> PACKET_CODEC =
            PacketCodec.of(
                    (value, buf) -> {
                        buf.writeBoolean(value.isReforgeable());
                        buf.writeBoolean(value.isQuickReforgeable());
                        buf.writeBoolean(value.isRotatable());
                        buf.writeString(value.startGrid());
                        buf.writeInt(value.requiredTiles());
                        buf.writeString(value.requiredMaterialTag());
                        buf.writeBoolean(value.mustBeJoined());
                    },
                    buf -> new ResonanceConstraintsComponent(
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readString(),
                            buf.readInt(),
                            buf.readString(),
                            buf.readBoolean()
                    )
            );
}
