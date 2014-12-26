package com.lweynant.wastecalendar.provider;

import android.database.Cursor;

import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class CursorReader implements ICursorContent  {

	private Cursor cursor;
	private WasteEventKeyValues keyValues;
	private CursorKeyValues cursorKeyValueReader;

	public CursorReader(IWasteEventFactory f) {
		keyValues = new WasteEventKeyValues(f);
	}
	
	public ICursorContent read(Cursor cursor){
		cursorKeyValueReader = new CursorKeyValues(cursor);
		this.cursor = cursor;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.lweynant.wastecalendar.provider.CursorContent#getWasteEvent()
	 */
	@Override
	public IWasteEvent getWasteEvent() {
		if (isCursorOnValidPosition(cursor)) return null;
		if (isCursorEmpty(cursor)) return null;
		IWasteEvent event = keyValues.readFrom(cursorKeyValueReader);
		return event;
	}

	@Override
	public int getId() {
		return cursor.getInt(cursor.getColumnIndex(WasteEventTableMetaData._ID));
	}


	private boolean isCursorEmpty(Cursor cursor) {
		return cursor.getCount() == 0;
	}

	private boolean isCursorOnValidPosition(Cursor cursor) {
		return cursor.isBeforeFirst() || cursor.isAfterLast();
	}

	public boolean isEmpty() {
		return cursor == null || cursor.getCount() == 0;
	}


}
