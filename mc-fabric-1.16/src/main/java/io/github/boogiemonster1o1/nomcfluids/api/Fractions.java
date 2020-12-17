package io.github.boogiemonster1o1.nomcfluids.api;

import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

/**
 * Contains helper methods related to serialization and deserialization
 * of Fractions.
 */
public class Fractions {
	/**
	 * Serializes this instance to the given packet buffer.
	 *
	 * @param buffer packet buffer to receive serialized data
	 */
	public static PacketByteBuf toPacketByteBuf(PacketByteBuf buffer, Fraction fraction) {
		buffer.writeVarLong(fraction.whole());
		buffer.writeVarLong(fraction.numerator());
		buffer.writeVarLong(fraction.divisor());
		return buffer;
	}

	/**
	 * Serializes this instance in an nbt compound tag without
	 * creating a new tag instance.
	 *
	 * @param tag NBT tag to contain serialized data
	 */
	public static CompoundTag toTag(CompoundTag tag, Fraction fraction) {
		tag.putLong("whole", fraction.whole());
		tag.putLong("numerator", fraction.numerator());
		tag.putLong("denominator", fraction.divisor());
		return tag;
	}

	/**
	 * Deserializes an new instance from an nbt tag previously
	 * returned by {@link #toTag(CompoundTag, Fraction)} ()}.
	 *
	 * @param tag nbt tag previously returned by {@link #toTag(CompoundTag, Fraction)}
	 */
	public static Fraction fromTag(CompoundTag tag) {
		long whole = tag.getLong("whole");
		long numerator = tag.getLong("numerator");
		long denominator = tag.getLong("denominator");
		return Fraction.of(whole, numerator, denominator);
	}

	/**
	 * Deserializes a new instance from previously encoded to a packet
	 * buffer by {@link #toPacketByteBuf(PacketByteBuf, Fraction)}
	 *
	 * @param buffer packet buffer containing encoded data
	 */
	public static Fraction fromPacketByteBuf(PacketByteBuf buffer) {
		long whole = buffer.readVarLong();
		long numerator = buffer.readVarLong();
		long denominator = buffer.readVarLong();
		return Fraction.of(whole, numerator, denominator);
	}
}
