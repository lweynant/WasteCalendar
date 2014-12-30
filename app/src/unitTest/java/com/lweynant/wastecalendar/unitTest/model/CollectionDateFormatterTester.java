package com.lweynant.wastecalendar.unitTest.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.CollectionDateFormatter;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.model.TakeOutDateFormatter;

import junit.framework.TestCase;

import org.hamcrest.CoreMatchers;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionDateFormatterTester extends TestCase {

    private IDate yesterday;
    private IDate today;
    private IDate todayLastYear;
    private IDate aDayInFuture;
    private ILocalizer localizer;
    private IDate tomorrow;
    private IDate dayAfterTomorrow;
    private IRelativeDateFormatter sut;
    private IDate aDayInThePast;

    public void setUp() throws Exception {
        yesterday = new Date(2013, Calendar.JANUARY, 9);
        today = new Date(2013, Calendar.JANUARY, 10);
        todayLastYear = new Date(2012, Calendar.JANUARY, 10);
        tomorrow = new Date(2013, Calendar.JANUARY, 11);
        dayAfterTomorrow = new Date(2013, Calendar.JANUARY, 12);
        aDayInFuture = new Date(2013, Calendar.FEBRUARY, 10);
        aDayInThePast = new Date(2013, Calendar.JANUARY, 2);
        localizer = mock(ILocalizer.class);
        sut = new CollectionDateFormatter(today, localizer);
    }

    @SmallTest
    public void testToday() {
        when(localizer.string(R.string.today)).thenReturn("today!");
        String string = sut.getRelativeTimeString(today);
        assertThat(string, is("today!"));
    }
    @SmallTest
    public void testYesterday() {
        when(localizer.string(R.string.yesterday)).thenReturn("yesterday!");
        String string = sut.getRelativeTimeString(yesterday);
        assertThat(string, is("yesterday!"));
    }
    @SmallTest
    public void testLastYear() {
        when(localizer.string(R.string.date_day_y_m_d)).thenReturn("%1$s, %2$d %3$s %4$d");
        String string = sut.getRelativeTimeString(todayLastYear);
        assertThat(string, is("Tuesday, 2012 January 10"));
    }

    @SmallTest
    public void testTomorrow() {
        when(localizer.string(R.string.date_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(tomorrow);
        assertThat(string, is("Friday, January 11"));
    }

    @SmallTest
    public void testDayAfterTomorrow() {
        when(localizer.string(R.string.date_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(dayAfterTomorrow);
        assertThat(string, is("Saturday, January 12"));
    }

    @SmallTest
    public void testDayInTheFuture() {
        when(localizer.string(R.string.date_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(aDayInFuture);
        assertThat(string, is("Sunday, February 10"));
    }

    @SmallTest
    public void testDayInThePast() {
        when(localizer.string(R.string.date_day_m_d)).thenReturn("%1$s, %2$s %3$d");
        String string = sut.getRelativeTimeString(aDayInThePast);
        assertThat(string, is("Wednesday, January 2"));
    }

}
