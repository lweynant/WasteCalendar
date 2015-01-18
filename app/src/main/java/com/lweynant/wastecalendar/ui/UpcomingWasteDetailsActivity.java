package com.lweynant.wastecalendar.ui;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public class UpcomingWasteDetailsActivity extends SingleFragmentActivity implements PastWasteListFragment.Callbacks{

    private static final String TAG = UpcomingWasteDetailsActivity.class.getSimpleName();

    @Override
    protected Fragment getFragment() {
        Resources localizer = new Resources(this);
        WasteEventKeyValues wasteEventKeyValues = new WasteEventKeyValues(new WasteEventFactory(localizer));
        IntentKeyValues intentReader = new IntentKeyValues(getIntent());
        IWasteEvent event = wasteEventKeyValues.readFrom(intentReader);

        return UpcomingWasteDetailsFragment.newInstance(event, localizer);
    }

    @Override
    public void onWasteEventClicked(IWasteEvent wasteEvent) {
        Log.d(TAG, "onWasteEventClicked: " + wasteEvent);
        IntentKeyValues intentKeyValues = new IntentKeyValues(new Intent(this,WasteDetailsActivity.class));
        WasteEventKeyValues writer = new WasteEventKeyValues(
                new WasteEventFactory(new Resources(this)));
        writer.writeTo(wasteEvent, intentKeyValues);
        startActivity(intentKeyValues.intent());
    }
}
