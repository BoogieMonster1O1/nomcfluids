package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public class EmptyFluidTransaction extends FluidTransaction {
	EmptyFluidTransaction() {
		super(null, null);
	}

	@Override
	public Fraction move() {
		System.err.println("Attempted to move fluid in an empty transaction");
		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			System.err.println("\tat " + e);
		}
		return Fraction.ZERO;
	}

	@Override
	public Fraction move(Fraction amount) {
		System.err.println("Attempted to move fluid in an empty transaction");
		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			System.err.println("\tat " + e);
		}
		return Fraction.ZERO;
	}
}
