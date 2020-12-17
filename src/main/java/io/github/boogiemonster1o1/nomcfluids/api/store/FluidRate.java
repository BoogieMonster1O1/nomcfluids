package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

/**
 * Specifies rates of fluid transfer.
 */
public interface FluidRate {
	Fraction getMaxInput(FluidType type, Side side);

	Fraction getMaxOutput(FluidType type, Side side);
}
