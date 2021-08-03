package com.longluo.util;

public class Pair<T1, T2> {
    public final T1 First;
    public final T2 Second;

    public Pair(T1 first, T2 second) {
        First = first;
        Second = second;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Pair)) {
            return false;
        }
        final Pair pair = (Pair) other;
        return
                ComparisonUtil.equal(First, pair.First) &&
                        ComparisonUtil.equal(Second, pair.Second);
    }

    @Override
    public int hashCode() {
        return ComparisonUtil.hashCode(First) + 23 * ComparisonUtil.hashCode(Second);
    }
}
