package com.lweynant.wastecalendar.unitTest.model;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.DateFormatter;
import com.lweynant.wastecalendar.model.ILocalizer;

import junit.framework.TestCase;

public class DateFormatterTester extends TestCase {

	private ILocalizer localizer;
	private DateFormatter sut;

	public void setUp() throws Exception {
		localizer = mock(ILocalizer.class);
		sut = new DateFormatter(localizer);
	}

	public void testDay() {
		when(localizer.string(R.string.date_day_m_d)).thenReturn("%1$s, %2$s %3$d");
		String string = sut.format(new Date(2013, Calendar.JANUARY, 8));
		assertThat(string, containsString("Tuesday, January 8"));
	}

}
