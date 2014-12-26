package com.lweynant.wastecalendar;

import com.lweynant.wastecalendar.model.IDate;

public interface IClock {
    public abstract long currentTimeMillis();
    public abstract IDate today();
	public abstract String formattedCurrentTime();
}
