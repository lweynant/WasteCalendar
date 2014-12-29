package com.lweynant.wastecalendar.provider;

import android.database.Cursor;

import com.lweynant.wastecalendar.model.IKeyValuesReader;

public class CursorKeyValues implements IKeyValuesReader {

    Cursor cursor;

    public CursorKeyValues(Cursor c) {
        this.cursor = c;
    }

    @Override
    public String getString(String key) {
        int index = cursor.getColumnIndex(key);
        return cursor.getString(index);
    }

    @Override
    public int getInt(String key) {
        int index = cursor.getColumnIndex(key);
        return cursor.getInt(index);
    }

}
