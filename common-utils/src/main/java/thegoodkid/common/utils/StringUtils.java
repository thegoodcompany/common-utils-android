package thegoodkid.common.utils;

import android.content.res.Resources;

import androidx.annotation.NonNull;

public class StringUtils {
    /**
     * Joins a list of integer values into a single string if and only if that value is not equal to {@code unless}
     * Each element is appended by their corresponding {@code appendants} followed by the {@code separator},
     *
     * @param values     list of integers which to be joined
     * @param appendants corresponding appendants
     * @param separator  the delimiter that separates each element (along with
     *                   their appendants)
     * @param unless     exception when joining will be skipped
     * @return the joined string
     */
    @NonNull
    public static String joinUnless(@NonNull int[] values, @NonNull String[] appendants, String separator, int unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        boolean useSeparator = false;
        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            if (useSeparator) joined.append(separator);
            else useSeparator = true;

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;
        }

        return joined.toString();
    }

    /**
     * Joins a list of integer values into a single string if and only if that value is not equal to {@code unless}
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
    public static String joinUnless(@NonNull Resources res, @NonNull int[] values, @NonNull int[] pluralAppendants, String separator, int unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        boolean useSeparator = false;
        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            if (useSeparator) joined.append(separator);
            else useSeparator = true;

            joined.append(values[i]).append(res.getQuantityString(pluralAppendants[i], values[i]));

            if (++i == total) break;
        }

        return joined.toString();
    }

    @NonNull
    public static String joinUnless(@NonNull long[] values, @NonNull String[] appendants, String separator, long unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        boolean useSeparator = false;
        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            if (useSeparator) joined.append(separator);
            else useSeparator = true;

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;
        }

        return joined.toString();
    }

    @NonNull
    public static String joinUnless(@NonNull Resources res, @NonNull long[] values, @NonNull int[] pluralAppendants, String separator, long unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        boolean useSeparator = false;
        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            if (useSeparator) joined.append(separator);
            else useSeparator = true;

            int quantity;
            if (values[i] > Integer.MAX_VALUE) quantity = Integer.MAX_VALUE;
            else if (values[i] < Integer.MIN_VALUE) quantity = Integer.MIN_VALUE;
            else quantity = (int) values[i];

            joined.append(values[i]).append(res.getQuantityString(pluralAppendants[i],
                    (quantity)));

            if (++i == total) break;
        }

        return joined.toString();
    }

    @NonNull
    public static <T> String joinUnless(@NonNull T[] values, @NonNull String[] appendants, String separator, T unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        boolean useSeparator = false;
        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            if (useSeparator) joined.append(separator);
            else useSeparator = true;

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;
        }

        return joined.toString();
    }
}
