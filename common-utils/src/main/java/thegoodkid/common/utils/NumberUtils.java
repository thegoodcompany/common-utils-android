package thegoodkid.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class NumberUtils {
    /**
     * Turns an {@link ArrayList} of integers into an array of integers
     * In places of null elements, the returned array has 0
     *
     * @param integers The {@link ArrayList} of integers that will be converted
     * @return An array of integers from the given {@link ArrayList}
     */
    @NonNull
    public static int[] toArray(@NonNull ArrayList<Integer> integers) {
        int[] ints = new int[integers.size()];

        int size = integers.size();
        for (int i = 0; i < size; i++) {
            Integer x = integers.get(i);
            if (x == null) continue;

            ints[i] = x;
        }

        return ints;
    }

    @NonNull
    public static int[] toArrayRemoveNull(@NonNull ArrayList<Integer> integers) {
        integers.removeAll(Collections.singleton(null));
        return toArray(integers);
    }

    /**
     * Removes any unwanted characters from the string and parses it
     * into a double wrapper object.
     * If the the string contains multiple points (.), only
     * the last one stays
     * <p>
     * This can be useful specially in places like query
     *
     * @param str      The string from which numbers are to be extracted
     * @param fallback The fallback object to be returned if the string
     *                 can't be parsed into a double wrapper object
     * @return A Double Object from the given string
     */
    @Nullable
    public static Double extractNumbers(@NonNull String str, @Nullable Double fallback) {
        StringBuilder builder = new StringBuilder(str);

        for (int latestDotIndex = builder.indexOf("."), lastDotIndex = builder.lastIndexOf(".");
             latestDotIndex != lastDotIndex;
             latestDotIndex = builder.indexOf("."), lastDotIndex--) {

            builder.deleteCharAt(latestDotIndex);
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

    /**
     * Converts any given number to word
     *
     * @param number   number to transform into words
     * @param readMode whether to read each digit individually or as whole
     * @return the converted string
     */
    @NonNull
    public static String toWord(@NonNull String number, @NonNull ReadMode readMode) {
        if (number.length() == 0) return "";

        String separator = " ";
        Number[] numbers = Number.values();

        StringBuilder numberBuilder = new StringBuilder(number);
        StringBuilder wordBuilder = new StringBuilder();

        if (numberBuilder.charAt(0) == '-') {
            wordBuilder.append("Minus ");
            numberBuilder.deleteCharAt(0);
        }

        int length = numberBuilder.length();

        if (readMode == ReadMode.DIGIT) {
            char[] charDigits = numberBuilder.toString().toCharArray();

            for (int i = 0; i < length; ) {
                if (charDigits[i] == '.') wordBuilder.append("Point");
                else {
                    int digit = Character.getNumericValue(charDigits[i]);
                    if (digit < 0) {
                        i++;
                        continue;
                    }

                    wordBuilder.append(numbers[digit].ones);
                }

                if (++i < length) wordBuilder.append(separator);
            }

            return wordBuilder.toString();
        }

        String[] parts = StringUtils.reverseSplitAfterEach(Number.suffixes.length * 3, numberBuilder.toString());
        int partsCount = parts.length;

        boolean encounteredNonzero = false;
        for (int partIndex = 0; partIndex < partsCount; partIndex++) {
            if (encounteredNonzero)
                wordBuilder.append(separator).append(Number.suffixes[Number.suffixes.length - 1]);

            String[] periods = StringUtils.reverseSplitAfterEach(3, parts[partIndex]);
            int periodsCount = periods.length;

            boolean partEncounteredNonzero = false;
            boolean periodEncounteredNonzero = false;
            for (int periodIndex = 0; periodIndex < periodsCount; periodIndex++) {
                if (periodEncounteredNonzero)
                    wordBuilder.append(separator).append(Number.suffixes[periodsCount - 1 - periodIndex]);

                periodEncounteredNonzero = false;
                String period = periods[periodIndex];
                int periodLength = period.length();
                for (int i = 0, place = periodLength - 1; i < periodLength; i++, place--) {
                    char c = period.charAt(i);
                    if (c == '0') continue;
                    if (i > 0 || partEncounteredNonzero || (encounteredNonzero && periodIndex == 0))
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

        return wordBuilder.toString();
    }

    private enum Number {
        ZERO(0, "Zero", "", ""),
        ONE(1, "One", "Ten", "Eleven"),
        TWO(2, "Two", "Twenty", "Twelve"),
        THREE(3, "Three", "Thirty", "Thirteen"),
        FOUR(4, "Four", "Forty", "Fourteen"),
        FIVE(5, "Five", "Fifty", "Fifteen"),
        SIX(6, "Six", "Sixty", "Sixteen"),
        SEVEN(7, "Seven", "Seventy", "Seventeen"),
        EIGHT(8, "Eight", "Eighty", "Eighteen"),
        NINE(9, "Nine", "Ninety", "Nineteen");

        private static final String[] suffixes = new String[]{
                "Thousand",
                "Million",
                "Billion",
                "Trillion",
                "Quadrillion",
                "Quintillion",
                "Sextillion",
                "Septillion",
                "Octillion",
                "Nonillion",
                "Decillion",
        };

        private final int intValue;
        private final String ones;
        private final String tens;
        private final String teens;

        Number(int intValue, String ones, String tens, String teens) {
            this.intValue = intValue;
            this.ones = ones;
            this.tens = tens;
            this.teens = teens;
        }

        private String getValueAt(int place) {
            if (place == 0) return ones;
            if (place == 1) return tens;
            if (place == 2) return ones + " Hundred";
            else return ones + " " + suffixes[place - 3];
        }
    }

    public enum ReadMode {
        DIGIT, NUMBER
    }
}
