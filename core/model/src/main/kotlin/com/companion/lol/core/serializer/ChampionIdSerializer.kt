package com.companion.lol.core.serializer

import com.companion.lol.core.model.ChampionId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ChampionIdSerializer : KSerializer<ChampionId> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("ChampionId", PrimitiveKind.INT)

  override fun serialize(encoder: Encoder, value: ChampionId) {
    encoder.encodeInt(value.value)
  }

  override fun deserialize(decoder: Decoder): ChampionId {
    return ChampionId(decoder.decodeInt())
  }
}
