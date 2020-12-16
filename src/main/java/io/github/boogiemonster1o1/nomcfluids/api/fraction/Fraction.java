package io.github.boogiemonster1o1.nomcfluids.api.fraction;

import java.util.Objects;

/**
 * Immutable, full-resolution rational number representation.
 */
@SuppressWarnings({"CopyConstructorMissesField", "unused"})
public class Fraction implements Comparable<Fraction> {
	public static final Fraction ZERO = Fraction.of(0, 0, 1);
	public static final Fraction ONE_THIRD = Fraction.of(0, 1,3);
	public static final Fraction HALF = Fraction.of(0, 1, 2);
	public static final Fraction ONE = Fraction.of(1, 0, 1);
	public static final Fraction ONE_THOUSAND = Fraction.of(1000, 0, 1);
	public static final Fraction MAX_VALUE = Fraction.of(Long.MAX_VALUE, 0, 1);

	protected long whole;
	protected long numerator;
	protected long divisor;

	/**
	 * Constructs a new fraction with value of zero.
	 * Generally better to use {@link #ZERO} instead.
	 */
	public Fraction() {
		this(0, 0, 1);
	}

	public Fraction(long whole, long numerator, long divisor) {
		this.validate(whole, numerator, divisor);
		this.whole = whole;
		this.numerator = numerator;
		this.divisor = divisor;
		this.normalize();
	}

	public Fraction(long numerator, long divisor) {
		this.validate(0, numerator, divisor);
		this.whole = numerator / divisor;
		this.numerator = numerator - this.whole * divisor;
		this.divisor = divisor;
		this.normalize();
	}

//	/**
//	 * Deserializes an new instance from an NBT tag previously returned by {@link #toTag()}.
//	 *
//	 * @param tag NBT tag previously returned by {@link #toTag()
//	 */
//	public Fraction(Tag tag) {
//		readTagInner((CompoundTag) tag);
//	}
//
//	/**
//	 * Deserializes a new instance from previously encoded to a packet buffer by {@link #writeBuffer(PacketByteBuf)}
//	 *
//	 * @param buf packet buffer containing data encoded by {@link #writeBuffer(PacketByteBuf)}
//	 */
//	public Fraction(PacketByteBuf buf) {
//		readBufferInner(buf);
//	}

	public Fraction(long whole) {
		this(whole, 0, 1);
	}

	public Fraction(Fraction template) {
		this(template.whole(), template.numerator(), template.divisor());
	}

	/**
	 * The whole-number portion of this fraction.
	 *
	 * If this fraction is negative, both {@code whole()} and {@link #numerator()}
	 * will be zero or negative.
	 *
	 * @return The whole-number portion of this fraction
	 */
	public final long whole() {
		return this.whole;
	}

	/**
	 * The fractional portion of this fraction, or zero if the fraction
	 * represents a whole number.<p>
	 *
	 * If this fraction is negative, both {@link #whole()} and {@code numerator()}
	 * will be zero or negative.<p>
	 *
	 * The absolute values of {@code numerator()} will always be zero
	 * or less than the value of {@link #divisor()}. Whole numbers are
	 * always fully represented in {@link #whole()}.
	 *
	 * @return The whole-number portion of this fraction
	 */
	public final long numerator() {
		return this.numerator;
	}

