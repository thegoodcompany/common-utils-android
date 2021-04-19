package thegoodcompany.common.utils.miscellaneous;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import thegoodcompany.common.utils.miscellaneous.Contracts.Producer;

/**
 * Performs identical to null safety (<code>?.</code>) operator. Start the flow
 * by either invoking the {@link Flow#start(Object)} method or creating a new
 * instance, passing the nullable object. Then invoke {@link Flow#then(FlowCallback)}
 * passing in a {@link FlowCallback} function.
 * The {@link FlowCallback#then(FlowCallback)} method is only invoked if the
 * given object is not {@code null}.
 *
 * <pre>
 *     String emailAddress = new Flow(res.getUser())
 *          .then(User::getEmail) // uses lambda expression; available in java 1.8 and higher
 *          .then(Email::toString)
 *          .getValue(); // optionally you can also pass a default object parameter to #getValue
 * </pre>
 *
 * Note that, {@link Flow#flow(Object, FlowCallback)} method performs
 * identically, but is designed for smaller chaining.
 * for example,
 * <pre>
 *     String emailAddress = flow(res.getUser(), User::getEmailAddress);
 * </pre>
 * It is recommended to use {@link Flow#flow(Object, FlowCallback)} wherever it
 * fits.
 *
 * @param <T> the type of value to hold
 */
public class Flow<T> {
    @Nullable
    private final T mValue;

    public Flow(@Nullable T value) {
        mValue = value;
    }

    @NonNull
    public static <T> Flow<T> start(@Nullable T value) {
        return new Flow<>(value);
    }

    @Nullable
    public static <S, R> R flow(@Nullable S s, FlowCallback<S, R> callback) {
        return flow(s, callback, null);
    }

    @Contract("_, _, !null -> !null")
    public static <S, R> R flow(@Nullable S s, FlowCallback<S, R> callback, R defaultValue) {
        if (s != null) {
            R r = callback.run(s);
            if (r != null) return r;
        }

        return defaultValue;
    }

    public <R> Flow<R> then(@NonNull FlowCallback<T, R> callback) {
        T value = getValue();
        return new Flow<>(value != null ? callback.run(value) : null);
    }

    @Nullable
    public T getValue() {
        return mValue;
    }

    @Contract("!null -> !null")
    public T getValue(@Nullable T defaultValue) {
        return mValue != null ? mValue : defaultValue;
    }

    @Contract("!null -> !null")
    public T getValue(@Nullable DefaultProducer<T> defaultValue) {
        return mValue != null ? mValue : flow(defaultValue, DefaultProducer::produce, null);
    }

    public interface FlowCallback<T, R> {
        @Nullable
        R run(@NonNull T t);
    }

    public interface DefaultProducer<T> extends Producer<T> {
        @NonNull
        @Override
        T produce();
    }
}

