package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public interface FluidHolder {
	/**
	 * @return Returns the maximum amount of fluid that can be stored
	 */
	Fraction getMaxFluidVolume(FluidType type, Side side);

	/**
	 * @return Returns the rate of transfer of this fluid holder
	 */
	FluidRate getRate();

	/**
	 * @param side The direction
	 * @return The maximum input for the specified direction
	 */
	default Fraction getMaxInput(FluidType type, Side side) {
		return this.getRate().getMaxInput(type, side);
	}

	/**
	 * @param side The direction
	 * @return The maximum output for the specified direction
	 */
	default Fraction getMaxOutput(FluidType type, Side side) {
		return this.getRate().getMaxOutput(type, side);
	}
}
