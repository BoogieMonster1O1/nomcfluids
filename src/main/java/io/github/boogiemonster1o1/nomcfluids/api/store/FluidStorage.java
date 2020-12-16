package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.util.Result;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public interface FluidStorage extends FluidHolder {
	Fraction getStored(FluidType type, Side side);

	Result setStored(FluidType type, Side side, Fraction amount);

	boolean isValid(FluidType type);
}