	/**
	 * The denominator for the fractional portion of this fraction.
	 * Will always be >= 1.
	 *
	 * @return the denominator for the fractional portion of this fraction
	 */
	public final long divisor() {
		return this.divisor;
	}

//	/**
//	 * Serializes this instance to the given packet buffer.
//	 *
//	 * @param buffer packet buffer to receive serialized data
//	 */
//	public final void writeBuffer(PacketByteBuf buffer) {
//		buffer.writeVarLong(whole);
//		buffer.writeVarLong(numerator);
//		buffer.writeVarLong(divisor);
//	}

//	/**
//	 * Serializes this instance in an NBT compound tag without
//	 * creating a new tag instance. This is meant for use cases
//	 * (mostly internal to Fluidity) where key collision is not a risk.
//	 *
//	 * @param tag NBT tag to contain serialized data
//	 */
//	public final void writeTag(CompoundTag tag) {
//		tag.putLong("whole", whole);
//		tag.putLong("numerator", numerator);
//		tag.putLong("denominator", divisor);
//	}

//	/**
//	 * Serializes this instance to a new NBT tag.
//	 *
//	 * @return new tag instance containing serialized data
//	 */
//	public final Tag toTag() {
//		final CompoundTag result = new CompoundTag();
//		writeTag(result);
//		return result;
//	}
//
//	protected final void readBufferInner(PacketByteBuf buf) {
//		whole = buf.readVarLong();
//		numerator = buf.readVarLong();
//		divisor = buf.readVarLong();
//		normalize();
//	}
//
//	protected final void readTagInner(CompoundTag tag) {
//		whole = tag.getLong("whole");
//		numerator = tag.getLong("numerator");
//		divisor = Math.max(1, tag.getLong("denominator"));
//		normalize();
//	}

