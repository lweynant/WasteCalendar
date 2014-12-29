package com.lweynant.wastecalendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IResources;

public class Resources implements IResources, ILocalizer {

    private final Context context;

    public Resources(Context context) {
        this.context = context;
    }

    @Override
    public String string(int id) {
        return context.getResources().getString(id);
    }

    @Override
    public Bitmap bitmap(int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

}
