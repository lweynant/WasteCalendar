package com.lweynant.wastecalendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.lweynant.wastecalendar.model.Alarm;
import com.lweynant.wastecalendar.model.IAlarmHandler;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.INotificationHandler;
import com.lweynant.wastecalendar.model.IPreferences;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.provider.WasteContentResolver;
import com.lweynant.wastecalendar.ui.LastAlarmSummaryBuilder;
import com.lweynant.wastecalendar.ui.WasteListActivity;

import java.util.List;

public class AlarmService extends IntentService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        Resources res = new Resources(this);
        alarm = new Alarm(new PreferencesHandler(this.getSharedPreferences(
                IPreferences.FILENAME, MODE_PRIVATE)), res, res);
        return super.onStartCommand(intent, flags, startId);

    }

    private Alarm alarm;
    private static final String TAG = "AlarmService";
    public static final String ACTION_SET_ALARM_REQUEST = "com.lweynant.wastecalendar.alarmservice.action.SET_ALARM_REQUEST";

    private static final class PreferencesHandler implements IPreferences {

        private SharedPreferences prefs;

        public PreferencesHandler(SharedPreferences prefs) {
            this.prefs = prefs;
        }

        @Override
        public int getInt(String key, int defaultValue) {
            if (key.equals(IPreferences.ALARM_HOUR)) {
                String value = prefs.getString(IPreferences.ALARM_HOUR,
                        IPreferences.ALARM_HOUR_DEFAULT_VALUE);
                return Integer.valueOf(value);
            } else
                return defaultValue;
        }

        @Override
        public void set(String key, String value) {
            prefs.edit().putString(key, value).commit();
        }
    }

    private static final class AlarmHandler implements IAlarmHandler {
        private final Context context;

        private AlarmHandler(Context context) {
            this.context = context;
        }

        @Override
        public Intent getContentIntent() {
            return new Intent(context, AlarmService.class);
        }

        @Override
        public PendingIntent getPendingIntent(Intent intent, int requestCode,
                                              int flags) {
            return PendingIntent
                    .getService(context, requestCode, intent, flags);
        }

        @Override
        public void set(int type, long timeInMillis, PendingIntent p) {
            Log.d(TAG,
                    "setting alarm for wasteEvent at time "
                            + DateUtil.getFormattedDay(timeInMillis));
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                    .set(type, timeInMillis, p);
        }

    }

    private final class NotificationHandler implements INotificationHandler {
        private Context context;

        public NotificationHandler(Context context) {
            this.context = context;
        }

        @Override
        public PendingIntent getContentIntent(int requestCode, int flags) {
            Intent i = new Intent(context, WasteListActivity.class);
            return PendingIntent.getActivity(context, requestCode, i, flags);
        }

        @Override
        public void sendNotification(Notification notification) {
            ((NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,
                    notification);
            Log.d(TAG,
                    "Notified wasteEvent" + notification.tickerText
                            + " for time: "
                            + DateUtil.getFormattedDay(notification.when));
        }
    }

    public static void setAlarmRequest(final Context context, IDate day) {
        Log.d(TAG, "setAlarm for " + DateUtil.getFormattedDay(day));
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ACTION_SET_ALARM_REQUEST);
        DateUtil.writeDateToIntent(day, intent);
        context.startService(intent);
    }

    public AlarmService() {
        super("AlarmService");
        alarm = null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Received intent: " + intent.getAction());
            String action = intent.getAction();
            if (ACTION_SET_ALARM_REQUEST.equals(action)) {
                IDate day = DateUtil.readDateFromIntent(intent);
                Log.d(TAG, "received action set alarm request for date: "
                        + DateUtil.getFormattedDay(day));

                setAlarmForUpcomingWasteEvents(day);
            } else if (Alarm.ACTION_NOTIFY_ALARM.equals(action)) {
                Log.d(TAG,
                        "received action to notify alarm for date: "
                                + DateUtil.getFormattedDay(DateUtil
                                .readDateFromIntent(intent)));
                notifyAlarmReceived(intent);

                setAlarmForUpcomingWasteEvents(tomorrow());
            }
        }
    }

    private IDate tomorrow() {
        return new Clock().today().dayAfter();
    }

    private void notifyAlarmReceived(Intent intent) {
        Notification.Builder builder = new Notification.Builder(
                this);

        alarm.notify(new NotificationHandler(this), builder, intent);
    }

    private void setAlarmForUpcomingWasteEvents(IDate day) {
        WasteContentResolver w = new WasteContentResolver(this);
        List<IWasteEvent> events = w.getUpcomingWasteEvents(day);
        alarm.setAlarmForEventsOn(new AlarmHandler(this), events,
                new LastAlarmSummaryBuilder(new Resources(this), new Clock()));
    }

}
