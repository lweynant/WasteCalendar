package com.lweynant.wastecalendar.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.lweynant.wastecalendar.DateUtil;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.ui.LastAlarmSummaryBuilder;

import java.util.Calendar;
import java.util.List;

public class Alarm {

    public static final String ACTION_NOTIFY_ALARM = "com.lweynant.wastecalendar.alarm.action.SET_ALARM";

    protected static final String EXTRA_NAME = "com.lweynant.wastecalendar.extra.NAME";
    protected static final String EXTRA_ICON = "com.lweynant.wastecalendar.extra.ICON";
    protected static final int REQUEST_CODE_SET_ALARM = 0;
    protected static final int REQUEST_CODE_NOTIFY = 1;

    private IPreferences sharedPrefs;
    private IResources resources;

    private ILocalizer localizer;

    public Alarm(IPreferences sharedPrefs, IResources resources, ILocalizer localizer) {
        this.sharedPrefs = sharedPrefs;
        this.resources = resources;
        this.localizer = localizer;
    }

    public void setAlarmForEventsOn(IAlarmHandler alarmManager,
                                    List<IWasteEvent> events, LastAlarmSummaryBuilder summaryBuilder) {
        if (events.size() > 0) {
            IWasteEvent wasteEvent = events.get(0);
            Intent intent = alarmManager.getContentIntent();
            PendingIntent pendingIntent = alarmManager.getPendingIntent(
                    fillInIntent(intent, events), REQUEST_CODE_SET_ALARM,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            long timeInMs = getTakeOutTimeInMs(wasteEvent);
            alarmManager.set(AlarmManager.RTC, timeInMs, pendingIntent);

            summaryBuilder.setType(wasteEvent.type().value());
            summaryBuilder.setAlarm(DateUtil.getFormattedTime(timeInMs));
            sharedPrefs.set(IPreferences.LAST_ALARM, summaryBuilder.build());

        }
    }

    private Intent fillInIntent(Intent intent, List<IWasteEvent> events) {
        IWasteEvent wasteEvent = events.get(0);
        IDate takeOutDate = wasteEvent.takeOutDate();
        intent.setAction(ACTION_NOTIFY_ALARM);
        String[] names = new String[events.size()];
        int[] icons = new int[events.size()];
        for (int i = 0; i < events.size(); i++){
            IWasteEvent event = events.get(i);
            names[i] = event.type().value();
            icons[i] = event.imageResource();
        }
        intent.putExtra(EXTRA_NAME, names);
        intent.putExtra(EXTRA_ICON, icons);
        DateUtil.writeDateToIntent(takeOutDate, intent);
        return intent;
    }

    private long getTakeOutTimeInMs(IWasteEvent wasteEvent) {
        Calendar c = wasteEvent.takeOutDate().calendar();
        c.set(Calendar.HOUR_OF_DAY,
                sharedPrefs.getInt(IPreferences.ALARM_HOUR, 0));
        long timeInMillis = c.getTimeInMillis();
        return timeInMillis;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void notify(INotificationHandler notificationManager,
                       Notification.Builder builder, Intent intent) {
        PendingIntent pi = notificationManager.getContentIntent(
                REQUEST_CODE_NOTIFY, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = fillInBuilder(builder, intent, pi);

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        notificationManager.sendNotification(notification);
    }

    private Notification.Builder fillInBuilder(
            Notification.Builder builder, Intent intent, PendingIntent pi) {
        int[] images = intent.getIntArrayExtra(Alarm.EXTRA_ICON);
        String[] names = intent.getStringArrayExtra(Alarm.EXTRA_NAME);
        String name = names[0];
        for (int i = 1; i < names.length; i++) {
            name += ", " + names[i];
        }
        builder.setSmallIcon(R.drawable.ic_action_delete);
        int largeIcon = images.length == 1 ? images[0] : R.drawable.trash;
        builder.setLargeIcon(resources.bitmap(largeIcon));
        builder.setTicker(name);
        builder.setContentTitle(name);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentText(localizer.string(R.string.this_evening));
        builder.setContentIntent(pi);
        builder.setLights(Color.BLUE, 1000, 5000);
        return builder;
    }

}
