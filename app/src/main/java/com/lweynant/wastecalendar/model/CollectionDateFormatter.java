package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.R;

public class CollectionDateFormatter extends RelativeDateFormatter {

    public CollectionDateFormatter(IDate when, ILocalizer localizer) {
        super(when, localizer);
    }

    @Override
    public String getRelativeTimeString(IDate when) {
        if (isSameDay(when)) return today();
        else if (isYesterday(when)) return yesterday();
        else return date(when);
    }


    public String date(IDate when) {
        if (ref.isSameYearAs(when)) {
            return date(R.string.date_day_m_d, when, false);
        } else {
            return date(R.string.date_day_y_m_d, when, true);
        }
    }

    private boolean isYesterday(IDate when) {
        return when.isYesterdayFor(ref);
    }

    private String yesterday() {
        return localizer.string(R.string.yesterday);
    }

    private String today() {
        return localizer.string(R.string.today);
    }

}
