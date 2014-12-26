package com.lweynant.wastecalendar.model;


public interface IPreferences {

	static final String FILENAME = "com.lweynant.wastecalendar_preferences";
	static final String ALARM_HOUR_DEFAULT_VALUE = "19";
	final static String ALARM_HOUR = "com.lweynant.wastecalendar.sharedpreferences.ALARM_HOUR";

	final static String LAST_ALARM = "com.lweynant.wastecalendar.sharedpreferences.LAST_ALARM";


	int getInt(String key, int defaultValue);
	void set(String key, String value);
	
}
