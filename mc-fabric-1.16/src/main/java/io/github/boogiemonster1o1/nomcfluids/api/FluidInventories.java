package io.github.boogiemonster1o1.nomcfluids.api;

import java.util.Map;

import com.mojang.datafixers.util.Pair;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.util.NbtType;

/**
 * Helper methods for deserializing maps and pairs of {@link Fraction} and
 * {@link FluidType} to tags
 */
public class FluidInventories {
	public static CompoundTag toTag(CompoundTag tag, Map<FluidType, Fraction> inventory) {
		ListTag list = new ListTag();
		inventory.forEach((fluidType, fraction) -> {
			list.add(toTagSingle(new CompoundTag(), fluidType, fraction));
		});
		tag.put("Fluids", list);
		return tag;
	}

	public static CompoundTag toTagSingle(CompoundTag tag, FluidType fluidType, Fraction fraction) {
		tag.putString("Fluid", Registry.FLUID.getId(FluidTypes.fluidOf(fluidType)).toString());
		Fractions.toTag(tag, fraction);
		return tag;
	}

	public static Pair<FluidType, Fraction> fromTagSingle(CompoundTag tag) {
		FluidType fluidType = FluidTypes.REGISTRY.get(new Identifier(tag.getString("Fluid")));
		Fraction fraction = Fractions.fromTag(tag);
		return Pair.of(fluidType, fraction);
	}

	public static Map<FluidType, Fraction> fromTag(CompoundTag tag) {
		Object2ObjectMap<FluidType, Fraction> map = new Object2ObjectOpenHashMap<>();
		ListTag list = tag.getList("Fluids", NbtType.COMPOUND);
		list.forEach((tag1) -> {
			Pair<FluidType, Fraction> pair = fromTagSingle((CompoundTag) tag1);
			map.put(pair.getFirst(), pair.getSecond());
		});
		return map;
	}
}
