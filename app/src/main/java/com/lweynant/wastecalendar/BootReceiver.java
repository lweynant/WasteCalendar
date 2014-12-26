package com.lweynant.wastecalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lweynant.wastecalendar.model.IDate;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";

	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received intent " + intent.getAction());
		AlarmService.setAlarmRequest(context, today());
	}

	private IDate today() {
		return new Clock().today();
	}
}
