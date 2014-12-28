package com.lweynant.wastecalendar.unitTest.provider;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.IContentResolver;
import com.lweynant.wastecalendar.provider.LocalizedType;
import com.lweynant.wastecalendar.provider.WasteContentResolver;

import junit.framework.TestCase;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WasteContentResolverTester extends TestCase {

	private WasteContentResolver sut;
	private IContentResolver resolver;
	private IDate date;
	private LocalizedType localizedType;
	private DatabaseType databaseType;

	public void setUp() throws Exception {
		resolver = mock(IContentResolver.class);
		date = new Date(2013, 0, 30);
        IWasteEventFactory factory = mock(IWasteEventFactory.class);
		localizedType = new LocalizedType("localized type");
		databaseType = new DatabaseType("database type");
		when(factory.toDatabaseFormat(localizedType)).thenReturn(databaseType);
		ILocalizer stubbedLocalizer = mock(ILocalizer.class);
		when(stubbedLocalizer.string(anyInt())).thenReturn("stubbed-type");
		sut = new WasteContentResolver(resolver, factory);
	}

	@SmallTest
	public void testInsertAnEvent() {
		sut.startInsertEvent(localizedType, date);
		verify(resolver).startInsert(databaseType, date);
	}
	@SmallTest
	public void testDeleteAnEvent() {
		String selection = "type = ? AND year = ? AND month = ? AND day = ?";
		String[] selectionArgs = new String[]{
				databaseType.value(),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
		sut.startDeleteEvent(localizedType, date);
		verify(resolver).startDelete(selection, selectionArgs);
	}
	@SmallTest
	public void testMarkEventAsCollected() {
		String selection = "type = ? AND year = ? AND month = ? AND day = ?";
		String[] selectionArgs = new String[]{
				databaseType.value(),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
		boolean collected = true;
		sut.startUpdateCollectedFieldForEvent(localizedType, date, collected);
		verify(resolver).startUpdate(databaseType, date, collected, selection, selectionArgs);
	}
	@SmallTest
	public void testMarkEventAsUncollected() {
		String selection = "type = ? AND year = ? AND month = ? AND day = ?";
		String[] selectionArgs = new String[]{
				databaseType.value(),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
		boolean collected = false;
		sut.startUpdateCollectedFieldForEvent(localizedType, date, collected);
		verify(resolver).startUpdate(databaseType, date, collected, selection, selectionArgs);
	}


}
