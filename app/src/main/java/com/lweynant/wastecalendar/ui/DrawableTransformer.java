package com.lweynant.wastecalendar.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class DrawableTransformer implements IDrawableTransformer {
    private final Resources resources;

    DrawableTransformer(Resources resources){
        this.resources = resources;
    }
    @Override
    public Drawable toCircle(int resourceId) {
        Bitmap bm = BitmapFactory.decodeResource(resources, resourceId);
        return new RoundImage(bm);
    }
}
