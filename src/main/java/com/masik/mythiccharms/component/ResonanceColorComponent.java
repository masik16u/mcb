package com.masik.mythiccharms.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ResonanceColorComponent(int rgb) {
    public static final Codec<ResonanceColorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("rgb").forGetter(ResonanceColorComponent::rgb)
    ).apply(instance, ResonanceColorComponent::new));

    public static final PacketCodec<ByteBuf, ResonanceColorComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ResonanceColorComponent::rgb,
            ResonanceColorComponent::new
    );
}
