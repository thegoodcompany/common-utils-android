package thegoodcompany.common.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GeneralUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void general() {
        List<Number> x = new ArrayList<>();
        x.add(1);
        x.add(2);
        x.add(3);
        x.add(4);
        x.add(5);

        assertThat(x.size()).isEqualTo(5);
        x.subList(1, 4).clear();
        assertThat(x.size()).isEqualTo(2);
    }

    public static void printTestInfo(String test, long iterationAmount) {
        System.out.println(test + " test completed\n...............................");
        System.out.println("Iterations: " + iterationAmount + " ("
                + NumberUtils.toWord(String.valueOf(iterationAmount), NumberUtils.ReadMode.NUMBER) + ")");
    }

    public static void printDuration(String item, long duration, long iterationAmount) {
        System.out.println(item + " duration: " + duration + " nanoseconds; per unit: "
                + duration / iterationAmount + " nanoseconds");
    }

    public static void printDiff(String between, long diff) {
        System.out.println("Difference (" + between + "): " + diff + " nanoseconds; or "
                + diff / 1000000 + " milliseconds");
    }
}