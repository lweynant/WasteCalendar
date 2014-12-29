package com.lweynant.wastecalendar.ui;

import com.lweynant.wastecalendar.IClock;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.ILocalizer;

public class LastAlarmSummaryBuilder {

    private ILocalizer localizer;
    private String type = "";
    private String format;
    private String alarm = "";
    private String setAt = "";
    private IClock clock;


    public LastAlarmSummaryBuilder(ILocalizer localizer, IClock clock) {
        this.localizer = localizer;
        this.format = localizer.string(R.string.last_alarm_summary_type_alarm_setat);
        this.clock = clock;
    }

    public String build() {
        if (set()) {
            return String.format(format, type, alarm, setAt);
        } else {
            return localizer.string(R.string.preferences_last_alarm_set_default_value);
        }
    }

    private boolean set() {
        return !type.isEmpty() || !alarm.isEmpty() || !setAt.isEmpty();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
        setAt = clock.formattedCurrentTime();
    }


}
