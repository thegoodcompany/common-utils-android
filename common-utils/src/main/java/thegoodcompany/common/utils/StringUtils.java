/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static thegoodcompany.common.utils.NumberUtils.toBoxed;

public class StringUtils {
    public static final int MAX_LEN_EMAIL = 254;

    private StringUtils() { }

    /**
     * Returns a new string made by joining each element from {@code elements} separated
     * by <pre>, </pre>
     *
     * @see #join(CharSequence, CharSequence...)
     */
    @NonNull
    public static String join(@NonNull List<CharSequence> elements) {
        return join(", ", elements);
    }

    /**
     * Returns a new string composed of the elements from {@code elements} separated
     * by {@code delimiter}
     *
     * @see #join(CharSequence, CharSequence...)
     */
    @NonNull
    public static String join(@NonNull CharSequence delimiter, @NonNull List<CharSequence> elements) {
        return join(delimiter, elements.toArray(new CharSequence[]{}));
    }

    /**
     * Returns a new string containing the the elements from {@code elements} joined
     * together by {@code delimiter}
     *
     * Note that {@code null} elements are represented as the word null.
     *
     * @param delimiter the delimiter that separates each element
     * @param elements the elements that will be joined together
     * @return a new string composed of each element from {@code elements} separated
     * by {@code delimiter}
     */
    @NonNull
    public static String join(@NonNull CharSequence delimiter, @NonNull CharSequence... elements) {
        return joinInternal(elements, delimiter, (element, index) -> String.valueOf(element));
    }

    @NonNull
    public static <T> String joinUnless(@NonNull T[] values, @NonNull CharSequence delimiter, T unless) {
        return joinInternal(values, delimiter, (element, index) -> {
            if (ObjectUtils.equals(element, unless)) return null;
            return String.valueOf(element);
        });
    }

    @NonNull
    public static String joinUnless(@NonNull Resources res, @NonNull int[] values, @NonNull int[] pluralAppendants, CharSequence separator, int unless) {
        return joinUnless(res, toBoxed(values), pluralAppendants, separator, unless);
    }

    @NonNull
    public static String joinUnless(@NonNull Resources res, @NonNull long[] values, @NonNull int[] pluralAppendants, CharSequence separator, long unless) {
        return joinUnless(res, toBoxed(values), pluralAppendants, separator, unless);
    }

    @NonNull
    public static String joinUnless(@NonNull Resources res, @NonNull double[] values, @NonNull int[] pluralAppendants, CharSequence separator, double unless) {
        return joinUnless(res, toBoxed(values), pluralAppendants, separator, unless);
    }

    /**
     * Joins a list of number values into a single string if and only if that value is not equal to {@code unless}
     * Each element is appended by their corresponding {@code appendants} followed by the {@code separator},
     * <p>
     * NOTE THAT no additional arguments will be passed to the quantity strings
     *
     * @param res              Android resource
     * @param values           list of integers which to be joined
     * @param pluralAppendants list of android quantity string ids
     * @param separator        the delimiter that separates each element (along with
     *                         their appendants)
     * @param unless           exception when joining will be skipped
     * @return the joined string
     */
    @NonNull
    public static <T extends Number> String joinUnless(@NonNull Resources res,
                                                       @NonNull T[] values,
                                                       @NonNull int[] pluralAppendants,
                                                       CharSequence separator,
                                                       T unless) {

        return joinInternal(values, separator, (element, index) -> {
            if (ObjectUtils.equals(element, unless)) return null;

            int quantity;
            if (element == null) quantity = 0;
            else if (element.longValue() > Integer.MAX_VALUE) quantity = Integer.MAX_VALUE;
            else if (element.longValue() < Integer.MIN_VALUE) quantity = Integer.MIN_VALUE;
            else quantity = element.intValue();

            return element + res.getQuantityString(pluralAppendants[index], quantity);
        });
    }

    @NonNull
    public static String joinUnless(@NonNull int[] elements, @NonNull CharSequence[] appendants, CharSequence delimiter, int unless) {
        Integer[] boxedElements = toBoxed(elements);
        return joinUnless(boxedElements, appendants, delimiter, unless);
    }

