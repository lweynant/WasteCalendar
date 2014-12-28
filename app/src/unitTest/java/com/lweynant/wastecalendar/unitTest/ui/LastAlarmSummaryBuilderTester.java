package com.lweynant.wastecalendar.unitTest.ui;
import android.annotation.SuppressLint;
import android.test.suitebuilder.annotation.SmallTest;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.lweynant.wastecalendar.IClock;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.ui.LastAlarmSummaryBuilder;

import junit.framework.TestCase;


public class LastAlarmSummaryBuilderTester extends TestCase {

	private LastAlarmSummaryBuilder sut;
	private ILocalizer resources;
	private String summary_string_format;
	private IClock clock;

	public void setUp() throws Exception {
		resources = mock(ILocalizer.class);
		summary_string_format = "%1$s %2$s set on%3$s";
		when(resources.string(R.string.last_alarm_summary_type_alarm_setat)).thenReturn(summary_string_format);
		clock = mock(IClock.class);
		sut = new LastAlarmSummaryBuilder(resources, clock);
	}

	@SmallTest
	public void testNoAlarmSet() {
		String noAlarmSet = "no alarm set";
        when(resources.string(R.string.preferences_last_alarm_set_default_value)).thenReturn(noAlarmSet);	
		String summary = sut.build();
		assertThat(summary, is(noAlarmSet));
	}
	@SmallTest
	public void testOnlyTypeSet() {
		String type = "PMD";
		sut.setType(type);
		String summary = sut.build();
		assertThat(summary, containsString(type));
	}
	@SuppressWarnings("unchecked")
	@SmallTest
	public void testOnlyAlarmSet() {
		String alarm = "23-4-99";
		String current = "2-4-00";
        when(clock.formattedCurrentTime()).thenReturn(current);
		sut.setAlarm(alarm);
		String summary = sut.build();
		assertThat(summary, allOf(containsString(alarm), containsString(current)));
	}

	
	@SuppressWarnings("unchecked")
	@SmallTest
	public void testAll() {
		String type = "PMD";
		String alarm = "23-4-99";
		String current = "2-4-00";
        when(clock.formattedCurrentTime()).thenReturn(current);

		sut.setType(type);
		sut.setAlarm(alarm);

		String summary = sut.build();
		assertThat(summary, allOf(containsString(type), containsString(alarm), containsString(current)));
	}
}
