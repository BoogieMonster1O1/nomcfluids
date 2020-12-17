package io.github.boogiemonster1o1.nomcfluids.api;

import java.awt.Color;
import java.util.function.BiConsumer;

import io.github.boogiemonster1o1.nomcfluids.api.fraction.Fraction;
import io.github.boogiemonster1o1.nomcfluids.api.settings.FluidSettings;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidRate;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidStorage;
import io.github.boogiemonster1o1.nomcfluids.api.util.Side;
import org.junit.jupiter.api.Test;

public class FluidTransactionTest {
	@Test
	public void testTransaction() {
		Fraction max = new Fraction(1000);
		Fraction twenty = new Fraction(20);
		FluidType water = new FluidType("water", FluidSettings.builder().luminance(0).color(Color.BLUE).build());
		FluidType lava = new FluidType("lava", FluidSettings.builder().density(1.4F).luminance(15).color(Color.ORANGE).build());

		class FluidContainer implements FluidStorage {
			@Override
			public Fraction getStored(FluidType type, Side side) {
				return null;
			}

			@Override
			public void setStored(FluidType type, Side side, Fraction amount) {

			}

			@Override
			public boolean isValid(FluidType type) {
				return type.equals(water) || type.equals(lava);
			}

			@Override
			public Fraction getMaxFluidVolume(FluidType type, Side side) {
				return max;
			}

			@Override
			public FluidRate getRate() {
				return new FluidRate() {
					@Override
					public Fraction getMaxInput(FluidType type, Side side) {
						return twenty;
					}

					@Override
					public Fraction getMaxOutput(FluidType type, Side side) {
						return twenty;
					}
				};
			}

			@Override
			public void forEach(BiConsumer<FluidType, Fraction> consumer, Side side) {

			}
		}
	}
}
