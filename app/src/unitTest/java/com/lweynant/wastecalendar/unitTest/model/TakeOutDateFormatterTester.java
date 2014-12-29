package com.lweynant.wastecalendar.unitTest.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.model.TakeOutDateFormatter;

import junit.framework.TestCase;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TakeOutDateFormatterTester extends TestCase {

    private IDate today;
    private IDate aDayInFuture;
    private ILocalizer localizer;
    private IDate tomorrow;
    private IDate dayAfterTomorrow;
    private IRelativeDateFormatter sut;
    private IDate aDayInThePast;

    public void setUp() throws Exception {
        today = new Date(2013, Calendar.JANUARY, 10);
        tomorrow = new Date(2013, Calendar.JANUARY, 11);
        dayAfterTomorrow = new Date(2013, Calendar.JANUARY, 12);
        aDayInFuture = new Date(2013, Calendar.FEBRUARY, 10);
        aDayInThePast = new Date(2013, Calendar.JANUARY, 2);
        localizer = mock(ILocalizer.class);
        sut = new TakeOutDateFormatter(today, localizer);
    }

    @SmallTest
    public void testToday() {
        when(localizer.string(R.string.this_evening)).thenReturn("this evening!");
        String string = sut.getRelativeTimeString(today);
        assertThat(string, is("this evening!"));
    }

    @SmallTest
    public void testTomorrow() {
        when(localizer.string(R.string.tomorrow_evening)).thenReturn("tomorrow evening");
        String string = sut.getRelativeTimeString(tomorrow);
        assertThat(string, is("tomorrow evening"));
    }

    @SmallTest
    public void testDayAfterTomorrow() {
        when(localizer.string(R.string.day_after_tomorrow)).thenReturn("day after tomorrow");
        String string = sut.getRelativeTimeString(dayAfterTomorrow);
        assertThat(string, is("day after tomorrow"));
    }

    @SmallTest
    public void testDayInTheFuture() {
        when(localizer.string(R.string.date_evening_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(aDayInFuture);
        assertThat(string, containsString("Sunday, February 10"));
    }

    @SmallTest
    public void testDayInThePast() {
        when(localizer.string(R.string.date_evening_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(aDayInThePast);
        assertThat(string, containsString("Wednesday, January 2"));
    }

}
