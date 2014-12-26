package com.lweynant.wastecalendar.model;

import android.app.Notification;
import android.app.PendingIntent;

public interface INotificationHandler {


	PendingIntent getContentIntent(int requestCodeNotify, int flagUpdateCurrent);
	void sendNotification(Notification notification);

	
}
