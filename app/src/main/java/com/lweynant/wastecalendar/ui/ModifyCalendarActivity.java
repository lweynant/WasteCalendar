package com.lweynant.wastecalendar.ui;

import android.app.Fragment;

public class ModifyCalendarActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new ModifyCalendarFragment();
    }

}
