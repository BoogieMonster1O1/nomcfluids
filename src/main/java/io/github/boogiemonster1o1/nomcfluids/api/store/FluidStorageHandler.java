package io.github.boogiemonster1o1.nomcfluids.api.store;

import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
import io.github.boogiemonster1o1.nomcfluids.api.util.Result;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;

public class FluidStorageHandler {
	private final FluidStorage storage;
	private final FluidType fluidType;
	private final boolean valid;
	private boolean simulate = false;
	private Side side = Side.UNKNOWN;

	public FluidStorageHandler(FluidStorage storage, FluidType fluidType) {
		this.storage = storage;
		this.fluidType = fluidType;
		this.valid = storage.isValid(fluidType);
	}

	public FluidStorageHandler simulate() {
		this.simulate = true;
		return this;
	}

	public boolean shouldPerform() {
		return !this.simulate;
	}

	public Fraction extract(Fraction amount) {
		if (!this.valid) {
			return Fraction.ZERO;
		}
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
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction stored = this.storage.getStored(this.fluidType, this.side);
		Fraction maxMinus = this.storage.getMaxFluidVolume(this.fluidType, this.side).withSubtraction(stored);
		Fraction maxInserted = maxMinus.isGreaterThan(amount) ? amount : maxMinus;
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction inserted = maxInserted.isGreaterThan(maxInput) ? maxInput : maxInserted;
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, stored.withAddition(inserted));
		}
		return inserted;
	}

	public Result set(Fraction amount) {
		if (!this.valid) {
			return Result.FAILURE;
		}
		if (amount.isNegative()) {
			amount = Fraction.ZERO;
		}
		Fraction max = this.storage.getMaxFluidVolume(this.fluidType, this.side);
		if (amount.isGreaterThan(max)) {
			amount = max;
		}
		if (this.shouldPerform()) {
			return this.storage.setStored(this.fluidType, this.side, amount);
		}
		return Result.SUCCESS;
	}

	public Fraction getMaxInput() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction maxInserted = this.storage.getMaxFluidVolume(this.fluidType, this.side).withSubtraction(this.storage.getStored(this.fluidType, this.side));
		return maxInput.isLessThan(maxInserted) ? maxInput : maxInserted;
	}

	public Fraction getMaxOutput() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction maxOutput = this.storage.getMaxOutput(this.fluidType, this.side);
		Fraction maxExtracted = this.storage.getStored(this.fluidType, this.side);
		return maxOutput.isLessThan(maxExtracted) ? maxOutput : maxExtracted;
	}

	public Fraction getStored() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		return this.storage.getStored(this.fluidType, this.side);
	}

	public Fraction getMaxFluidVolume() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		return this.storage.getMaxFluidVolume(this.fluidType, this.side);
	}

	public FluidTransaction into(FluidStorageHandler target) {
		if (!this.valid) {
			return new EmptyFluidTransaction();
		}
		return new FluidTransaction(this, target);
	}

	public FluidStorageHandler side(Side side) {
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
