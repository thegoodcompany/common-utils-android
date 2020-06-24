package thegoodkid.common.utils;

import androidx.annotation.NonNull;

public class StringUtils {
    @NonNull
    public static String joinUnless(@NonNull int[] values, @NonNull String[] appendants, String separator, int unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;

            joined.append(separator);
        }

        return joined.toString();
    }

    @NonNull
    public static String joinUnless(@NonNull long[] values, @NonNull String[] appendants, String separator, long unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;

            joined.append(separator);
        }

        return joined.toString();
    }

    @NonNull
    public static <T> String joinUnless(@NonNull T[] values, @NonNull String[] appendants, String separator, T unless) {
        StringBuilder joined = new StringBuilder();
        int total = values.length;

        for (int i = 0; i < total; ) {
            if (values[i] == unless) {
                i++;
                continue;
            }

            joined.append(values[i]).append(appendants[i]);

            if (++i == total) break;

            joined.append(separator);
        }

        return joined.toString();
    }
}
