package com.lweynant.wastecalendar.ui;

import android.graphics.drawable.Drawable;

import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.provider.ICursorContent;

public interface IViewHolder {

    void setTextAndDefaultImage(ICursorContent content, IRelativeDateFormatter formatter);

    void setAndShowImage(Drawable image);

    boolean isForId(int id);

    int getImageResource();

}
