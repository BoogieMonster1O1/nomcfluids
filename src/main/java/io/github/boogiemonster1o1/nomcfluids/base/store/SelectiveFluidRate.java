package io.github.boogiemonster1o1.nomcfluids.base.store;

import java.util.function.Function;
import java.util.function.ToLongFunction;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidRate;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;

/**
 * A {@link FluidRate} implementation that dynamically gets the i/o
 * rate depending on the {@link FluidType}
 */
public class SelectiveFluidRate implements FluidRate {
	private final Function<FluidType, Fraction> input;
	private final Function<FluidType, Fraction> output;

	public SelectiveFluidRate(Function<FluidType, Fraction> input, Function<FluidType, Fraction> output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public Fraction getMaxInput(FluidType type, Side side) {
		return this.input.apply(type);
	}

	@Override
	public Fraction getMaxOutput(FluidType type, Side side) {
		return this.output.apply(type);
	}

	public static SelectiveFluidRate of(Function<FluidType, Fraction> input, Function<FluidType, Fraction> output) {
		return new SelectiveFluidRate(input, output);
	}

	public static SelectiveFluidRate of(ToLongFunction<FluidType> input, ToLongFunction<FluidType> output) {
		return new SelectiveFluidRate((type) -> Fraction.of(input.applyAsLong(type)), (type) -> Fraction.of(output.applyAsLong(type)));
	}
}
