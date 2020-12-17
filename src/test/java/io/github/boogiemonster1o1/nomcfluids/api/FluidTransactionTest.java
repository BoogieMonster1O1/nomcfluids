package io.github.boogiemonster1o1.nomcfluids.api;

import java.awt.Color;
import java.util.function.BiConsumer;

import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import io.github.boogiemonster1o1.nomcfluids.api.settings.FluidSettings;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidRate;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidStorage;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import io.github.boogiemonster1o1.nomcfluids.base.store.SimpleFluidRate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluidTransactionTest {
	@Test
	public void testSingleTransaction() {
		Fraction max = new Fraction(1000);
		Fraction twenty = new Fraction(20);
		FluidType water = new FluidType("water", FluidSettings.builder().luminance(0).color(Color.BLUE).build());
		class FluidContainer implements FluidStorage {
			private Fraction stored;
			private final FluidRate fluidRate = SimpleFluidRate.of(twenty, twenty);

			public FluidContainer(Fraction stored) {
				this.stored = stored;
			}

			@Override
			public Fraction getStored(FluidType type, Side side) {
				return this.stored;
			}

			@Override
			public void setStored(FluidType type, Side side, Fraction amount) {
				this.stored = amount;
			}

			@Override
			public boolean isValid(FluidType type) {
				return type.equals(water);
			}

			@Override
			public Fraction getMaxFluidVolume(FluidType type, Side side) {
				return max;
			}

			@Override
			public FluidRate getRate() {
				return this.fluidRate;
			}

			@Override
			public void forEach(BiConsumer<FluidType, Fraction> consumer, Side side) {
				consumer.accept(water, this.stored);
			}
		}

		FluidContainer first = new FluidContainer(Fraction.ONE_THOUSAND);
		FluidContainer second = new FluidContainer(Fraction.ZERO);
		long setStart = System.nanoTime();
		FluidHandlers.of(first, water).set(Fraction.ONE_HUNDRED);
		System.out.println("Setting: " + (System.nanoTime() - setStart));
		assertEquals(FluidHandlers.of(first, water).getStored().longValue(), 100L);
		long moveStart = System.nanoTime();
		FluidHandlers.of(first, water).into(FluidHandlers.of(second, water)).move();
		System.out.println("Moving: " + (System.nanoTime() - moveStart));
		assertEquals(80L, FluidHandlers.of(first, water).getStored().longValue());
		assertEquals(20L, FluidHandlers.of(second, water).getStored().longValue());
	}
}
