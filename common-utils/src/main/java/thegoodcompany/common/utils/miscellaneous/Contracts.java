package thegoodcompany.common.utils.miscellaneous;

public final class Contracts {
    private Contracts() {

    }

    public interface Function<T, R> {
        R run(T t);
    }

    public interface ThrowableFunction<T, R, E extends Exception> {
        R run(T t) throws E;
    }

    public interface Producer<T> {
        T produce();
    }

    public interface Consumer<T> {
        void consume(T t);
    }
}
