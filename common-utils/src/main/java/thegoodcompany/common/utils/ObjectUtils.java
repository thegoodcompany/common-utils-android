package thegoodcompany.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ObjectUtils {
    private ObjectUtils() {

    }

    /**
     * Checks if all given arguments are equal or not
     *
     * @param o an object
     * @param os other objects that will be compared against first argument
     * @return whether all given objects are equal
     */
    public static boolean equals(@Nullable Object o, @NonNull Object... os) {
        for (Object o1 : os) {
            if (!equals(o, o1)) return false;
        }

        return true;
    }

    /**
     * Null-safe object equality checker. Identical to {@link java.util.Objects#equals}
     * except, this can be called on modules below api 19 up to 16
     *
     * @param o an object
     * @param o2 another object that will be compared with the first argument
     * @return whether the first argument is equal to second argument
     */
    public static boolean equals(@Nullable Object o, @Nullable Object o2) {
        // java.util.Objects#equals requires api 19 or higher; current minimum is 16
        // noinspection EqualsReplaceableByObjectsCall
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static boolean nonNull(@Nullable Object o) {
        return o != null;
    }
}
