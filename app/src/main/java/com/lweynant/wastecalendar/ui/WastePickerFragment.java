package com.lweynant.wastecalendar.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.CollectionDateFormatter;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.provider.LocalizedType;

import java.util.HashMap;

public class WastePickerFragment extends DialogFragment {
    private static final String TAG = "DialogFragment";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY_OF_MONTH = "day of month";
    private static final String TYPES = "types";
    private static final String SELECTED_TYPES = "selected_types";
    final private HashMap<String, CheckBox> checkBoxes = new HashMap<String, CheckBox>();
    private Date date;
    private IWastePickerListener listener;
    private String[] types;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.waste_picker, null);
        types = args.getStringArray(TYPES);
        String[] selectedTypes = args.getStringArray(SELECTED_TYPES);
        for (String type : types) {
            CheckBox check = new CheckBox(getActivity());
            check.setText(type);
            check.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            view.addView(check);
            checkBoxes.put(type, check);
            if (contains(selectedTypes, type)) {
                check.setChecked(true);
            }
        }
        date = new Date(args.getInt(YEAR), args.getInt(MONTH), args.getInt(DAY_OF_MONTH));
        CollectionDateFormatter formatter = new CollectionDateFormatter(Date.today(), new Resources(getActivity()));
        return new AlertDialog.Builder(getActivity())
                .setTitle("Events for " + formatter.date(date))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "ok is clicked, adding waste-events");
                        for (String type : types) {
                            if (checkBoxes.containsKey(type)) {
                                LocalizedType localizedType = new LocalizedType(type);
                                CheckBox box = checkBoxes.get(type);
                                if (listener != null) {
                                    listener.onResult(localizedType, box.isChecked());
                                }
                            }
                        }

                    }
                })
                .setView(view)
                .create();
    }

    private boolean contains(String[] selectedTypes, String type) {
        boolean contains = false;
        for (String string : selectedTypes) {
            if (string.equals(type)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static WastePickerFragment newInstance(IDate day, LocalizedType[] types, String[] selectedTypes) {
        Bundle args = new Bundle();
        args.putInt(YEAR, day.year());
        args.putInt(MONTH, day.month());
        args.putInt(DAY_OF_MONTH, day.dayOfMonth());
        String[] typeStrings = new String[types.length];
        for (int i = 0; i < typeStrings.length; i++) {
            typeStrings[i] = types[i].value();
        }
        args.putStringArray(TYPES, typeStrings);
        args.putStringArray(SELECTED_TYPES, selectedTypes);

        WastePickerFragment f = new WastePickerFragment();
        f.setArguments(args);
        return f;
    }

    public void setListener(IWastePickerListener listener) {
        this.listener = listener;
    }

}
