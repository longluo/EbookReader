package com.longluo.zlibrary.core.util;

public class RationalNumber {
	public static RationalNumber create(long numerator, long denominator) {
		if (denominator == 0) {
			return null;
		}
		return new RationalNumber(numerator, denominator);
	}

	public final long Numerator;
	public final long Denominator;

	private RationalNumber(long numerator, long denominator) {
		final long gcd = GCD(numerator, denominator);
		if (gcd > 1) {
			numerator /= gcd;
			denominator /= gcd;
		}
		if (denominator < 0) {
			numerator = -numerator;
			denominator = -denominator;
		}
		Numerator = numerator;
		Denominator = denominator;
	}

	public float toFloat() {
		return 1.0f * Numerator / Denominator;
	}

	private long GCD(long a, long b) {
		if (a < 0) {
			a = -a;
		}
		if (b < 0) {
			b = -b;
		}
		while (a != 0 && b != 0) {
			if (a > b) {
				a = a % b;
			} else {
				b = b % a;
			}
		}
		return a + b;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RationalNumber)) {
			return false;
		}
		final RationalNumber otherNumber = (RationalNumber)other;
		return otherNumber.Numerator == Numerator && otherNumber.Denominator == Denominator;
	}

	@Override
	public int hashCode() {
		return (int)(37 * Numerator + Denominator);
	}
}
