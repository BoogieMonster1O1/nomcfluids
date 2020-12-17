package io.github.boogiemonster1o1.nomcfluids.api;

import java.util.Objects;

import io.github.boogiemonster1o1.nomcfluids.api.settings.FluidSettings;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ExtendedFluidType extends FluidType {
	private final Fluid fluid;

	ExtendedFluidType(Identifier id, FluidSettings settings, Fluid fluid) {
		super(id.toString(), settings);
		this.fluid = fluid;
		if (!FluidTypes.REGISTRY.containsId(id)) {
			Registry.register(FluidTypes.REGISTRY, id, this);
		}
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExtendedFluidType)) return false;
		if (!super.equals(o)) return false;
		ExtendedFluidType that = (ExtendedFluidType) o;
		return Objects.equals(this.fluid, that.fluid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.fluid);
	}

	@Override
	public String toString() {
		return "ExtendedFluidType{" + "fluid=" + this.fluid +
				", id='" + this.id + '\'' +
				", settings=" + this.settings +
				'}';
	}
}
