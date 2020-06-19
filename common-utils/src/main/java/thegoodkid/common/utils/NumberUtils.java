package thegoodkid.common.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NumberUtils {
    /**
     * Turns an {@link ArrayList} of integers into an array of integers
     * @param integers The {@link ArrayList} of integers that will be converted
     * @return An array of integers from the given {@link ArrayList}
     */
    @NonNull
    public static int[] toArray(@NonNull ArrayList<Integer> integers) {
        int[] ints = new int[integers.size()];

        int count = 0;
        for (int x : integers) {
            ints[count] = x;
            count++;
        }

        return ints;
    }

    /**
     * Removes any unwanted characters from the string and parses it
     * into a double absolute value.
     * If the the string contains multiple points (.), only
     * the last one stays
     *
     * @param str      The string from which number are to be extracted
     * @param fallback The fallback number to be returned if the string
     *                 can't be parsed into a double value
     * @return A double value from the given string
     */
    public static double extractNumbers(@NonNull String str, double fallback) {
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
}
