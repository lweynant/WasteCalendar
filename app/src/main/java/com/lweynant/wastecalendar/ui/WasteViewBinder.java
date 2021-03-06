package com.lweynant.wastecalendar.ui;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.provider.CursorReader;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

class WasteViewBinder implements ViewBinder {

    private static final String TAG = "ViewBinder";
    final private DrawableFactory factory;
    final private IRelativeDateFormatter dateFormatter;
    final private ILocalizer localizer;

    public WasteViewBinder(ILocalizer localizer, DrawableFactory factory, IRelativeDateFormatter dateFormatter) {
        this.factory = factory;
        this.dateFormatter = dateFormatter;
        this.localizer = localizer;
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        CursorReader wasteCursorReader = new CursorReader(new WasteEventFactory(localizer));
        wasteCursorReader.read(cursor);
        final int id = wasteCursorReader.getId();
        IViewHolder v = (IViewHolder) view.getTag();
        dateFormatter.reset(Date.today());
        v.setTextAndDefaultImage(wasteCursorReader, dateFormatter);
        new AsyncTask<IViewHolder, Void, Drawable>() {
            private IViewHolder v;

            @Override
            protected Drawable doInBackground(IViewHolder... params) {
                v = params[0];
                Drawable d = factory.getDrawable(v.getImageResource());
                return d;
            }

            @Override
            protected void onPostExecute(Drawable result) {
                super.onPostExecute(result);
                if (v.isForId(id)) {
                    // If this item hasn't been recycled already,
                    // set and show the image
                    v.setAndShowImage(result);
                }
            }

        }.execute(v);

        return true;
    }

}
