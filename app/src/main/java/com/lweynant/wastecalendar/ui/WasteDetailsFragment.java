package com.lweynant.wastecalendar.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.CollectionDateFormatter;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.WasteContentResolver;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public class WasteDetailsFragment extends Fragment implements OnCheckedChangeListener {
    private IWasteEvent wasteEvent;

    public static Fragment newInstance(IWasteEvent event, ILocalizer localizer) {
        WasteDetailsFragment fragment = new WasteDetailsFragment();
        final BundleKeyValues bundleKeyValues = new BundleKeyValues(new Bundle());

        WasteEventKeyValues writer = new WasteEventKeyValues(new WasteEventFactory(localizer));
        writer.writeTo(event, bundleKeyValues);

        fragment.setArguments(bundleKeyValues.bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WasteEventKeyValues reader = new WasteEventKeyValues(new WasteEventFactory(new Resources(getActivity())));
        BundleKeyValues bundle = new BundleKeyValues(getArguments());
        wasteEvent = reader.readFrom(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.waste_detail, container, false);
        TextView typeView = (TextView) v.findViewById(R.id.waste_details_type);
        typeView.setText(wasteEvent.type().value());
        CollectionDateFormatter formatter = new CollectionDateFormatter(Date.today(), new Resources(getActivity()));
        TextView collectionDateView = (TextView) v.findViewById(R.id.waste_details_collection_date);
        collectionDateView.setText(formatter.getRelativeTimeString(wasteEvent.collectionDate()));
        TextView takeOutDateView = (TextView) v.findViewById(R.id.waste_details_take_out_date);
        takeOutDateView.setText(formatter.getRelativeTimeString(wasteEvent.takeOutDate()));

        ImageView imageView = (ImageView) v.findViewById(R.id.waste_details_image);
        imageView.setImageResource(wasteEvent.imageResource());
        CheckBox collectedView = (CheckBox) v.findViewById(R.id.waste_details_collected);
        collectedView.setChecked(wasteEvent.isCollected());
        if (wasteEvent.collectionDate().after(tomorrow())) {
            collectedView.setEnabled(false);
        }

        collectedView.setOnCheckedChangeListener(this);
        return v;
    }

    private IDate tomorrow() {
        return Date.today().dayAfter();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        WasteContentResolver resolver = new WasteContentResolver(getActivity());
        resolver.startUpdateCollectedFieldForEvent(wasteEvent.type(), wasteEvent.collectionDate(), isChecked);

    }


}
