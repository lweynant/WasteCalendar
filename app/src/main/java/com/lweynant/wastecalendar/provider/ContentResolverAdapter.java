package com.lweynant.wastecalendar.provider;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;

import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class ContentResolverAdapter implements IContentResolver {
    private final Context context;
    private final AsyncQueryHandler handler;

    public ContentResolverAdapter(Context context) {
        this.context = context;
        this.handler = new AsyncQueryHandler(context.getContentResolver()) {
        };
    }

    @Override
    public Cursor query(String[] projection, String selection,
                        String[] selectionArgs) {
        return context.getContentResolver().query(
                WasteProviderMetaData.CONTENT_URI, projection, selection,
                selectionArgs, WasteEventTableMetaData.SORT_ORDER_ASC_DATE);
    }

    @Override
    public void startInsert(DatabaseType databaseType, IDate date) {
        WasteContentValues cv = new WasteContentValues(new WasteEventFactory(
                new Resources(context)), databaseType, date);
        handler.startInsert(0, null, WasteProviderMetaData.CONTENT_URI,
                cv.getContentValues());

    }

    @Override
    public void startDelete(String selection, String[] selectionArgs) {
        handler.startDelete(1, null, WasteProviderMetaData.CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public void startUpdate(DatabaseType databaseType, IDate date,
                            boolean collected, String selection, String[] selectionArgs) {
        WasteContentValues cv = new WasteContentValues(new WasteEventFactory(
                new Resources(context)), databaseType, date, collected);
        handler.startUpdate(0, null, WasteProviderMetaData.CONTENT_URI,
                cv.getContentValues(), selection, selectionArgs);

    }
}
