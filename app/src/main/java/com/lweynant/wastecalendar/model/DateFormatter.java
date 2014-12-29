package com.lweynant.wastecalendar.model;

import android.annotation.SuppressLint;

import com.lweynant.wastecalendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private ILocalizer localizer;

    public DateFormatter(ILocalizer localizer) {
        this.localizer = localizer;
    }

    public String format(int format, String day, String month, int dayOfMonth) {
        String f = localizer.string(format);
        String d = String.format(f, day, month, dayOfMonth);
        return d;
    }

    @SuppressLint("SimpleDateFormat")
    public String month(IDate when) {
        return new SimpleDateFormat("MMMM").format(convert(when));
    }

    @SuppressLint("SimpleDateFormat")
    public String day(IDate when) {
        return new SimpleDateFormat("EEEE").format(convert(when));
    }

    private Date convert(IDate when) {
        return when.calendar().getTime();
    }

    public String format(IDate date) {
        return format(R.string.date_day_m_d, capitalizeFirstLetter(day(date)), month(date), date.dayOfMonth());
    }

    private String capitalizeFirstLetter(String day) {
        return Character.toUpperCase(day.charAt(0)) + day.substring(1);
    }

}
