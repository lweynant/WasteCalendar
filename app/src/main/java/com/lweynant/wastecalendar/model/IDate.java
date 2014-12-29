package com.lweynant.wastecalendar.model;

import java.util.Calendar;


public interface IDate extends Comparable<IDate> {

    int year();

    int month();

    int dayOfMonth();

    boolean isSameAs(IDate when);

    boolean isYesterdayFor(IDate when);

    boolean isTomorrowFor(IDate when);

    boolean isDayAfterTomorrowFor(IDate when);

    IDate dayBefore();

    IDate dayAfter();

    boolean before(IDate when);

    boolean after(IDate when);

    Calendar calendar();
}
