package thegoodkid.common.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class NumberUtilsTest {
    @Test
    public void numberExtraction_isCorrect() {
        assertThat(NumberUtils.extractNumbers("abc.def", -1)).isEqualTo(-1);
        assertThat(NumberUtils.extractNumbers("abc.d.efg", -1)).isEqualTo(-1);
        assertThat(NumberUtils.extractNumbers("542.33648", -1)).isEqualTo(542.33648);
        assertThat(NumberUtils.extractNumbers("54233648", -1)).isEqualTo(54233648);
        assertThat(NumberUtils.extractNumbers("-5a4b2c3d3e6f4g8h", -1)).isEqualTo(54233648);
        assertThat(NumberUtils.extractNumbers("54.233.648", -1)).isEqualTo(54233.648);
        assertThat(NumberUtils.extractNumbers("54.23a3.6bc4d8", -1)).isEqualTo(54233.648);
    }

    @Test
    public void numberExtraction_performance() {
        long amount = 420000;

        long startMillis = System.currentTimeMillis();
        for (long i = 0; i < amount; i++) {
            NumberUtils.extractNumbers("a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6.a1b2c3.d4e5f6", -1);
        }
        long duration = System.currentTimeMillis() - startMillis;

        System.out.println("Duration: " + duration + " millis");
    }
}
