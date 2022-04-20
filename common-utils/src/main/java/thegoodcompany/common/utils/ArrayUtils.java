/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Predicate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    private ArrayUtils() { }

    /**
     * Combines an array of arrays into one array
     *
     * @param ts array of arrays to combine
     * @param <T> type of value the arrays hold
     * @return a new array that is super set of the given arrays
     */
    @NonNull
    @SafeVarargs
    public static <T> T[] combine(@NonNull T[]... ts) {
        int totalLen = 0;
        for (T[] t : ts) {
            totalLen += t.length;
        }

        T[] combined = Arrays.copyOf(ts[0], totalLen);
        int count = ts[0].length;
        for (int i = 1, tsLength = ts.length; i < tsLength; i++) {
            T[] t = ts[i];
            int tLen = t.length;

            System.arraycopy(t, 0, combined, count, tLen);
            count += tLen;
        }

        return combined;
    }

    /**
     * Returns a up casted new unmodifiable List that is backed by the original list.
     *
     * @param original list to up cast
     * @param <T> the 'up cast to' type
     * @return a new immutable list
     */
    @NonNull
    public static <T> List<T> upCastListBacked(@NonNull List<? extends T> original) {
        return upCastListBacked(original, null);
    }

    /**
     * Returns a up casted new List that is backed by the original list. Modifications to
     * this list is only possible if that modification is complied w/ original list
     *
     * @param original list to up cast
     * @param <T> the 'up cast to' type
     * @return a new up casted list
     */
    @NonNull
    public static <T, R extends T> List<T> upCastListBacked(@NonNull List<R> original, @Nullable Class<R> rClass) {
        return new UpCastList<>(original, rClass);
    }

    @NonNull
    public static <T> List<T> upCastList(@NonNull List<? extends T> original) {
        return new ArrayList<>(original);
    }

    /**
     * An unmodifiable list that is backed by the original list and acts as
     * if its elements were <code>T</code> typed
     *
     * @param <T> the "up cast to" type
     */
    private static class UpCastList<T, F extends T> extends AbstractList<T> {
        private final List<F> mOriginal;
        @Nullable private final Class<F> fClass;

        UpCastList(List<F> original, @Nullable Class<F> fClass) {
            mOriginal = original;
            this.fClass = fClass;
        }

        @Override
        public T get(int index) {
            return mOriginal.get(index);
        }

        @Override
        public T set(int index, T element) {
            return mOriginal.set(index, downCast(element));
        }

        @Override
        public void add(int index, T element) {
            mOriginal.add(index, downCast(element));
        }

        @Override
        public T remove(int index) {
            return mOriginal.remove(index);
        }

        private F downCast(T t) {
            if (fClass != null && fClass.isInstance(t)) {
                return fClass.cast(t);
            }

            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return mOriginal.size();
        }
    }

    /**
     * @see #transform(List, ArrayTransformer)
     */
    @NonNull
    public static <T> List<T> transform(@NonNull int[] ints, @NonNull ArrayTransformer<Integer, T> transformer) {
        return transform(NumberUtils.toBoxed(ints), transformer);
    }

    /**
     * @see #transform(List, ArrayTransformer)
     */
    @NonNull
    public static <T> List<T> transform(@NonNull long[] longs, @NonNull ArrayTransformer<Long, T> transformer) {
        return transform(NumberUtils.toBoxed(longs), transformer);
    }

    /**
     * @see #transform(List, ArrayTransformer)
     */
    @NonNull
    public static <T> List<T> transform(@NonNull double[] longs, @NonNull ArrayTransformer<Double, T> transformer) {
        return transform(NumberUtils.toBoxed(longs), transformer);
    }

    /**
     * @see #transform(List, ArrayTransformer)
     */
    @NonNull
    public static <F, T> List<T> transform(@NonNull F[] fs, @NonNull ArrayTransformer<F, T> transformer) {
        return transform(Arrays.asList(fs), transformer);
    }

    /**
     * Accepts a transformer function and invokes it with every element from {@code src} then returns a new
     * {@link List} containing return values of {@code transformer}
     *
     * @param src the source list
     * @param transformer a transformer function that will be invoked on every element from {@code src}
     * @param <F> type of elements the source list holds
     * @param <T> type of elements the returned list hold
     * @return a new list containing all the returns values from {@code transformer}
     */
    @NonNull
    public static <F, T> List<T> transform(@NonNull List<F> src, @NonNull ArrayTransformer<F, T> transformer) {
        int size = src.size();
        List<T> tos = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            F f = src.get(i);
            tos.add(transformer.transform(f, i, src));
        }

        return tos;
    }

    public static <T> boolean every(@NonNull List<T> list, @NonNull Predicate<T> predicate) {
        for (T t : list)
            if (!predicate.test(t)) return false;

        return true;
    }

    public static <T> boolean some(@NonNull List<T> list, @NonNull Predicate<T> predicate) {
        for (T t : list)
            if (predicate.test(t)) return true;

        return false;
    }

    public interface ArrayTransformer<F, T> {
        T transform(F t, int index, List<F> source);
    }
}
