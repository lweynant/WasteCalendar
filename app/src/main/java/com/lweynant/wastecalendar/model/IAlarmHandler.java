package com.lweynant.wastecalendar.model;

import android.app.PendingIntent;
import android.content.Intent;

public interface IAlarmHandler {

	Intent getContentIntent();
	PendingIntent getPendingIntent(Intent intent, int requestCode, int flags);
	void set(int rtc, long timeInMillis, PendingIntent pendingIntent);

}
