package com.lweynant.wastecalendar.provider;


import android.database.Cursor;

import com.lweynant.wastecalendar.model.IDate;

public interface IContentResolver {

    Cursor query(String[] projection, String selection, String[] selectionArgs);

    void startInsert(DatabaseType databaseType, IDate date);

    void startDelete(String selection, String[] selectionArgs);

    void startUpdate(DatabaseType databaseType, IDate date, boolean collected, String selection, String[] selectionArgs);
}
