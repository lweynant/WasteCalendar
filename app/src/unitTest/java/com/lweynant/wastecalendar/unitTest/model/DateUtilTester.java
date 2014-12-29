package com.lweynant.wastecalendar.unitTest.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.DateUtil;
import com.lweynant.wastecalendar.model.Date;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilTester extends TestCase {

    @Before
    public void setUp() throws Exception {
    }

    @SmallTest
    public void testFormatOfDate() {
        Date date = new Date(2002, Calendar.MAY, 14);
        String string = DateUtil.getFormattedDay(date);
        assertThat(string, is("14-05-2002"));
    }

    @SmallTest
    public void testFormatOfDateTimeInMs() {
        Date date = new Date(2004, Calendar.MARCH, 10);
        String string = DateUtil.getFormattedDay(date.calendar().getTimeInMillis());
        assertThat(string, is("10-03-2004"));
    }

    @SmallTest
    public void testFormatOfTime() {
        Calendar c = new Date(2004, Calendar.MARCH, 10).calendar();
        c.set(Calendar.HOUR_OF_DAY, 19);
        c.set(Calendar.MINUTE, 30);
        String string = DateUtil.getFormattedTime(c.getTimeInMillis());
        assertThat(string, is("10-03-2004 19:30"));
    }


}
