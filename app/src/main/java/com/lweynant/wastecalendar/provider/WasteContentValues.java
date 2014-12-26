package com.lweynant.wastecalendar.provider;

import android.content.ContentValues;

import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;

public class WasteContentValues implements IWasteContentValues {

	private ContentValues cv;
	private WasteEventKeyValues keyValues;
	private ContentValuesWriter writer;

	public WasteContentValues(ContentValues cv){
		this.cv = cv;
	}
	public WasteContentValues(IWasteEventFactory f,DatabaseType type, IDate collectionDate) {
		this(f, type, collectionDate, false);
	}
	public WasteContentValues(IWasteEventFactory f, DatabaseType type, IDate date,
			boolean collected) {
		keyValues = new WasteEventKeyValues(f);
		cv = new ContentValues();  
		writer = new ContentValuesWriter(cv);
		IWasteEvent event = f.createWasteEvent(type, date);
		event.setCollected(collected);
		keyValues.writeTo(event, writer);
		
	}
	@Override
	public ContentValues getContentValues(){
		return cv;
	}

}
