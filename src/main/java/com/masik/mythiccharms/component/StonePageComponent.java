package com.masik.mythiccharms.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record StonePageComponent(String content, boolean hidden) {
    public static final Codec<StonePageComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("content").forGetter(StonePageComponent::content),
            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(StonePageComponent::hidden)
    ).apply(instance, StonePageComponent::new));

    public static final PacketCodec<ByteBuf, StonePageComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, StonePageComponent::content,
            PacketCodecs.BOOL, StonePageComponent::hidden,
            StonePageComponent::new
    );
}
