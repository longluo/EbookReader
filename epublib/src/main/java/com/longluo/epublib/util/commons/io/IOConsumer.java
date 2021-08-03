package com.longluo.epublib.util.commons.io;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Like {@link Consumer} but throws {@link IOException}.
 *
 * @param <T> the type of the input to the operations.
 * @since 2.7
 */
@FunctionalInterface
public interface IOConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws IOException if an I/O error occurs.
     */
    void accept(T t) throws IOException;

    /**
     * Returns a composed {@code IoConsumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this operation followed by the {@code after}
     *         operation
     * @throws NullPointerException if {@code after} is null
     */
    @SuppressWarnings("unused")
    default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (final T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
