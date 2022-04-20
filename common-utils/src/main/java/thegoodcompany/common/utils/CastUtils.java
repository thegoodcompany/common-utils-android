package thegoodcompany.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CastUtils {
    private CastUtils() {

    }

    /**
     * Makes an unchecked cast to {@link Map}. For checked cast, use {@link #safeCastMap(Object, Class, Class)} 
     * instead.
     * 
     * @throws ClassCastException if provided {@code mapObj} is not an instance of {@link Map}
     */
    @NonNull
    @Contract("null -> fail")
    public static <K, V> Map<K, V> quickCastMap(@Nullable Object mapObj) {
        if (mapObj instanceof Map) //noinspection unchecked
            return (Map<K, V>) mapObj;

        throw makeCastException(mapObj, Map.class);
    }

    /**
     * Similar to {@link #quickCastMap(Object)} but instant of throwing an exception, it returns null
     * @see #quickCastMap(Object)
     */
    @Contract("null -> null")
    public static <K, V> Map<K, V> quickSafeCastMap(@Nullable Object mapObj) {
        if (mapObj instanceof Map) //noinspection unchecked
            return (Map<K, V>) mapObj;

        return null;
    }

    /**
     * Makes an unchecked cast to {@link List}. For checked cast, use {@link #safeCastList(Object, Class)}
     * instead.
     * 
     * @throws ClassCastException if {@code obj} is not an instance of {@link List}
     */
    @NonNull
    public static <T> List<T> quickCastList(@Nullable Object obj) {
        if (obj instanceof List) //noinspection unchecked
            return (List<T>) obj;

        throw makeCastException(obj, List.class);
    }

    /**
     * Similar to {@link #quickCastList(Object)} except it returns null for class incompatibility.
     * @see #quickCastList(Object) 
     */
    @Nullable
    @Contract("null -> null")
    public static <T> List<T> quickSafeCastList(@Nullable Object obj) {
        if (obj instanceof List) //noinspection unchecked
            return (List<T>) obj;

        return null;
    }

    /**
     * Similar to {@link #safeCastMap(Object, Class, Class)} except {@code keyClass} is {@link String}
     * @see #safeCastMap(Object, Class, Class) 
     */
    @Nullable
    public static <V> Map<String, V> safeCastMap(@Nullable Object mapObj, @NonNull Class<V> valueClass) {
        return safeCastMap(mapObj, String.class, valueClass);
    }

    /**
     * Safely casts an object to {@link Map} with keys of type {@code keyClass} and values of type {@code valueClass}.
     * Entries with incompatible types are ignored.
     *
     * @return a new {@link Map} object with appropriate types or null if {@code mapObj} is not a {@link Map}
     */
    @Nullable
    public static <K, V> Map<K, V> safeCastMap(@Nullable Object mapObj, @NonNull Class<K> keyClass, @NonNull Class<V> valueClass) {
        if (!(mapObj instanceof Map)) return null;

        Map<?, ?> obj = (Map<?, ?>) mapObj;
        Object[] keys = obj.keySet().toArray();
        Object[] values = obj.values().toArray();

        int size = obj.size();
        Map<K, V> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Object key = keys[i];
            Object value = values[i];

            if (keyClass.isInstance(key) && valueClass.isInstance(value))
                result.put(keyClass.cast(key), valueClass.cast(value));
        }

        return result;
    }

    /**
     * Safely casts {@code obj} to {@link List} of {@code tClass} objects. Ignores incompatible values.
     * @return a new {@link List} object with appropriate types or null if {@code obj} is not a {@link List}
     */
    @Nullable
    public static <T> List<T> safeCastList(@Nullable Object obj, Class<T> tClass) {
        if (!(obj instanceof List)) return null;

        List<?> list = (List<?>) obj;
        List<T> value = new ArrayList<>(list.size());
        for (Object item : list) if (tClass.isInstance(item)) value.add(tClass.cast(item));

        return value;
    }

    /**
     * Safely casts {@code obj} to {@link List} of {@code eClass}. Enums are inferred using {@link Class#isInstance(Object)}
     * or by comparing {@link Enum#toString()} against items of {@code obj}. Incompatible types are ignored.
     */
    @Nullable
    public static <E extends Enum<E>> List<E> safeCastEnumList(@Nullable Object obj, Class<E> eClass) {
        if (!(obj instanceof List)) return null;

        List<?> list = (List<?>) obj;
        E[] constants = eClass.getEnumConstants();
        if (constants == null || constants.length == 0) return null;

        List<E> value = new ArrayList<>(list.size());
        for (Object o : list) {
            if (eClass.isInstance(o)) {
                value.add(eClass.cast(o));
            } else for (E constant : constants) {
                if (constant.toString().equals(o.toString())) {
                    value.add(constant);
                    break;
                }
            }
        }

        return value;
    }

    @Nullable
    public static <T, V> List<Map<T, V>> safeCastMapList(@Nullable Object obj, Class<T> tClass, Class<V> vClass) {
        if (!(obj instanceof List)) return null;

        List<?> listRaw = (List<?>) obj;
        List<Map<T, V>> res = new ArrayList<>();
        for (Object item : listRaw) {
            Map<T, V> map = safeCastMap(item, tClass, vClass);
            if (map != null) res.add(map);
        }

        return res;
    }

    @Nullable
    public static <E extends Enum<E>> E uncheckedSafeCastEnum(@Nullable String strName, @NonNull Class<?> eClass) {
        //noinspection unchecked
        return safeCastEnum(strName, (Class<E>) eClass);
    }

    @Nullable
    public static <E extends Enum<E>> E safeCastEnum(@Nullable String str, Class<E> eClass) {
        if (str == null) return null;

        E[] constants = eClass.getEnumConstants();
        return safeCastEnum(str, constants);
    }

    public static <E extends Enum<E>> E safeCastEnum(@NonNull String str, @Nullable E[] eConsts) {
        int len;
        if (eConsts == null || (len = eConsts.length) == 0) return null;

        for (int i = 0; i < len; i++) {
            E e = eConsts[i];
            if (e.toString().equals(str)) return e;
        }

        return null;
    }

    /**
     * Safely casts number to any subclasses of {@link Number}. E.g. from {@link Long} to {@link Integer}
     */
    @Nullable
    public static <T extends Number> T safeCastNumber(@NonNull Object obj, @NonNull Class<T> tClass) {
        if (!(obj instanceof Number)) return null;

        Number num = (Number) obj;
        if (tClass.isInstance(num)) return tClass.cast(num);
        if (tClass == Integer.class) return tClass.cast(num.intValue());
        if (tClass == Long.class) return tClass.cast(num.longValue());
        if (tClass == Double.class) return tClass.cast(num.doubleValue());
        if (tClass == Byte.class) return tClass.cast(num.byteValue());
        if (tClass == Short.class) return tClass.cast(num.shortValue());
        if (tClass == Float.class) return tClass.cast(num.floatValue());

        return null;
    }

    @NonNull
    private static ClassCastException makeCastException(@Nullable Object actual, @NonNull Class<?> expected) {
        return new ClassCastException("Provided object can not be casted into " + expected.getSimpleName()
                + (actual == null ? (" [object was null]") :
                (" [object class: " + actual.getClass().getCanonicalName() + "]")));
    }
}
