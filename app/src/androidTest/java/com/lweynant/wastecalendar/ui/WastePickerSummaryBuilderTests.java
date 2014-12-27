package com.lweynant.wastecalendar.ui;

import android.test.suitebuilder.annotation.SmallTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Calendar;


import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;

import junit.framework.TestCase;

public class WastePickerSummaryBuilderTests extends TestCase {

	private WastePickerSummaryBuilder sut;
	private IDisplay display;

	public void setUp() throws Exception {
		display = mock(IDisplay.class);
		sut = new WastePickerSummaryBuilder(display);
	}

	@SmallTest
	public void testempty() {
		sut.display();
		verify(display).diplay("");
	}
	
	@SmallTest
	public void testoneDay() {
		sut.add(day(3));
		sut.display();
		verify(display).diplay("3");
	}

	private IDate day(int i) {
		return new Date(2013, Calendar.MARCH, i);
	}

	@SmallTest
	public void testtwoDays(){
		sut.add(day(2));
		sut.add(day(3));
		sut.display();
		verify(display).diplay("2, 3");
	}
	@SmallTest
	public void testtwoDaysInReverseOrder(){
		sut.add(day(2));
		sut.add(day(3));
		sut.display();
		verify(display).diplay("2, 3");
	}
	@SmallTest
	public void testtwoDaysOfDifferentMonthOnlyLastMonthIsShown(){
		sut.add(day(Calendar.MARCH, 2));
		sut.add(day(Calendar.JANUARY, 3));
		sut.display();
		verify(display).diplay("3");
	}

	@SmallTest
	public void testdaysOfDifferentMonthAddOntherOftherTheFirst(){
		sut.add(day(Calendar.MARCH, 2));
		sut.add(day(Calendar.JANUARY, 3));
		sut.add(day(Calendar.MARCH, 6));
		sut.display();
		verify(display).diplay("2, 6");
	}
	@SmallTest
	public void testdaysOfDifferentMonthAddSwitchToTheFirst(){
		sut.add(day(Calendar.MARCH, 2));
		sut.add(day(Calendar.JANUARY, 3));
		sut.switchTo(Calendar.MARCH);
		sut.display();
		verify(display).diplay("2");
	}
	@SmallTest
	public void testaddSameDayTwiceOnlyShownOnce(){
		sut.add(day(3));
		sut.add(day(3));
		sut.display();
		verify(display).diplay("3");
	}
	@SmallTest
	public void testemptyContainsNoDate(){
		assertFalse(sut.contains(day(9)));
	}
	@SmallTest
	public void testcontainsReturnsTrueOnAddedDate(){
		IDate day = day(3);
		sut.add(day);
		assertTrue(sut.contains(day));
	}
	@SmallTest
	public void testcontainsReturnsTrueOnAddedDateAfterSwitchingToOtherMonth(){
		IDate day = day(3);
		sut.add(day);
		sut.switchTo(day.month()+1);
		assertTrue(sut.contains(day));
	}
	@SmallTest
	public void testremovePreviouslyAddedDayNoLongerShows(){
		IDate day = day(3);
		sut.add(day);
		sut.remove(day);
		sut.display();
		verify(display).diplay("");
		
	}
	@SmallTest
	public void testremoveOnEmpty(){
		sut.remove(day(2));
		sut.display();
		verify(display).diplay("");
	}
	@SmallTest
	public void testremovePreviouslyAddedDayAfterSwitchingToOtherMonthNoLongerShows(){
		IDate day = day(3);
		sut.add(day);
		sut.switchTo(day.month()+1);
		sut.remove(day);
		sut.switchTo(day.month());
		sut.display();
		verify(display).diplay("");
		
	}
	private IDate day(int month, int dayOfMonth) {
		return new Date(2013, month, dayOfMonth);
	}
}
