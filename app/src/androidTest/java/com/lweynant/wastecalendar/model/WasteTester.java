package com.lweynant.wastecalendar.model;

import android.test.suitebuilder.annotation.SmallTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.provider.LocalizedType;

import junit.framework.TestCase;

public class WasteTester extends TestCase{

	private IDate today;

	public void setUp() throws Exception {
		today = new Date(2013, Calendar.JANUARY, 11);
	}

	@SmallTest
	public void testPMD() {
		Waste w = new Waste(new LocalizedType("PMD"), R.drawable.pmd, today);
		assertThat(w.type().value(), is("PMD"));
		assertThat(w.collectionDate(), is(today));
		assertThat(w.takeOutDate().isYesterdayFor(w.collectionDate()), is(true));
		assertThat(w.imageResource(), is(R.drawable.pmd));
	}

	@SmallTest
	public void testNewWasteEventIsNotCollectedYet() {
		IWasteEvent w = createAWasteEvent();
		assertFalse(w.isCollected());
	}

	@SmallTest
	public void testWastEventIsCollectedAfterSettingItAsCollected() {
		IWasteEvent w = createAWasteEvent();
		w.setCollected(true);
		assertTrue(w.isCollected());
	}
	
	@SmallTest
	public void testCompareToEventWithALaterCollectionDate(){
		IWasteEvent event = createAWasteEvent();
		IWasteEvent after = createWasteEventForDate(event.collectionDate().dayAfter());
		assertThat(event.compareTo(after), is(-1));
	}
	@SmallTest
	public void testCompareToEventWithAnEarlierCollectionDate(){
		IWasteEvent event = createAWasteEvent();
		IWasteEvent before = createWasteEventForDate(event.collectionDate().dayBefore());
		assertThat(event.compareTo(before), is(1));
	}
	@SmallTest
	public void testCompareToSameEvent(){
		IWasteEvent event = createAWasteEvent();
		assertThat(event.compareTo(event), is(0));
	}
	@SmallTest
	public void testCompareToOtherEventWithTheSameDate(){
		IWasteEvent event = createWasteEvent("type1", today);
		IWasteEvent other = createWasteEvent("other", today);
		assertThat(event.compareTo(other), is(0));
	}
	
	@SmallTest
	public void testEqualsOnSameEvent(){
		IWasteEvent event = createAWasteEvent();
		assertThat(event.equals(event), is(true));
	}
	@SmallTest
	public void testEqualsOnEventWithSameTypeAndDate(){
		IWasteEvent event = createWasteEvent("type", today);
		IWasteEvent other = createWasteEvent("type", today);
		assertThat(event.equals(other), is(true));
	}
	@SmallTest
	public void testEqualsOnEventWithSameTypeAndDateButOnlyOneIsCollected(){
		IWasteEvent event = createWasteEvent("type", today);
		event.setCollected(true);
		IWasteEvent other = createWasteEvent("type", today);
		assertThat(event.equals(other), is(false));
	}
	@SmallTest
	public void testEqualsOnEventWithSameTypeButOtherDate(){
		IWasteEvent event = createWasteEvent("type", today);
		IWasteEvent other = createWasteEvent("type", today.dayAfter());
		assertThat(event.equals(other), is(false));
	}
	@SmallTest
	public void testEqualsOnEventWithSameDateButOtherType(){
		IWasteEvent event = createWasteEvent("this", today);
		IWasteEvent other = createWasteEvent("that", today);
		assertThat(event.equals(other), is(false));
	}
	@SmallTest
	public void testEqualsOnDifferentObject(){
		IWasteEvent event = createAWasteEvent();
		assertThat(event.equals("string"), is(false));
	}
	
	@SmallTest
	public void testToString(){
		IWasteEvent event = createWasteEvent("event", new Date(2013, Calendar.JANUARY, 3));
		assertThat(event.toString(), is("event: 2013-1-3"));
	}
	
	
	private IWasteEvent createWasteEvent(String type, IDate day) {
		Waste w = new Waste(new LocalizedType(type), 0, day);
		return w;
	}

	private IWasteEvent createWasteEventForDate(IDate day) {
		return createWasteEvent("a waste event", day);
	}

	private IWasteEvent createAWasteEvent() {
		return createWasteEvent("a waste event", today);
	}

}