    @NonNull
    public static String joinUnless(@NonNull long[] elements, @NonNull CharSequence[] appendants, CharSequence delimiter, long unless) {
        Long[] boxedElements = toBoxed(elements);
        return joinUnless(boxedElements, appendants, delimiter, unless);
    }

    @NonNull
    public static String joinUnless(@NonNull double[] elements, @NonNull CharSequence[] appendants, CharSequence delimiter, double unless) {
        Double[] boxedElements = toBoxed(elements);
        return joinUnless(boxedElements, appendants, delimiter, unless);
    }

    /**
     * Joins each elements of an array together separated by {@code delimiter} if
     * and only if that element is not equal to {@code unless}. In the process, it
     * also appends every element by their corresponding {@code appendants}
     *
     * @param elements array of elements that will be joined together
     * @param appendants corresponding appendants will be added at the end of each element
     * @param delimiter separator of each element
     * @param unless the exception value, if an element is equal to this argument, it will be untouched
     * @param <T> the type of elements
     * @return a new string composed of eligible elements appended by their {@code appendants} and separated by {@code delimiter}
     */
    @NonNull
    public static <T> String joinUnless(@NonNull T[] elements, @NonNull CharSequence[] appendants, CharSequence delimiter, T unless) {
        return joinInternal(elements, delimiter, (element, index) -> {
            if (ObjectUtils.equals(element, unless)) return null;
            return String.valueOf(element) + appendants[index];
        });
    }

    @NonNull
    private static <T> String joinInternal(@NonNull T[] elements, CharSequence delimiter, @NonNull JoinHelper<T> helper) {
        StringBuilder joined = new StringBuilder();

        boolean hasAppended = false;
        int len = elements.length;
        for (int i = 0; i < len; i++) {
            T element = elements[i];

            CharSequence str = helper.produce(element, i);
            if (str == null || str.length() == 0) continue;

            if (hasAppended) joined.append(delimiter);
            else hasAppended = true;

            joined.append(str);
        }

        return joined.toString();
    }

    /**
     * Splits a string after each specified number to character
     *
     * @param count  number of character to skip before splitting the string
     * @param string the string to split
     * @return array of split strings
     */
    @NonNull
    public static String[] splitAfterEach(int count, @NonNull String string) {
        int length = string.length();
        int arraySize = (int) Math.ceil((double) length / count);

        String[] splitString = new String[arraySize];
        for (int i = 0, i2 = 0; i < arraySize; i++) {
            splitString[i] = string.substring(i2, ((i2 += count) >= length ? length : i2));
        }

        return splitString;
    }

    /**
     * Splits a string from end to start after each specified number of chars then
     * returns the splitted strings in original order (ergo, in the order they appear\
     * on the string)
     *
     * @param count number of character to skip before splitting the string
     * @param string   the string to split
     * @return array of split strings
     */
    @NonNull
    public static String[] halfReverseSplitAfterEach(int count, @NonNull String string) {
        int length = string.length();
        int arraySize = (int) Math.ceil((double) length / count);

        String[] splitString = new String[arraySize];
        for (int i = arraySize - 1, i2 = length; i >= 0; i--) {
            int end = i2;
            splitString[i] = string.substring((i2 -= count) < 0 ? 0 : i2, end);
        }

        return splitString;
    }

    /**
     * Splits a string from end to start after each specified number of chars
     *
     * @param count number of characters to skip before splitting the string
     * @param string   the string to split
     * @return array of splitted strings
     */
    @NonNull
    public static String[] reverseSplitAfterEach(int count, @NonNull String string) {
        int length = string.length();
        int arraySize = (int) Math.ceil((double) length / count);

        String[] splitString = new String[arraySize];
        for (int i = 0, i2 = length; i < arraySize; i++) {
            int end = i2;
            splitString[i] = string.substring((i2 -= count) < 0 ? 0 : i2, end);
        }

        return splitString;
    }

    public static boolean isValidEmail(@Nullable String email) {
        return email != null &&
                email.length() <= MAX_LEN_EMAIL &&
                email.toLowerCase().matches("^[\\S]+@[\\S]+\\.[^.,/]+$");
    }

    private interface JoinHelper<T> {
        @Nullable
        CharSequence produce(@Nullable T element, int index);
    }
}
