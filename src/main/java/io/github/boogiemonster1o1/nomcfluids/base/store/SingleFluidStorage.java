//package io.github.boogiemonster1o1.nomcfluids.base.store;
//
//import java.util.function.BiConsumer;
//
//import io.github.boogiemonster1o1.nomcfluids.api.FluidType;
//import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
//import io.github.boogiemonster1o1.nomcfluids.api.store.FluidRate;
//import io.github.boogiemonster1o1.nomcfluids.api.store.FluidStorage;
//import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
//
///**
// * A {@link FluidStorage} implementation that stores a single fluid
// * type and has a static maximum capacity.
// */
//public class SingleFluidStorage implements FluidStorage {
//	private final FluidType storedFluid;
//	private final Fraction maximum;
//	private final FluidRate fluidRate;
//	private Fraction stored;
//
//	private SingleFluidStorage(FluidType storedFluid, Fraction maximum, Fraction stored, Fraction input, Fraction output) {
//		this.storedFluid = storedFluid;
//		this.maximum = maximum;
//		this.stored = stored;
//		this.fluidRate = SimpleFluidRate.of(input, output);
//	}
//
//	@Override
//	public Fraction getStored(FluidType type, Side side) {
//		if (this.isValid(type)) {
//			return this.stored;
//		}
//		return Fraction.ZERO;
//	}
//
//	@Override
//	public void setStored(FluidType type, Side side, Fraction amount) {
//		this.stored = amount;
//	}
//
//	@Override
//	public boolean isValid(FluidType type) {
//		return type.equals(this.storedFluid);
//	}
//
//	@Override
//	public Fraction getMaxFluidVolume(FluidType type, Side side) {
//		if (this.isValid(type)) {
//			return this.maximum;
//		}
//		return Fraction.ZERO;
//	}
//
//	@Override
//	public FluidRate getRate() {
//		return this.fluidRate;
//	}
//
//	@Override
//	public void forEach(BiConsumer<FluidType, Fraction> consumer, Side side) {
//		consumer.accept(this.storedFluid, this.stored);
//	}
//
//	public static Builder builder() {
//		return new Builder();
//	}
//
//	public static class Builder {
//		private FluidType fluidType;
//		private Fraction maximum;
//		private Fraction defaultStored = Fraction.ZERO;
//		private Fraction input;
//		private Fraction output;
//
//		private Builder() {
//		}
//
//		public Builder fluid(FluidType type) {
//			this.fluidType = type;
//			return this;
//		}
//
//		public Builder input(Fraction input) {
//			this.input = input;
//			return this;
//		}
//
//		public Builder output(Fraction output) {
//			this.output = output;
//			return this;
//		}
//
//		public Builder maximum(Fraction maximum) {
//			this.maximum = maximum;
//			return this;
//		}
//
//		public Builder defaultStored(Fraction defaultStored) {
//			this.defaultStored = defaultStored;
//			return this;
//		}
//
//		public SingleFluidStorage build() {
//			if (this.fluidType == null) {
//				throw new IllegalStateException("Missing fluid type");
//			}
//			if (this.maximum == null) {
//				throw new IllegalStateException("Missing maximum capacity");
//			}
//			if (this.input == null) {
//				throw new IllegalStateException("Missing input rate");
//			}
//			if (this.output == null) {
//				throw new IllegalStateException("Missing output rate");
//			}
//			return new SingleFluidStorage(this.fluidType, this.maximum, this.defaultStored, this.input, this.output);
//		}
//	}
//}
