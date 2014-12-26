package com.lweynant.wastecalendar.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lweynant.wastecalendar.DateUtil;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class WasteContentResolver {

	final private IContentResolver resolver;
	final private IWasteEventFactory factory;

	public WasteContentResolver(final Context context) {
		this(new ContentResolverAdapter(context), new WasteEventFactory(
				new Resources(context)));
	}
	public WasteContentResolver(IContentResolver resolver,
			IWasteEventFactory factory) {
		this.resolver = resolver;
		this.factory = factory;
	}

	public void startInsertEvent(LocalizedType type, IDate date) {
		DatabaseType dbType = factory.toDatabaseFormat(type);
		resolver.startInsert(dbType, date);
	}

	private Cursor queryUpcomingWasteEvents(IDate day) {
		Cursor c = null;
		IDate nextDay = day;
		String selection = WasteEventTableMetaData.WASTE_EVENT_YEAR
				+ " = ? AND " + WasteEventTableMetaData.WASTE_EVENT_MONTH
				+ " = ? AND " + WasteEventTableMetaData.WASTE_EVENT_DAY
				+ " = ?";
		Log.d("resolver", "selection: " + selection);
		//check for the next 2 weeks (this is a hack, need to find a better way)
		for (int i = 0; i < 14; i++) {
			nextDay = nextDay.dayAfter();
			String[] selectionArgs = {Integer.toString(nextDay.year()),
					Integer.toString(nextDay.month()),
					Integer.toString(nextDay.dayOfMonth()),};
			Log.d("resolver",
					"query date = " + DateUtil.getFormattedDay(nextDay) + " - "
							+ selectionArgs[0] + " " + selectionArgs[1] + " "
							+ selectionArgs[2]);
			c = resolver.query(WasteEventTableMetaData.fullProjection,
					selection, selectionArgs);
			if (!isEmpty(c)) {
				Log.d("resolver", "found event");
				break;
			}
			c.close();
		}
		return c;
	}


	private boolean isEmpty(Cursor c) {
		return c == null || c.getCount() == 0;
	}
	public List<IWasteEvent> getUpcomingWasteEvents(IDate day) {
		CursorReader reader = new CursorReader(factory);
		Cursor cursor = queryUpcomingWasteEvents(day);
		reader.read(cursor);
		ArrayList<IWasteEvent> events = new ArrayList<IWasteEvent>(
				cursor.getCount());
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			IWasteEvent e = reader.getWasteEvent();
			events.add(e);
		}
		cursor.close();
		return events;
	}
	public void startDeleteEvent(LocalizedType type, IDate date) {
		DatabaseType dbType = factory.toDatabaseFormat(type);
		String where = "type = ? AND year = ? AND month = ? AND day = ?";
		String[] selectionArgs = new String[]{dbType.value(),
				Integer.toString(date.year()), Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())};
		resolver.startDelete(where, selectionArgs);
	}
	public void startUpdateCollectedFieldForEvent(LocalizedType localizedType,
			IDate date, boolean collected) {
		DatabaseType dbType = factory.toDatabaseFormat(localizedType);
		String where = "type = ? AND year = ? AND month = ? AND day = ?";
		String[] selectionArgs = new String[]{dbType.value(),
				Integer.toString(date.year()), Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())};
		resolver.startUpdate(dbType, date, collected, where, selectionArgs);
	}

}
