package com.lweynant.wastecalendar.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ModifyCalendarActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new ModifyCalendarFragment();
    }

}
