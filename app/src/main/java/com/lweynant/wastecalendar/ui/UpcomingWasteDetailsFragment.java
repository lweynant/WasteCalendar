package com.lweynant.wastecalendar.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

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

public class UpcomingWasteDetailsFragment extends Fragment implements OnCheckedChangeListener {
    private IWasteEvent wasteEvent;

    public static Fragment newInstance(IWasteEvent event, ILocalizer localizer) {
        UpcomingWasteDetailsFragment fragment = new UpcomingWasteDetailsFragment();
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


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
        if (fragment == null){
            fragment = PastWasteListFragment.newInstance(wasteEvent, new Resources(getActivity()));
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upcoming_waste_detail, container, false);
        ILocalizer l = new Resources(this.getActivity());
        String collection = l.string(R.string.collection_date);
        CollectionDateFormatter formatter = new CollectionDateFormatter(Date.today(), new Resources(getActivity()));
        collection += " " + formatter.getRelativeTimeString(wasteEvent.collectionDate());
        TextView collectionDateView = (TextView) v.findViewById(R.id.upcoming_waste_details_collection_date);
        collectionDateView.setText(collection);

        ImageView imageView = (ImageView) v.findViewById(R.id.upcoming_waste_details_image);
        DrawableTransformer transformer = new DrawableTransformer(getResources());
        imageView.setImageDrawable(transformer.toCircle(wasteEvent.imageResource()));
        CheckBox takenOutView = (CheckBox) v.findViewById(R.id.upcoming_waste_details_collected);
        takenOutView.setChecked(wasteEvent.isCollected());
        takenOutView.setEnabled(wasteEvent.takeOutDate().isSameAs(today()));
        takenOutView.setOnCheckedChangeListener(this);

        ActionBar a = getActivity().getActionBar();
        a.setTitle(wasteEvent.type().value());
        //a.setIcon(transformer.toCircle(wasteEvent.imageResource()));
        String subTitle = l.string(R.string.take_out);
        subTitle += " " + formatter.getRelativeTimeString(wasteEvent.takeOutDate());
        a.setSubtitle(subTitle);
        return v;
    }

    private IDate today() {
        return Date.today();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        WasteContentResolver resolver = new WasteContentResolver(getActivity());
        resolver.startUpdateCollectedFieldForEvent(wasteEvent.type(), wasteEvent.collectionDate(), isChecked);

    }


}
