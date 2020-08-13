package thegoodkid.common.utils;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

public class NumberUtilsTest {
    @Test
    public void toArray_isCorrect() {
        ArrayList<Integer> test1 = new ArrayList<>();
        ArrayList<Integer> test2 = new ArrayList<>();
        ArrayList<Integer> test3 = new ArrayList<>();

        test2.add(5); test2.add(4); test2.add(3);
        test3.add(null); test3.add(153);

        assertThat(NumberUtils.toArray(test1)).isEqualTo(new int[]{});
        assertThat(NumberUtils.toArray(test2)).isEqualTo(new int[]{5, 4, 3});
        assertThat(NumberUtils.toArray(test3)).isEqualTo(new int[]{0, 153});
        assertThat(NumberUtils.toArrayRemoveNull(test3)).isEqualTo(new int[]{153});
    }

    @Test
    public void toArray_performance() {
        long amount = 42000;

        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            integers.add((int) (Math.random() * 1000));
        }

        long start = System.currentTimeMillis();
        for (long i = 0; i < amount; i++) {
            NumberUtils.toArray(integers);
        }
        long duration = System.currentTimeMillis() - start;

        System.out.println("Duration: " + duration + " millis");
    }

    @Test
    public void numberExtraction_isCorrect() {
        assertThat(NumberUtils.extractNumbers("abc.def", -1.0)).isEqualTo(-1.0);
        assertThat(NumberUtils.extractNumbers("abc.d.efg", -1.0)).isEqualTo(-1.0);
        assertThat(NumberUtils.extractNumbers("542.33648", -1.0)).isEqualTo(542.33648);
        assertThat(NumberUtils.extractNumbers("54233648", -1.0)).isEqualTo(54233648);
        assertThat(NumberUtils.extractNumbers("-5a4b2c3d3e6f4g8h", -1.0)).isEqualTo(54233648);
        assertThat(NumberUtils.extractNumbers("54.233.648", -1.0)).isEqualTo(54233.648);
        assertThat(NumberUtils.extractNumbers("54.23a3.6bc4d8", -1.0)).isEqualTo(54233.648);
    }

    @Test
    public void numberExtraction_performance() {
        long amount = 420000;

        long startMillis = System.currentTimeMillis();
        for (long i = 0; i < amount; i++) {
            NumberUtils.extractNumbers("a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6", -1.0);
        }
        long duration = System.currentTimeMillis() - startMillis;

        System.out.println("Duration: " + duration + " millis");
    }

    @Test
    public void toWord_isCorrect() {
        assertThat(NumberUtils.toWord("5634", NumberUtils.ReadMode.DIGIT)).isEqualTo("Five six three four");
        assertThat(NumberUtils.toWord("5634.7", NumberUtils.ReadMode.DIGIT)).isEqualTo("Five six three four point seven");
        assertThat(NumberUtils.toWord("-5634.7", NumberUtils.ReadMode.DIGIT)).isEqualTo("Minus five six three four point seven");

        assertThat(NumberUtils.toWord("452", NumberUtils.ReadMode.NUMBER)).isEqualTo("Four hundred fifty two");
        assertThat(NumberUtils.toWord("1011", NumberUtils.ReadMode.NUMBER)).isEqualTo("One thousand eleven");
        assertThat(NumberUtils.toWord("213", NumberUtils.ReadMode.NUMBER)).isEqualTo("Two hundred thirteen");
        assertThat(NumberUtils.toWord("213.56", NumberUtils.ReadMode.NUMBER)).isEqualTo("Two hundred thirteen point five six");
        assertThat(NumberUtils.toWord(".563", NumberUtils.ReadMode.NUMBER)).isEqualTo("Point five six three");
        assertThat(NumberUtils.toWord("200", NumberUtils.ReadMode.NUMBER)).isEqualTo("Two hundred");
        assertThat(NumberUtils.toWord("-200", NumberUtils.ReadMode.NUMBER)).isEqualTo("Minus two hundred");

        assertThat(NumberUtils.toWord("85682", NumberUtils.ReadMode.NUMBER)).isEqualTo("Eighty five thousand six hundred eighty two");
        assertThat(NumberUtils.toWord("5759472", NumberUtils.ReadMode.NUMBER))
                .isEqualTo("Five million seven hundred fifty nine thousand four hundred seventy two");
        assertThat(NumberUtils.toWord("5000000000000", NumberUtils.ReadMode.NUMBER))
                .isEqualTo("Five trillion");
    }

    @Test
    public void toWord_performance() {
        long amount = 420000;

        long startMillis = System.currentTimeMillis();
        for (long i = 0; i < amount; i++) {
            NumberUtils.toWord("54632.884", NumberUtils.ReadMode.NUMBER);
        }
        long duration = System.currentTimeMillis() - startMillis;

        System.out.println("Duration: " + duration + " millis");
    }
}
