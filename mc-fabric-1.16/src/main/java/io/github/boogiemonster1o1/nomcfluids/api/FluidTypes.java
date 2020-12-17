package io.github.boogiemonster1o1.nomcfluids.api;

import io.github.boogiemonster1o1.nomcfluids.api.settings.FluidSettings;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

public class FluidTypes {
	public static final Registry<ExtendedFluidType> REGISTRY = FabricRegistryBuilder.createSimple(ExtendedFluidType.class, new Identifier("nomcfluids", "fluid_type")).buildAndRegister();

	public static ExtendedFluidType create(Fluid fluid, FluidSettings settings) {
		if (fluid == Fluids.EMPTY) {
			throw new UnsupportedOperationException("Cannot store empty fluid!");
		}
		return new ExtendedFluidType(Registry.FLUID.getId(fluid), settings, fluid);
	}

	public static Fluid fluidOf(FluidType type) {
		if (type.getClass() == ExtendedFluidType.class) {
			return ((ExtendedFluidType) type).getFluid();
		}
		throw new UnsupportedOperationException("Cannot get fluid of " + type + " as it is not an ExtendedFluidType!");
	}
}
