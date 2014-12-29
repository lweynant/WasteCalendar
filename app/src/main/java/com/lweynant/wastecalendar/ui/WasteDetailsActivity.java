package com.lweynant.wastecalendar.ui;

import android.app.Fragment;

import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public class WasteDetailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        Resources localizer = new Resources(this);
        WasteEventKeyValues wasteEventKeyValues = new WasteEventKeyValues(new WasteEventFactory(localizer));
        IntentKeyValues intentReader = new IntentKeyValues(getIntent());
        IWasteEvent event = wasteEventKeyValues.readFrom(intentReader);
        return WasteDetailsFragment.newInstance(event, localizer);
    }

}
