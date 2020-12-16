package io.github.boogiemonster1o1.nomcfluids.api.store;

import java.util.function.BooleanSupplier;

import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public final class FluidTransaction {
	private final FluidStorageContext source;
	private final FluidStorageContext target;
	private boolean simulate = false;
	private boolean disabled = false;

	FluidTransaction(FluidStorageContext source, FluidStorageContext target) {
		this.source = source;
		this.target = target;
	}

	public FluidTransaction simulate() {
		this.simulate = true;
		return this;
	}

	public Fraction move() {
		return this.move(Fraction.MAX_VALUE);
	}

	public FluidTransaction onlyIf(BooleanSupplier booleanSupplier) {
		if (!booleanSupplier.getAsBoolean()) {
			this.disabled = true;
		}
		return this;
	}

	public Fraction move(Fraction amount) {
		if (this.disabled) {
			return Fraction.ZERO;
		} else if (this.simulate || !this.source.shouldPerform() || !this.target.shouldPerform()) {
			this.simulate = true;
			this.source.simulate();
			this.target.simulate();
		}
		Fraction targetMaxInput = this.target.getMaxInput();
		Fraction sourceMaxOutput = this.source.getMaxOutput();
		Fraction maxInserted = sourceMaxOutput.isLessThan(amount) ? sourceMaxOutput : amount;
		Fraction maxMove = targetMaxInput.isLessThan(maxInserted) ? targetMaxInput : maxInserted;
		if (maxMove.isNegative()) {
			return Fraction.ZERO;
		}
		return this.target.insert(this.source.extract(amount));
	}
}
