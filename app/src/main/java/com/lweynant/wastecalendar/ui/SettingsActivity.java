package com.lweynant.wastecalendar.ui;

import android.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new SettingsFragment();
    }

}
