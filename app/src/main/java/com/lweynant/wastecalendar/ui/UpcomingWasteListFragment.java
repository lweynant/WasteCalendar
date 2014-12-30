package com.lweynant.wastecalendar.ui;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lweynant.wastecalendar.AlarmService;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IPreferences;
import com.lweynant.wastecalendar.model.IRelativeDateFormatter;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.TakeOutDateFormatter;
import com.lweynant.wastecalendar.provider.CursorReader;
import com.lweynant.wastecalendar.provider.ICursorContent;
import com.lweynant.wastecalendar.provider.SQLBuilder;
import com.lweynant.wastecalendar.provider.WasteEventFactory;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class UpcomingWasteListFragment extends WasteListFragment
        implements
        OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "WasteListFragment";
    private DrawableFactory drawableFactory = null;
    private Callbacks callbacks;

    /**
     * required interface for hosting activities
     */
    public interface Callbacks {
        void onWasteEventClicked(IWasteEvent wasteEvent);
    }

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

    private class ViewHolder implements IViewHolder {
        private TextView title;
        private TextView date;
        private ImageView icon;
        private int imageResource;
        private int id;
        private DrawableFactory factory;

        public ViewHolder(View view, DrawableFactory factory) {
            icon = (ImageView) view.findViewById(R.id.waste_list_item_image);
            title = (TextView) view.findViewById(R.id.waste_list_item_title);
            date = (TextView) view.findViewById(R.id.waste_list_item_date);
            this.id = -1;
            icon.setVisibility(View.INVISIBLE);
            this.factory = factory;
        }

        @Override
        public void setTextAndDefaultImage(final ICursorContent content, IRelativeDateFormatter formatter) {
            final IWasteEvent event = content.getWasteEvent();
            date.setText(formatter.getRelativeTimeString(event.takeOutDate()));
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

    public class WasteCursorAdapter extends SimpleCursorAdapter {

        public WasteCursorAdapter(Context context) {
            super(context, 0, null,
                    new String[]{WasteEventTableMetaData.WASTE_EVENT_TYPE},
                    new int[]{R.id.waste_list_item}, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Log.d(TAG, "newView");
            View view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.list_item_waste, null);
            view.setTag(new ViewHolder(view, drawableFactory));
            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        drawableFactory = new DrawableFactory(getResources());
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.prefs, false);
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "setting listener on shared preferences");
        getLoaderManager().initLoader(0, null, this);
        WasteCursorAdapter cursorAdapter = new WasteCursorAdapter(getActivity());

        ILocalizer localizer = new Resources(getActivity());
        cursorAdapter.setViewBinder(new WasteViewBinder(localizer,
                drawableFactory, new TakeOutDateFormatter(Date.today(), localizer)));
        setListAdapter(cursorAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
        drawableFactory = null;

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
        if (IPreferences.ALARM_HOUR.equals(key)) {
            String value = sharedPreferences.getString(IPreferences.ALARM_HOUR,
                    "");
            Log.d(TAG, "key " + key + " changed, resetting alarm to " + value);
            AlarmService.setAlarmRequest(getActivity(), Date.today());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        queryDate = Date.today();
        IDate tomorrowCollectionDate = queryDate.dayAfter();
        SQLBuilder builder = new SQLBuilder();
        builder.queryFrom(tomorrowCollectionDate);
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Log.d(TAG, "created query: " + builder.toString());
        return new CursorLoader(getActivity(),
                WasteProviderMetaData.CONTENT_URI,
                WasteEventTableMetaData.fullProjection, builder.selection(),
                builder.selectionArgs(), WasteEventTableMetaData.SORT_ORDER_ASC_DATE);
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
