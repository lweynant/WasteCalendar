package com.lweynant.wastecalendar.provider;

import android.content.ContentValues;

import com.lweynant.wastecalendar.model.IKeyValuesWriter;

public class ContentValuesWriter implements IKeyValuesWriter {

	private ContentValues contentValues;

	public ContentValuesWriter(ContentValues cv){
		contentValues = cv;
	}
	@Override
	public void put(String key, String value) {
		contentValues.put(key, value);
	}

	@Override
	public void put(String key, int value) {
		contentValues.put(key, value);
	}

}
