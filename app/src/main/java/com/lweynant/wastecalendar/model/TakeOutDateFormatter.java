package com.lweynant.wastecalendar.model;


import com.lweynant.wastecalendar.R;

public class TakeOutDateFormatter extends RelativeDateFormatter {

    public TakeOutDateFormatter(IDate ref, ILocalizer localizer) {
        super(ref, localizer);
    }


    @Override
    public String getRelativeTimeString(IDate when) {
        if (isSameDay(when)) return thisEvening();
        else if (isTomorrow(when)) return tomorrow();
        else if (isDayAfterTomorrow(when)) return dayAfterTomorrow();
        else return date(R.string.date_evening_day_m_d, when);
    }


    private String dayAfterTomorrow() {
        return localizer.string(R.string.day_after_tomorrow);
    }


    private String tomorrow() {
        return localizer.string(R.string.tomorrow_evening);
    }


    private String thisEvening() {
        return localizer.string(R.string.this_evening);
    }


    private boolean isTomorrow(IDate when) {
        return when.isTomorrowFor(ref);
    }

    private boolean isDayAfterTomorrow(IDate when) {
        return when.isDayAfterTomorrowFor(ref);
    }

}
