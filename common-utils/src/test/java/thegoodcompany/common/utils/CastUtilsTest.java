package thegoodcompany.common.utils;

import static com.google.common.truth.Truth.assertThat;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CastUtilsTest {
    @Test
    public void numberCast_isCorrect() {
        int whole = 5;
        double dotted = 5.5;
        long bigWhole = 55;
        Object objWhole = 5;

        assertThat(CastUtils.safeCastNumber(whole, Double.class)).isEqualTo(5.0d);
        assertThat(CastUtils.safeCastNumber(whole, Long.class)).isEqualTo(5L);
        assertThat(CastUtils.safeCastNumber(dotted, Integer.class)).isEqualTo(5);
        assertThat(CastUtils.safeCastNumber(dotted, Float.class)).isEqualTo(5.5f);
        assertThat(CastUtils.safeCastNumber(bigWhole, Double.class)).isEqualTo(55.0d);
        assertThat(CastUtils.safeCastNumber(objWhole, Double.class)).isEqualTo(5.0d);

        Class<?> oClass = Double.class;
        assertThat((Double) CastUtils.safeCastNumber(whole, oClass.asSubclass(Number.class))).isEqualTo(5.0d);
    }

    @Test
    public void enumListCast_isCorrect() {
        List<Direction> dirs = new ArrayList<>();
        dirs.add(Direction.NORTH);
        dirs.add(Direction.EAST);

        List<String> strDirs = ArrayUtils.transform(dirs, (t, index, source) -> t.toString());
        List<Direction> castedDirs = CastUtils.safeCastEnumList((Object) dirs, Direction.class);
        List<Direction> castedStrDirs = CastUtils.safeCastEnumList((Object) strDirs, Direction.class);

        assertThat(castedDirs).containsExactly(Direction.NORTH, Direction.EAST);
        assertThat(castedStrDirs).containsExactly(Direction.NORTH, Direction.EAST);
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST;

        @NonNull
        @Override
        public String toString() {
            return name();
        }
    }
}
