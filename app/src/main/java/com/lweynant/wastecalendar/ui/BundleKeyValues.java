package com.lweynant.wastecalendar.ui;

import android.os.Bundle;

import com.lweynant.wastecalendar.model.IKeyValuesReader;
import com.lweynant.wastecalendar.model.IKeyValuesWriter;

public class BundleKeyValues implements IKeyValuesReader, IKeyValuesWriter {

    private Bundle bundle;

    public BundleKeyValues(Bundle b) {
        bundle = b;
    }

    @Override
    public void put(String key, String value) {
        bundle.putString(key, value);
    }

    @Override
    public void put(String key, int value) {
        bundle.putInt(key, value);
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public int getInt(String key) {
        return bundle.getInt(key);
    }

    public Bundle bundle() {
        return bundle;
    }
}
