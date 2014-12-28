package com.lweynant.wastecalendar.unitTest.provider;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.provider.SQLBuilder;

import junit.framework.TestCase;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SQLBuilderTests extends TestCase{

	private SQLBuilder sut;

	public void setUp() throws Exception {
		sut = new SQLBuilder();
	}
	@SmallTest
	public void testQueryFromAGivenDate() {
		Date date = new Date(2013, Calendar.AUGUST, 2);
		sut.queryFrom(date);
		assertThat(sut.selection(), is("year > ? OR ( (year = ?  AND month > ?) OR (year = ? AND month = ?  AND day >= ?))"));
		assertThat(sut.selectionArgs(), is(new String[]{
				Integer.toString(2013),
				Integer.toString(2013),
				Integer.toString(7),
				Integer.toString(2013),
				Integer.toString(7),
				Integer.toString(2)
		}));
	}
	@SmallTest
	public void testQueryUntilAGivenDate() {
		Date date = new Date(2013, Calendar.AUGUST, 2);
		sut.queryUntil(date);
		assertThat(sut.selection(), is("year < ? OR ( (year = ?  AND month < ?) OR (year = ? AND month = ?  AND day <= ?))"));
		assertThat(sut.selectionArgs(), is(new String[]{
				Integer.toString(2013),
				Integer.toString(2013),
				Integer.toString(7),
				Integer.toString(2013),
				Integer.toString(7),
				Integer.toString(2)
		}));
	}
	@SmallTest
	public void testQueryGivenType(){
		Date date = new Date(2013, Calendar.AUGUST, 2);
		sut.query("type", date);
		assertThat(sut.selection(), is("type = ? AND year = ? AND month = ? AND day =?"));
		assertThat(sut.selectionArgs(), is(new String[]{
				"type",
				Integer.toString(2013),
				Integer.toString(7),
				Integer.toString(2)
		}));
		
	}

}
