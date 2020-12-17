package io.github.boogiemonster1o1.nomcfluids.api.store;

import java.util.function.BiConsumer;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;

/**
 * A container that stores fluid.
 */
public interface FluidStorage extends FluidHolder {
	Fraction getStored(FluidType type, Side side);

	void setStored(FluidType type, Side side, Fraction amount);

	boolean isValid(FluidType type);

	void forEach(BiConsumer<FluidType, Fraction> consumer, Side side);
}
