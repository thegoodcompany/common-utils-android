/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public class NumberUtils {
    private static final String DEF_SEPARATOR = " ";

    private NumberUtils() { }

    /**
     * Turns n {@link List} of integers into an array of integers
     * In places of null elements, the returned array has 0
     *
     * @param integers The {@link List} of integers that will be converted
     * @return An array of integers from the given {@link List}
     */
    @NonNull
    public static int[] toArray(@NonNull List<Integer> integers) {
        int[] ints = new int[integers.size()];

        int size = integers.size();
        for (int i = 0; i < size; i++) {
            Integer x = integers.get(i);
            if (x == null) continue;

            ints[i] = x;
        }

        return ints;
    }

    /**
     * Returns an array of int of non-null integers from {@code integers}
     */
    @NonNull
    public static int[] toArrayRemoveNull(@NonNull List<Integer> integers) {
        integers.removeAll(Collections.singleton(null));
        return toArray(integers);
    }

    /**
     * Removes any unwanted characters from the string and parses it
     * into a double wrapper object.
     * If the the string contains multiple points (.), only
     * the first one stays
     *
     * This can be useful specially in places like query
     *
     * @param str      The string from which numbers are to be extracted
     * @param fallback The fallback object to be returned if the string
     *                 can't be parsed into a double wrapper object
     * @return A Double Object from the given string
     */
    @Nullable
    @Contract("_, !null -> !null")
    public static Double extractNumbers(@NonNull String str, @Nullable Double fallback) {
        StringBuilder builder = new StringBuilder(str);

        boolean hasProcessedDot = false;
        for (int i = 0, len = builder.length(); i < len; ) {
            char c = builder.charAt(i);
            if (Character.isDigit(c)) {
                i++;
                continue;
            }

            if (!hasProcessedDot && c == '.') {
                hasProcessedDot = true;
                i++;
                continue;
            }

            builder.deleteCharAt(i);
            len--;
        }

        int length = builder.length();
        for (int i = 0; i < length; i++) {
            char c = builder.charAt(i);
            if (Character.isDigit(c) || c == '.') continue;

            builder.deleteCharAt(i);
            i--;
            length--;
        }

        if (builder.length() == 1 && builder.charAt(0) == '.') return fallback;
        if (builder.length() > 0) return Double.parseDouble(builder.toString());
        else return fallback;
    }

    @NonNull
    private static StringBuilder toWordInternal(@NonNull String number, @NonNull ReadMode readMode, @NonNull String separator) {
        if (number.length() == 0) return new StringBuilder();

        Number[] numbers = Number.values();
        StringBuilder numberBuilder = new StringBuilder(number);
        StringBuilder wordBuilder = new StringBuilder();

        if (numberBuilder.charAt(0) == '-') {
            wordBuilder.append("minus ");
            numberBuilder.deleteCharAt(0);
        }

        int length = numberBuilder.length();

        switch (readMode) {
            case DIGIT:
                char[] charDigits = numberBuilder.toString().toCharArray();

                for (int i = 0; ; ) {
                    if (charDigits[i] == '.') wordBuilder.append("point");
                    else {
                        int digit = Character.getNumericValue(charDigits[i]);
                        wordBuilder.append(numbers[digit].ones);
                    }

                    if (++i < length) wordBuilder.append(separator);
                    else break;
                }
                break;
            case NUMBER:
                String strNumber = numberBuilder.toString();
                int pointIndex = strNumber.indexOf('.');
                String[] parts = StringUtils.halfReverseSplitAfterEach(Number.suffixes.length * 3, pointIndex != -1
                        ? strNumber.substring(0, pointIndex) : strNumber);

                boolean encounteredNonzero = false;
                for (String part : parts) {
                    if (encounteredNonzero)
                        wordBuilder.append(separator).append(Number.suffixes[Number.suffixes.length - 1]);

                    String[] periods = StringUtils.halfReverseSplitAfterEach(3, part);
                    int periodCount = periods.length;

                    boolean partEncounteredNonzero = false;
                    boolean periodEncounteredNonzero = false;
                    for (int periodIndex = 0; periodIndex < periodCount; periodIndex++) {
                        if (periodEncounteredNonzero)
                            wordBuilder.append(separator).append(Number.suffixes[periodCount - 1 - periodIndex]);

                        periodEncounteredNonzero = false;
                        String period = periods[periodIndex];
                        int periodLength = period.length();
                        for (int i = 0, place = periodLength - 1; i < periodLength; i++, place--) {
                            char c = period.charAt(i);
                            if (c == '0') continue;
                            if (encounteredNonzero && (i > 0 || partEncounteredNonzero))
                                wordBuilder.append(separator);

                            int nextIndexedNumber;
                            if (c == '1' && place == 1 && (nextIndexedNumber = Character.getNumericValue(numberBuilder.charAt(++i))) != 0)
                                wordBuilder.append(numbers[nextIndexedNumber].teens);
                            else
                                wordBuilder.append(numbers[Character.getNumericValue(c)].getValueAt(place));

                            if (!periodEncounteredNonzero) {
                                periodEncounteredNonzero = true;
                                partEncounteredNonzero = true;
                                encounteredNonzero = true;
                            }
                        }
                    }
                }

                break;
            default:
                break;
        }

        return wordBuilder;
    }

    /**
     * Converts any given number to word
     *
     * @param number   number to transform into words
     * @param readMode whether to read each digit individually or as whole
     * @return the converted string
     */
    @NonNull
    public static String toWord(@NonNull String number, @NonNull ReadMode readMode) {
        return toWord(number, readMode, DEF_SEPARATOR);
    }

    @NonNull
    public static String toWord(@NonNull String number, @NonNull ReadMode readMode, @Nullable String separator) {
        if (separator == null) separator = DEF_SEPARATOR;
        StringBuilder result;

        if (readMode == ReadMode.DIGIT) {
            result = toWordInternal(number, ReadMode.DIGIT, separator);
        } else {
            int periodIndex = number.indexOf('.');
            if (periodIndex != -1) {
                result = toWordInternal(number.substring(0, periodIndex), ReadMode.NUMBER, separator);
                if (result.length() > 0) result.append(separator);
                result.append(toWordInternal(number.substring(periodIndex), ReadMode.DIGIT, separator));
            } else {
                result = toWordInternal(number, ReadMode.NUMBER, separator);
            }
        }

        if (result.length() == 0) result.append(Number.ZERO.ones);

        result.setCharAt(0, Character.toUpperCase(result.charAt(0)));
        return result.toString();
    }

    @NonNull
    public static int[] toPrimitive(@NonNull Integer[] src) {
        int len = src.length;
        int[] primitive = new int[len];

        for (int i = 0; i < len; i++) {
            primitive[i] = src[i];
        }

        return primitive;
    }

    @NonNull
    public static Integer[] toBoxed(@NonNull int[] src) {
        int len = src.length;
        Integer[] boxed = new Integer[len];
        
        for (int i = 0; i < len; i++) {
            boxed[i] = src[i];
        }

        return boxed;
    }

    @NonNull
    public static long[] toPrimitive(@NonNull Long[] src) {
        int len = src.length;
        long[] primitive = new long[len];

        for (int i = 0; i < len; i++) {
            primitive[i] = src[i];
        }

        return primitive;
    }

    @NonNull
    public static Long[] toBoxed(@NonNull long[] src) {
        int len = src.length;
        Long[] boxed = new Long[len];

        for (int i = 0; i < len; i++) {
            boxed[i] = src[i];
        }

        return boxed;
    }

    @NonNull
    public static double[] toPrimitive(@NonNull Double[] src) {
        int len = src.length;
        double[] primitive = new double[len];

        for (int i = 0; i < len; i++) {
            primitive[i] = src[i];
        }

        return primitive;
    }

    @NonNull
    public static Double[] toBoxed(@NonNull double[] src) {
        int len = src.length;
        Double[] boxed = new Double[len];

        for (int i = 0; i < len; i++) {
            boxed[i] = src[i];
        }

        return boxed;
    }
    
    private enum Number {
        ZERO("zero", "", ""),
        ONE("one", "ten", "eleven"),
        TWO("two", "twenty", "twelve"),
        THREE("three", "thirty", "thirteen"),
        FOUR("four", "forty", "fourteen"),
        FIVE("five", "fifty", "fifteen"),
        SIX("six", "sixty", "sixteen"),
        SEVEN("seven", "seventy", "seventeen"),
        EIGHT("eight", "eighty", "eighteen"),
        NINE("nine", "ninety", "nineteen");

        private static final String[] suffixes = new String[]{
                "thousand",
                "million",
                "billion",
                "trillion",
                "quadrillion",
                "quintillion",
                "sextillion",
                "septillion",
                "octillion",
                "nonillion",
                "decillion",
        };

        private final String ones;
        private final String tens;
        private final String teens;

        Number(String ones, String tens, String teens) {
            this.ones = ones;
            this.tens = tens;
            this.teens = teens;
        }

        private String getValueAt(int place) {
            if (place == 0) return ones;
            if (place == 1) return tens;
            if (place == 2) return ones + " hundred";
            else return ones + " " + suffixes[place - 3];
        }
    }

    public enum ReadMode {
        DIGIT, NUMBER
    }
}
