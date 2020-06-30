package thegoodkid.common.utils;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class CalendarUtils {
    //calculated days return mode
    public static final int MODE_YEAR_MONTH_DAY = 0;
    public static final int MODE_YEAR_DAY = 1;
    public static final int MODE_MONTH_DAY = 2;
    public static final int MODE_DAY = 3;
    public static final int MODE_HOUR = 4;
    public static final int MODE_MINUTE = 5;
    public static final int MODE_SECOND = 6;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;

    private static final int ELEMENTS = 6;

    /**
     * Base calculation unit is second
     */
    private static final long SECOND_DIVISOR = 1000; //millis to seconds

    private static final long MINUTE_DIVISOR = 60; //seconds to minutes
    private static final long HOUR_DIVISOR = MINUTE_DIVISOR * 60;
    private static final long DAY_DIVISOR = HOUR_DIVISOR * 24;
    private static final long YEAR_DIVISOR = DAY_DIVISOR * 365; //365 is important and shouldn't be 366

    /**
     * Calculates the difference between two dates.
     * {@code returnMode} defines how and which fields should be calculated
     * <p>
     * Note: The first month of the year, January, is 0
     */
    @NonNull
    public static long[] calculateIntervals(int year1, int month1, int day1, int year2, int month2, int day2, int returnMode) {
        long[] duration = new long[ELEMENTS];

        //setting undefined fields (inc. millisecond) to zero is very important
        //and may cause bugs if they are left to be set automatically
        Calendar date1 = Calendar.getInstance();
        date1.set(year1, month1, day1, 0, 0, 0);
        date1.set(Calendar.MILLISECOND, 0);

        Calendar date2 = Calendar.getInstance();
        date2.set(year2, month2, day2, 0, 0, 0);
        date2.set(Calendar.MILLISECOND, 0);

        long millisTime1 = date1.getTimeInMillis();
        long millisTime2 = date2.getTimeInMillis();

        long intervalInSeconds;
        //throughout the rest of the method, we'll be assuming that date1 is before date2
        if (millisTime1 > millisTime2) {
            intervalInSeconds = (millisTime1 - millisTime2) / SECOND_DIVISOR;
            day1 += day2;
            month1 += month2;
            year1 += year2;
            day2 = day1 - day2;
            month2 = month1 - month2;
            year2 = year1 - year2;
            day1 -= day2;
            month1 -= month2;
            year1 -= year2;
        } else {
            intervalInSeconds = (millisTime2 - millisTime1) / SECOND_DIVISOR;
        }

        int intervalInDays = Long.valueOf(intervalInSeconds / DAY_DIVISOR).intValue();

        if (returnMode == MODE_YEAR_MONTH_DAY || returnMode == MODE_YEAR_DAY || returnMode == MODE_MONTH_DAY) {
            int intervalInYears = Double.valueOf(Math.floor((float) intervalInSeconds / YEAR_DIVISOR)).intValue();

            int remainedDaysInYear = intervalInDays % 365 - calculateLeapDays(year1, month1, day1, year2, month2, day2);
            int remainedDaysInMonth = calculateComplementaryDays(day1, day2, month1);
            int remainedMonthsInYear = calculateComplementaryMonths(month1, month2, day1 > day2);

            //Because we subtracted all the leap days (which inc. leap from this year; if present),
            //We now need to re-include leap days to 'after' year and/or month; if present and required
            if (isLeapYear(year2) && ((month2 > Calendar.FEBRUARY && month1 <= Calendar.FEBRUARY) || (month2 == Calendar.FEBRUARY && day2 == 29))) {
                if (year1 == year2 && month1 == Calendar.FEBRUARY) remainedDaysInMonth++;
                remainedDaysInYear++;
            }

            if (remainedDaysInYear < 0) {
                intervalInYears--;
                remainedDaysInYear += 365;
            }

            switch (returnMode) {
                case MODE_YEAR_MONTH_DAY:
                    duration[YEAR] = intervalInYears;
                    duration[MONTH] = remainedMonthsInYear;
                    duration[DAY] = remainedDaysInMonth;
                    return duration;
                case MODE_YEAR_DAY:
                    duration[YEAR] = intervalInYears;
                    duration[DAY] = remainedDaysInYear;
                    return duration;
                case MODE_MONTH_DAY:
                    duration[MONTH] = (intervalInYears * 12) + remainedMonthsInYear;
                    duration[DAY] = remainedDaysInMonth;
                    return duration;
            }

        } else if (returnMode == MODE_DAY) {
            duration[DAY] = intervalInDays;
            return duration;
        } else if (returnMode == MODE_HOUR) {
            duration[HOUR] = intervalInSeconds / HOUR_DIVISOR;
            return duration;
        } else if (returnMode == MODE_MINUTE) {
            duration[MINUTE] = intervalInSeconds / MINUTE_DIVISOR;
            return duration;
        } else if (returnMode == MODE_SECOND) {
            duration[SECOND] = intervalInSeconds;
            return duration;
        } else {
            throw new IllegalArgumentException("Mode: " + returnMode + " is not a valid mode");
        }

        return duration;
    }

    public static boolean isLeapYear(int year) {
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
    }

    private static int calculateComplementaryMonths(int monthOfYear1, int monthOfYear2, boolean isStartDayGreaterThanEndDay) {
        if (monthOfYear2 > monthOfYear1)
            return isStartDayGreaterThanEndDay ? (monthOfYear2 - (monthOfYear1 + 1)) : (monthOfYear2 - monthOfYear1);
        else if (monthOfYear2 < monthOfYear1) return 12 - (monthOfYear1 - monthOfYear2);
        else if (isStartDayGreaterThanEndDay) return 11; //implied monthOfYear1 == monthOfYear2
        else return 0;
    }

    //this method doesn't put additional effects on leap year
    private static int calculateComplementaryDays(int beforeDay, int afterDay, int beforeMonth) {
        Month month = Month.values()[beforeMonth];

        if (afterDay > beforeDay) {
            return afterDay - beforeDay;
        } else {
            int days = (month.getNumberOfDays() - beforeDay) + afterDay;
            return days == month.getNumberOfDays() ? 0 : days;
        }
    }

    public static int calculateLeapDays(int beforeYear, int beforeMonth, int beforeDay, int afterYear, int afterMonth, int afterDay) {
        int leapDays = 0;

        boolean isBeforeLeapYear = isLeapYear(beforeYear);
        boolean isAfterLeapYear = isLeapYear(afterYear);

        //if previous year is leap year or the date is before/is february 29th
        if (isBeforeLeapYear && (beforeMonth < Calendar.FEBRUARY || (beforeMonth == Calendar.FEBRUARY && beforeDay <= 29))) {
            leapDays++;
        }

        //if forward year is leap year or the date is/after february 29th
        if (afterYear != beforeYear && (isAfterLeapYear && (afterMonth > Calendar.FEBRUARY || (afterMonth == Calendar.FEBRUARY &&
                afterDay == 29)))) {
            leapDays++;
        }

        //having first and last year taken care of, we're only counting leap days for years
        //in the middle
        for (int i = beforeYear + 1; i < afterYear; i++) {
            if (isLeapYear(i)) leapDays++;
        }

        return leapDays;
    }

    public static boolean isBefore(int subjectDay, int subjectMonth, int destinationDay, int destinationMonth) {
        return subjectMonth < destinationMonth || (subjectMonth == destinationMonth && subjectDay < destinationDay);
    }

    public static int getFieldsCount() {
        return ELEMENTS;
    }

    public enum Month {
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        private int mNumberOfDays;

        Month(int numberOfDays) {
            mNumberOfDays = numberOfDays;
        }

        private int getNumberOfDays() {
            return mNumberOfDays;
        }
    }
}