	@Override
	public final boolean equals(Object val) {
		if (!(val instanceof Fraction)) {
			return false;
		}

		return this.compareTo((Fraction) val) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.whole, this.numerator, this.divisor);
	}

	protected final void validate(long whole, long numerator, long divisor) {
		if (divisor < 1) {
			throw new IllegalArgumentException("Fraction divisor must be >= 1");
		}
	}

	@Override
	public final String toString() {
		return String.format("%d and %d / %d, approx: %f", this.whole, this.numerator, this.divisor, this.toDouble());
	}

	protected final void normalize() {
		if (Math.abs(this.numerator) >= this.divisor) {
			final long w = this.numerator / this.divisor;
			this.whole += w;
			this.numerator -= w * this.divisor;
		}

		if (this.numerator == 0) {
			this.divisor = 1;
			return;
		}

		// keep signs consistent
		if (this.whole < 0) {
			if (this.numerator > 0) {
				this.whole += 1;
				this.numerator -= this.divisor;
			}
		} else if (this.numerator < 0) {
			if (this.whole > 0) {
				this.whole -= 1;
				this.numerator += this.divisor;
			}
		}

		// remove powers of two bitwise
		final int twos = Long.numberOfTrailingZeros(this.numerator | this.divisor);

		if (twos > 0) {
			this.numerator >>= twos;
			this.divisor >>= twos;
		}

		// use conventional gcd for rest
		final long gcd = this.gcd(Math.abs(this.numerator), this.divisor);

		if (gcd != this.divisor) {
			this.numerator /= gcd;
			this.divisor /= gcd;
		}
	}

	protected final long gcd(long a, long b) {
		while (b != 0) {
			final long t = b;
			b = a % b;
			a = t;
		}

		return a;
	}

	/**
	 * Intended for user display. Result may be approximate due to floating point error.
	 *
	 * @param units Fraction of one that counts as 1 in the result. Must be >= 1.
	 * @return Current value scaled so that that 1.0 = one of the given units
	 */
	public final double toDouble(long units) {
		// start with unit scale
		final double base = (double) this.numerator() / (double) this.divisor() + this.whole();

		// scale to requested unit
		return units == 1 ? base : base / units;
	}

	/**
	 * Intended for user display. Result may be approximate due to floating point error.
	 *
	 * @return This fraction as a {@code double} primitive
	 */
	public final double toDouble() {
		return this.toDouble(1);
	}

	/**
	 * Returns the number of units that is less than or equal to the given unit.
	 * Make be larger than this if value is not evenly divisible .
	 *
	 * @param divisor Fraction of one bucket that counts as 1 in the result. Must be >= 1.
	 * @return Number of units within current volume.
	 */
	public final long toLong(long divisor) {
		if (divisor < 1) {
			throw new IllegalArgumentException("RationalNumber divisor must be >= 1");
		}

		final long base = this.whole() * divisor;

		if (this.numerator() == 0) {
			return base;
		} else if (this.divisor() == divisor) {
			return base + this.numerator();
		} else {
			return base + this.numerator() * divisor / this.divisor();
		}
	}

	/**
	 * Test if this fraction is exactly zero.
	 *
	 * @return {@code true} if this fraction is exactly zero
	 */
	public final boolean isZero() {
		return this.whole() == 0 && this.numerator() == 0;
	}

	/**
	 * Test if this fraction is a negative number.
	 *
	 * @return {@code true} if this fraction is a negative number
	 */
	public final boolean isNegative() {
		return this.whole() < 0 || (this.whole() == 0 && this.numerator() < 0);
	}

	@Override
	public final int compareTo(Fraction o) {
		final int result = Long.compare(this.whole(), o.whole());
		return result == 0 ? Long.compare(this.numerator() * o.divisor(), o.numerator() * this.divisor()) : result;
	}

	public final boolean isGreaterThan(Fraction other) {
		return this.compareTo(other) > 0;
	}

	public final boolean isGreaterThanOrEqual(Fraction other) {
		return this.compareTo(other) >= 0;
	}

	public final boolean isLessThan(Fraction other) {
		return this.compareTo(other) < 0;
	}

	public final boolean isLessThanOrEqual(Fraction other) {
		return this.compareTo(other) <= 0;
	}

	/**
	 * Ensures this instance is safe to retain. Should always be
	 * called for any {@code Fraction} instance that will be retained
	 * unless the instance is already known to be immutable.
	 *
	 * @return a new immutable {@code Fraction} instance if this is a {@code MutableFraction}, or this instance otherwise.
	 */
	Fraction toImmutable() {
		return this;
	}

	/**
	 * The smallest whole number that is greater than or equal to the
	 * rational number represented by this fraction.
	 *
	 * @return the smallest whole number greater than or equal to this fraction
	 */
	public final long ceil() {
		return this.numerator() == 0 || this.whole() < 0 ? this.whole() : this.whole() + 1;
	}

	/**
	 * Returns a new value equal to this fraction multiplied by -1.
	 *
	 * @return A new fraction equal to this fraction multiplied by -1
	 */
	public final Fraction toNegated() {
		return Fraction.of(-this.whole(), -this.numerator(), this.divisor());
	}

	/**
	 * Returns a new value equal to this value less the given parameter.
	 *
	 * This method is allocating and for frequent and repetitive operations
	 * it will be preferable to use a mutable fraction instance.
	 *
	 * @param diff value to be subtracted from this value
	 * @return a new value equal to this value less the given parameter
	 */
	public final Fraction withSubtraction(Fraction diff) {
		final MutableFraction f = new MutableFraction(this);
		f.subtract(diff);
		return f.toImmutable();
	}

	/**
	 * Returns a new value equal to this value plus the given parameter.
	 *
	 * This method is allocating and for frequent and repetitive operations
	 * it will be preferable to use a mutable fraction instance.
	 *
	 * @param diff value to be added to this value
	 * @return a new value equal to this value plus the given parameter
	 */
	public final Fraction withAddition(Fraction diff) {
		final MutableFraction f = new MutableFraction(this);
		f.add(diff);
		return f.toImmutable();
	}

	public static Fraction of(long whole, long numerator, long divisor) {
		return new Fraction(whole, numerator, divisor);
	}

	public static Fraction of(long numerator, long divisor) {
		return new Fraction(numerator, divisor);
	}

	public static Fraction of(long whole) {
		return new Fraction(whole);
	}

	@SuppressWarnings({"unused", "UnusedReturnValue"})
	private static final class MutableFraction extends Fraction {
		public MutableFraction() {
			super();
		}

		public MutableFraction(long whole) {
			super(whole, 0, 1);
		}

		public MutableFraction(long numerator, long divisor) {
			super(numerator, divisor);
		}

		public MutableFraction(long whole, long numerator, long divisor) {
			super(whole, numerator, divisor);
		}

		public MutableFraction(Fraction template) {
			super(template.whole(), template.numerator(), template.divisor());
		}

		public MutableFraction set(long whole) {
			return this.set(whole, 0, 1);
		}

		public MutableFraction set(long numerator, long divisor) {
			this.validate(0, numerator, divisor);
			this.whole = numerator / divisor;
			this.numerator = numerator - this.whole * divisor;
			this.divisor = divisor;
			return this;
		}

		public MutableFraction set(long whole, long numerator, long divisor) {
			this.validate(whole, numerator, divisor);
			this.whole = whole;
			this.numerator = numerator;
			this.divisor = divisor;
			return this;
		}

		public MutableFraction set(Fraction template) {
			this.whole = template.whole();
			this.numerator = template.numerator();
			this.divisor = template.divisor();
			return this;
		}

		public MutableFraction add(Fraction val) {
			return this.add(val.whole(), val.numerator(), val.divisor());
		}

		public MutableFraction add(long whole) {
			return this.add(whole, 0, 1);
		}

		public MutableFraction add(long numerator, long divisor) {
			return this.add(0, numerator, divisor);
		}

		public MutableFraction add(long whole, long numerator, long divisor) {
			this.validate(whole, numerator, divisor);
			this.whole += whole;

			if (Math.abs(numerator) >= divisor) {
				final long w = numerator / divisor;
				this.whole += w;
				numerator -= w * divisor;
			}

			final long n = this.numerator * divisor + numerator * this.divisor;

			if (n == 0) {
				this.numerator = 0;
				this.divisor = 1;
			} else {
				this.numerator = n;
				this.divisor = divisor * this.divisor;
				this.normalize();
			}

			return this;
		}

		public MutableFraction multiply(Fraction val) {
			return this.multiply(val.whole(), val.numerator(), val.divisor());
		}

		public MutableFraction multiply(long whole) {
			this.numerator *= whole;
			this.whole *= whole;
			this.normalize();
			return this;
		}

		public MutableFraction multiply(long numerator, long divisor) {
			return this.multiply(0, numerator, divisor);
		}

		public MutableFraction multiply(long whole, long numerator, long divisor) {
			if (numerator == 0) {
				return this.multiply(whole);
			}

			this.validate(whole, numerator, divisor);

			// normalize fractional part
			if (Math.abs(numerator) >= divisor) {
				final long w = numerator / divisor;
				whole += w;
				numerator -= w * divisor;
			}

			// avoids a division later to factor out common divisor from the two steps that follow this
			final long numeratorProduct = numerator * this.numerator;
			this.numerator *= divisor;
			numerator *= this.divisor;

			this.divisor *= divisor;
			this.numerator = this.numerator * whole + numerator * this.whole + numeratorProduct;
			this.whole *= whole;
			this.normalize();

			return this;
		}

		public MutableFraction subtract(Fraction val) {
			return this.add(-val.whole(), -val.numerator(), val.divisor());
		}

		public MutableFraction subtract(long whole) {
			return this.add(-whole, 0, 1);
		}

		public MutableFraction subtract(long numerator, long divisor) {
			return this.add(0, -numerator, divisor);
		}

		public MutableFraction negate() {
			this.numerator = -this.numerator;
			this.whole = -this.whole;
			return this;
		}

		/**
		 * Rounds down to multiple of divisor if not already divisible by it.
		 *
		 * @param divisor Desired multiple
		 */
		public void roundDown(long divisor) {
			if(this.divisor != divisor) {
				this.set(this.whole, this.numerator * divisor / this.divisor, divisor);
			}
		}

		public static MutableFraction of(long whole, long numerator, long divisor) {
			return new MutableFraction(whole, numerator, divisor);
		}

		public static MutableFraction of(long numerator, long divisor) {
			return new MutableFraction(numerator, divisor);
		}

		public static MutableFraction of(long whole) {
			return new MutableFraction(whole);
		}

		@Override
		Fraction toImmutable() {
			return Fraction.of(this.whole(), this.numerator(), this.divisor());
		}
	}
}
