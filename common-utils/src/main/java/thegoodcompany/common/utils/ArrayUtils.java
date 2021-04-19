/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    private ArrayUtils() { }

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
}
