package thegoodcompany.common.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static thegoodcompany.common.utils.CalendarUtils.DAY;
import static thegoodcompany.common.utils.CalendarUtils.MODE_YEAR_DAY;
import static thegoodcompany.common.utils.CalendarUtils.MODE_YEAR_MONTH_DAY;
import static thegoodcompany.common.utils.CalendarUtils.MONTH;
import static thegoodcompany.common.utils.CalendarUtils.YEAR;

public class CalendarUtilsTest {
    @Test
    public void dateIntervals_isCorrect1() {
        long[] test1 = CalendarUtils.calculateIntervals(2020, 0, 1, 2020, 0, 4, MODE_YEAR_DAY);
        assertThat(test1[DAY]).isEqualTo(3);
    }

    @Test
    public void dateIntervals_isCorrect() {
        long[] test1 = CalendarUtils.calculateIntervals(2020, 5, 22, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test2 = CalendarUtils.calculateIntervals(2020, 5, 25, 2020, 5, 22, MODE_YEAR_MONTH_DAY);
        long[] test3 = CalendarUtils.calculateIntervals(2020, 2, 25, 2020, 5, 22, MODE_YEAR_MONTH_DAY);
        long[] test4 = CalendarUtils.calculateIntervals(2020, 3, 30, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test5 = CalendarUtils.calculateIntervals(2020, 0, 28, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test6 = CalendarUtils.calculateIntervals(2020, 1, 27, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test7 = CalendarUtils.calculateIntervals(2020, 1, 29, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test8 = CalendarUtils.calculateIntervals(2019, 0, 1, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test9 = CalendarUtils.calculateIntervals(2019, 1, 13, 2020, 5, 25, MODE_YEAR_MONTH_DAY);
        long[] test10 = CalendarUtils.calculateIntervals(2007, 2, 28, 2020, 5, 25, MODE_YEAR_MONTH_DAY);

        //from jun 22th to jun 25th
        assertThat(test1[DAY]).isEqualTo(3);
        assertThat(test1[MONTH]).isEqualTo(0);
        assertThat(test1[YEAR]).isEqualTo(0);

        //from jun 25th to jun 22th
        assertThat(test2[DAY]).isEqualTo(3);
        assertThat(test2[MONTH]).isEqualTo(0);
        assertThat(test2[YEAR]).isEqualTo(0);

        //from mar 25th to jun 22th
        assertThat(test3[DAY]).isEqualTo(28);
        assertThat(test3[MONTH]).isEqualTo(2);
        assertThat(test3[YEAR]).isEqualTo(0);

        //from apr 30th to jun 25th
        assertThat(test4[DAY]).isEqualTo(25);
        assertThat(test4[MONTH]).isEqualTo(1);
        assertThat(test4[YEAR]).isEqualTo(0);

        //from jan 28th to jun 25th
        assertThat(test5[DAY]).isEqualTo(28);
        assertThat(test5[MONTH]).isEqualTo(4);
        assertThat(test5[YEAR]).isEqualTo(0);

        //from feb 27th to jun 25th
        assertThat(test6[DAY]).isEqualTo(27);
        assertThat(test6[MONTH]).isEqualTo(3);
        assertThat(test6[YEAR]).isEqualTo(0);

        //from feb 29th to jun 25th
        assertThat(test7[DAY]).isEqualTo(25);
        assertThat(test7[MONTH]).isEqualTo(3);
        assertThat(test7[YEAR]).isEqualTo(0);

        //from jan 01th, 2019 to jun 25th, 2020
        assertThat(test8[DAY]).isEqualTo(24);
        assertThat(test8[MONTH]).isEqualTo(5);
        assertThat(test8[YEAR]).isEqualTo(1);

        //from feb 13th, 2019 to jun 25th, 2020
        assertThat(test9[DAY]).isEqualTo(12);
        assertThat(test9[MONTH]).isEqualTo(4);
        assertThat(test9[YEAR]).isEqualTo(1);

        //from mar 28th, 2007 ti jun 25th, 2020
        assertThat(test10[DAY]).isEqualTo(28);
        assertThat(test10[MONTH]).isEqualTo(2);
        assertThat(test10[YEAR]).isEqualTo(13);
    }

    @Test
    public void leapDaysCount_isCorrect() {
        assertThat(CalendarUtils.calculateLeapDays(2020, 1, 1, 2020, 5, 25))
                .isEqualTo(1);

        assertThat(CalendarUtils.calculateLeapDays(2020, 1, 29, 2020, 5, 25))
                .isEqualTo(1);

        assertThat(CalendarUtils.calculateLeapDays(2017, 5, 24, 2020, 5, 25))
                .isEqualTo(1);

        assertThat(CalendarUtils.calculateLeapDays(2016, 5, 24, 2020, 5, 25))
                .isEqualTo(1);

        assertThat(CalendarUtils.calculateLeapDays(2016, 1, 29, 2020, 5, 25))
                .isEqualTo(2);

        assertThat(CalendarUtils.calculateLeapDays(2007, 1, 29, 2020, 5, 25))
                .isEqualTo(4);
    }

    @Test
    public void dateIsBefore_isCorrect() {
        assertThat(CalendarUtils.isBefore(7, 2, 8, 2)).isTrue();
        assertThat(CalendarUtils.isBefore(8, 2, 8, 2)).isFalse();
        assertThat(CalendarUtils.isBefore(9, 2, 8, 2)).isFalse();
    }
}
