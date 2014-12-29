package com.lweynant.wastecalendar;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;

import java.util.Calendar;

public class Clock implements IClock {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public IDate today() {
        Calendar c = Calendar.getInstance();
        return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public String formattedCurrentTime() {
        return DateUtil.getFormattedTime(new Clock().currentTimeMillis());
    }

}
