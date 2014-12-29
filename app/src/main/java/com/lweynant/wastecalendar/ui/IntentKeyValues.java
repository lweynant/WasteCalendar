package com.lweynant.wastecalendar.ui;

import android.content.Intent;

import com.lweynant.wastecalendar.model.IKeyValuesReader;
import com.lweynant.wastecalendar.model.IKeyValuesWriter;

public class IntentKeyValues implements IKeyValuesReader, IKeyValuesWriter {

    private Intent intent;

    public IntentKeyValues(Intent i) {
        intent = i;
    }

    @Override
    public void put(String key, String value) {
        intent.putExtra(key, value);
    }

    @Override
    public void put(String key, int value) {
        intent.putExtra(key, value);
    }

    @Override
    public String getString(String key) {
        return intent.getStringExtra(key);
    }

    @Override
    public int getInt(String key) {
        return intent.getIntExtra(key, 0);
    }

    public Intent intent() {
        return intent;
    }

}
