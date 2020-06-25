package thegoodkid.common.utilsdemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.microsoft.fluentui.datetimepicker.DateTimePicker;
import com.microsoft.fluentui.datetimepicker.DateTimePickerDialog;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import thegoodkid.common.utils.CalendarUtils;
import thegoodkid.common.utils.StringUtils;
import thegoodkid.common.utilsdemo.databinding.ActivityCalendarUtilsBinding;
import thegoodkid.common.utilsdemo.utilis.ViewUtils;

public class CalendarUtilsActivity extends AppCompatActivity implements DateTimePickerDialog.OnDateTimePickedListener {
    private static final String START_DATE = "start_date";
    private static final String DURATION = "duration";

    private ActivityCalendarUtilsBinding binding;
    private ZonedDateTime startDate;
    private Duration duration = Duration.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarUtilsBinding.inflate(getLayoutInflater());

        if (savedInstanceState != null) {
            startDate = (ZonedDateTime) savedInstanceState.getSerializable(START_DATE);
            duration = (Duration) savedInstanceState.getSerializable(DURATION);

            if (startDate != null && duration != null) onDateTimePicked(startDate, duration);
        }

        setContentView(binding.getRoot());

        setupAppbar();
        init();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(START_DATE, startDate);
        outState.putSerializable(DURATION, duration);
    }

    private void setupAppbar() {
        setSupportActionBar(binding.appBar.getToolbar());

        binding.appBar.getToolbar().setTitle(DemoListActivity.Demos.CALENDAR_UTILS.title);
        binding.appBar.getToolbar().setNavigationIcon(ViewUtils.createNavigationBackDrawable(this));
        binding.appBar.getToolbar().setNavigationOnClickListener(view -> onBackPressed());
    }

    private void init() {
        AndroidThreeTen.init(this);

        binding.intervalRangeStart.setOnClickListener(view -> showDatePicker(Type.RANGE_START));
        binding.intervalRangeEnd.setOnClickListener(view -> showDatePicker(Type.RANGE_END));
    }

    private void showDatePicker(Type type) {
        if (startDate == null) startDate = ZonedDateTime.now();

        DateTimePicker picker = DateTimePicker.Companion.newInstance(this, DateTimePickerDialog.Mode.DATE,
                type.mode, startDate, duration);

        picker.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onDateTimePicked(@NotNull ZonedDateTime zonedDateTime, @NotNull Duration duration) {
        this.startDate = zonedDateTime;
        this.duration = duration;

        int startDay = zonedDateTime.getDayOfMonth();
        int startMonth = zonedDateTime.getMonthValue();
        int startYear = zonedDateTime.getYear();

        ZonedDateTime endDate = zonedDateTime.plus(duration);
        int endDay = endDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        long[] ymdRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_YEAR_MONTH_DAY);
        long[] ydRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_YEAR_DAY);
        long[] mdRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_MONTH_DAY);
        long[] dayRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_DAY);
        long[] hourRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_HOUR);
        long[] minuteRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_MINUTE);
        long[] secondRes = CalendarUtils.calculateIntervals(startYear, startMonth, startDay, endYear, endMonth, endDay, CalendarUtils.MODE_SECOND);

        int[] appendants = new int[ymdRes.length];
        appendants[CalendarUtils.YEAR] = R.plurals.year;
        appendants[CalendarUtils.MONTH] = R.plurals.month;
        appendants[CalendarUtils.DAY] = R.plurals.day;
        appendants[CalendarUtils.HOUR] = R.plurals.hour;
        appendants[CalendarUtils.MINUTE] = R.plurals.minute;
        appendants[CalendarUtils.SECOND] = R.plurals.second;

        binding.resultIntervalYMD.setText(StringUtils.joinUnless(getResources(), ymdRes, appendants, ", ", 0));
        binding.resultIntervalYD.setText(StringUtils.joinUnless(getResources(), ydRes, appendants, ", ", 0));
        binding.resultIntervalMD.setText(StringUtils.joinUnless(getResources(), mdRes, appendants, ", ", 0));
        binding.resultIntervalDay.setText(StringUtils.joinUnless(getResources(), dayRes, appendants, ", ", 0));
        binding.resultIntervalHour.setText(StringUtils.joinUnless(getResources(), hourRes, appendants, ", ", 0));
        binding.resultIntervalMinute.setText(StringUtils.joinUnless(getResources(), minuteRes, appendants, ", ", 0));
        binding.resultIntervalSecond.setText(StringUtils.joinUnless(getResources(), secondRes, appendants, ", ", 0));
    }

    private enum Type {
        RANGE_START(DateTimePickerDialog.DateRangeMode.START),
        RANGE_END(DateTimePickerDialog.DateRangeMode.END);

        DateTimePickerDialog.DateRangeMode mode;

        Type(DateTimePickerDialog.DateRangeMode mode) {
            this.mode = mode;
        }
    }
}