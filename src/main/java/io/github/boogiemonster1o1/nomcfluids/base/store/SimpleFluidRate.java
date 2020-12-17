package io.github.boogiemonster1o1.nomcfluids.base.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidRate;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;

/**
 * A {@link FluidRate} implementation that uses static values.
 */
public class SimpleFluidRate implements FluidRate {
	private final Fraction input;
	private final Fraction output;

	private SimpleFluidRate(Fraction input, Fraction output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public Fraction getMaxInput(FluidType type, Side side) {
		return this.input;
	}

	@Override
	public Fraction getMaxOutput(FluidType type, Side side) {
		return this.output;
	}

	public static SimpleFluidRate of(long input, long output) {
		return new SimpleFluidRate(Fraction.of(input), Fraction.of(output));
	}

	public static SimpleFluidRate of(Fraction input, Fraction output) {
		return new SimpleFluidRate(input, output);
	}

	public static SimpleFluidRate of(Number input, Number output) {
		return of(input.longValue(), output.longValue());
	}
}
