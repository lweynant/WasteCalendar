package com.lweynant.wastecalendar.provider;

import android.content.UriMatcher;
import android.net.Uri;

public class WasteProviderUriMatcher {
    /**
     * A UriMatcher instance
     */
    private static UriMatcher sUriMatcher;
    public static final int DIR = 1;
    public static final int ITEM = 2;

    private int lastMatched = UriMatcher.NO_MATCH;

    public WasteProviderUriMatcher() {
        this(new UriMatcher(UriMatcher.NO_MATCH));
    }

    public WasteProviderUriMatcher(UriMatcher matcher) {
        sUriMatcher = matcher;
        initMatcher();
    }

    private void initMatcher() {
        // Add a pattern that routes URIs terminated with "table name" to a DIR operation
        sUriMatcher.addURI(WasteProviderMetaData.AUTHORITY, WasteProviderMetaData.TABLE_NAME, DIR);

        // Add a pattern that routes URIs terminated with "table name" plus an integer
        // to an item operation
        sUriMatcher.addURI(WasteProviderMetaData.AUTHORITY, WasteProviderMetaData.TABLE_NAME + "/#", ITEM);
    }

    public void match(Uri uri) {
        lastMatched = sUriMatcher.match(uri);
    }

    public boolean isDir() {
        return lastMatched == DIR;
    }

    public boolean isItem() {
        return lastMatched == ITEM;
    }

    public String type() {
        if (isDir()) return WasteProviderMetaData.CONTENT_TYPE;
        else if (isItem()) return WasteProviderMetaData.CONTENT_TYPE_ITEM;
        else throw new IllegalArgumentException("Unknown Uri");
    }

}
