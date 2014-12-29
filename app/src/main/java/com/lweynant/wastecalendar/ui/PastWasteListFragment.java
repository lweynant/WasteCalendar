package com.lweynant.wastecalendar.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.CollectionDateFormatter;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.provider.CursorReader;
import com.lweynant.wastecalendar.provider.ICursorContent;
import com.lweynant.wastecalendar.provider.SQLBuilder;
import com.lweynant.wastecalendar.provider.WasteEventFactory;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class PastWasteListFragment extends WasteListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * required interface for hosting activities
     */
    public interface Callbacks {
        void onWasteEventClicked(IWasteEvent wasteEvent);
    }

    private Callbacks callbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }


    private static final String TAG = "AllWasteListFragment";

    private class ViewHolder implements IViewHolder {
        private TextView title;
        private TextView date;
        private ImageView icon;
        private int imageResource;
        private int id;
        private DrawableFactory factory;
        private CheckBox collected;

        public ViewHolder(View view, DrawableFactory factory) {
            icon = (ImageView) view.findViewById(R.id.pastwaste_list_item_image);
            title = (TextView) view.findViewById(R.id.pastwaste_list_item_title);
            date = (TextView) view.findViewById(R.id.pastwaste_list_item_date);
            collected = (CheckBox) view.findViewById(R.id.pastwaste_list_item_collected);
            this.id = -1;
            icon.setVisibility(View.INVISIBLE);
            this.factory = factory;
        }

        @Override
        public void setTextAndDefaultImage(final ICursorContent content, IRelativeDateFormatter formatter) {
            final IWasteEvent event = content.getWasteEvent();
            date.setText(formatter.getRelativeTimeString(event.collectionDate()));
            collected.setChecked(event.isCollected());
            imageResource = event.imageResource();
            this.id = content.getId();
            icon.setImageDrawable(factory.getDrawable(R.drawable.ic_menu_delete));
            title.setText(event.type().value());
        }

        @Override
        public void setAndShowImage(Drawable result) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(result);
        }

        @Override
        public boolean isForId(int id) {
            return this.id == id;
        }

        @Override
        public int getImageResource() {
            return imageResource;
        }
    }

    private class WasteCursorAdapter extends SimpleCursorAdapter {

        public WasteCursorAdapter(Context context) {
            super(context, 0, null,
                    new String[]{WasteEventTableMetaData.WASTE_EVENT_TYPE},
                    new int[]{R.id.pastwaste_list_item}, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Log.d(TAG, "newView");
            View view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.list_item_pastwaste, null);
            view.setTag(new ViewHolder(view, drawableFactory));
            return view;
        }
    }

    private DrawableFactory drawableFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        drawableFactory = new DrawableFactory(getResources());
        getLoaderManager().initLoader(0, null, this);
        WasteCursorAdapter cursorAdapter = new WasteCursorAdapter(getActivity());
        ILocalizer localizer = new Resources(getActivity());
        cursorAdapter.setViewBinder(new WasteViewBinder(localizer,
                drawableFactory, new CollectionDateFormatter(Date.today(), localizer)));
        setListAdapter(cursorAdapter);
    }

    @Override
    protected void reloadData() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor item = (Cursor) l.getAdapter().getItem(position);
        WasteEventFactory fac = new WasteEventFactory(new Resources(getActivity()));

        IWasteEvent wasteEvent = new CursorReader(fac).read(item).getWasteEvent();
        Log.d(TAG, "onListItemClick " + wasteEvent.toString());
        callbacks.onWasteEventClicked(wasteEvent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        queryDate = Date.today();
        SQLBuilder builder = new SQLBuilder();
        builder.queryUntil(queryDate);
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Log.d(TAG, "created query: " + builder.toString());
        return new CursorLoader(getActivity(),
                WasteProviderMetaData.CONTENT_URI,
                WasteEventTableMetaData.fullProjection, builder.selection(),
                builder.selectionArgs(), WasteEventTableMetaData.SORT_ORDER_DESC_DATE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }


}
