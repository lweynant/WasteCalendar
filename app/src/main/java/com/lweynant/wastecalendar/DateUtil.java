package com.lweynant.wastecalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;

public class DateUtil {
	
	public static final String EXTRA_YEAR = "com.lweynant.wastecalendar.dateutil.extra.YEAR";
	public static final String EXTRA_MONTH = "com.lweynant.wastecalendar.dateutil.extra.MONTH";
	public static final String EXTRA_DAY_OF_MONTH = "com.lweynant.wastecalendar.dateutil.extra.DAY_OF_MONTH";
	public static String formattedDay = "dd-MM-yyyy";
	public static String formattedTime = "dd-MM-yyyy HH:mm";
	@SuppressLint("SimpleDateFormat")
	public static String getFormattedDay(long timeInMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMillis);
		return format(c, formattedDay);
	}
	public static String getFormattedDay(IDate day) {
		Calendar c = day.calendar();
		return format(c, formattedDay);
	}
	@SuppressLint("SimpleDateFormat")
	private static String format(Calendar c, String format) {
		return new SimpleDateFormat(format).format(c.getTime());
	}
	public static void writeDateToIntent(IDate date, Intent intent) {
		intent.putExtra(EXTRA_YEAR, date.year());
		intent.putExtra(EXTRA_MONTH, date.month());
		intent.putExtra(EXTRA_DAY_OF_MONTH, date.dayOfMonth());
	}
	public static IDate readDateFromIntent(Intent intent) {
		return new Date(intent.getIntExtra(EXTRA_YEAR, 0), intent.getIntExtra(EXTRA_MONTH, 0), intent.getIntExtra(EXTRA_DAY_OF_MONTH, 0));
	}
	public static String getFormattedTime(long timeInMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMillis);
		return format(c, formattedTime);
	}

}
