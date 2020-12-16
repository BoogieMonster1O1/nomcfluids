package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.util.Result;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public class FluidStorageContext {
	private final FluidStorage storage;
	private final FluidType fluidType;
	private boolean simulate = false;
	private Side side = Side.UNKNOWN;

	public FluidStorageContext(FluidStorage storage, FluidType fluidType) {
		this.storage = storage;
		this.fluidType = fluidType;
	}

	public FluidStorageContext simulate() {
		this.simulate = true;
		return this;
	}

	public boolean shouldPerform() {
		return !this.simulate;
	}

	public Fraction extract(Fraction amount) {
		Fraction stored = this.storage.getStored(this.fluidType, this.side);
		Fraction maxExtracted = amount.isGreaterThan(stored) ? amount : stored;
		Fraction maxOutput = this.storage.getMaxOutput(this.fluidType, this.side);
		Fraction extracted = maxExtracted.isGreaterThan(maxOutput) ? maxOutput : maxExtracted;
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, stored.withSubtraction(extracted));
		}
		return extracted;
	}

	public Fraction insert(Fraction amount) {
		Fraction stored = this.storage.getStored(this.fluidType, this.side);
		Fraction maxMinus = this.storage.getMaxFluidVolume(this.fluidType).withSubtraction(stored);
		Fraction maxInserted = maxMinus.isGreaterThan(amount) ? amount : maxMinus;
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction inserted = maxInserted.isGreaterThan(maxInput) ? maxInput : maxInserted;
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, stored.withAddition(inserted));
		}
		return inserted;
	}

	public Result set(Fraction amount) {
		if (amount.isNegative()) {
			amount = Fraction.ZERO;
		}
		Fraction max = this.storage.getMaxFluidVolume(this.fluidType);
		if (amount.isGreaterThan(max)) {
			amount = max;
		}
		if (this.shouldPerform()) {
			return this.storage.setStored(this.fluidType, this.side, amount);
		}
		return Result.SUCCESS;
	}

	public Fraction getMaxInput() {
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction maxInserted = this.storage.getMaxFluidVolume(this.fluidType).withSubtraction(this.storage.getStored(this.fluidType, this.side));
		return maxInput.isLessThan(maxInserted) ? maxInput : maxInserted;
	}

	public Fraction getMaxOutput() {
		Fraction maxOutput = this.storage.getMaxOutput(this.fluidType, this.side);
		Fraction maxExtracted = this.storage.getStored(this.fluidType, this.side);
		return maxOutput.isLessThan(maxExtracted) ? maxOutput : maxExtracted;
	}

	public Fraction getStored() {
		return this.storage.getStored(this.fluidType, this.side);
	}

	public Fraction getMaxFluidVolume() {
		return this.storage.getMaxFluidVolume(this.fluidType);
	}

	public FluidTransaction into(FluidStorageContext target) {
		return new FluidTransaction(this, target);
	}

	public FluidStorageContext side(Side side) {
		this.side = side;
		return this;
	}

	public Result consume(Fraction amount) {
		Fraction stored = this.getStored();
		if (stored.isGreaterThanOrEqual(amount)) {
			if (this.shouldPerform()) {
				this.set(stored.withSubtraction(amount));
			}
			return Result.SUCCESS;
		}
		return Result.FAILURE;
	}
}
