package com.lweynant.wastecalendar.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lweynant.wastecalendar.DateUtil;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.provider.LocalizedType;
import com.lweynant.wastecalendar.provider.WasteContentResolver;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public class ModifyCalendarFragment extends Fragment
		implements
			OnDateChangeListener,
			IWastePickerListener {


	private static final String TAG = "ModifyCalendarFragment";
	private IDate currentDay = null;
	private HashMap<LocalizedType, WastePickerSummaryBuilder> views = new HashMap<LocalizedType, WastePickerSummaryBuilder>();
	private IWasteEventFactory factory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView called");
		ILocalizer localizer = new Resources(getActivity());
		factory = new WasteEventFactory(localizer);
		LinearLayout v = (LinearLayout) inflater.inflate(
				R.layout.modify_calendar, container, false);
		CalendarView cv = (CalendarView) v.findViewById(R.id.calendarView1);
		cv.setFirstDayOfWeek(Calendar.MONDAY);

		currentDay = firstDayOfThisMonth();
		cv.setDate(currentDay.calendar().getTimeInMillis());
		views.clear();

		for (LocalizedType type : factory.getPossibleTypes()) {
			IWasteEvent event = factory.createWasteEvent(type, currentDay);
			View item = inflater.inflate(R.layout.modify_waste_item, null);
			ImageView i = (ImageView) item
					.findViewById(R.id.modify_waste_item_image);
			i.setImageResource(event.imageResource());
			final TextView t = (TextView) item
					.findViewById(R.id.modify_waste_item_dates);
			t.setText("");
			v.addView(item);
			views.put(type, new WastePickerSummaryBuilder(new IDisplay() {

				@Override
				public void diplay(String text) {
					t.setText(text);
				}
			}));
		}
		cv.setOnDateChangeListener(this);
		cv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick !!!");
				
			}
		});
		cv.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "ongtouch !!!");
				return false;
			}
		});
		return v;
	}

	private Date firstDayOfThisMonth() {
		IDate today = Date.today();
		Date firstDayOfThisMonth = new Date(today.year(), today.month(), 1);
		return firstDayOfThisMonth;
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {

		Date day = new Date(year, month, dayOfMonth);
		if (day.isSameAs(currentDay)) {
			Log.d(TAG, "onSelectedDayChange, looking at new month: "
					+ (month + 1));
		} else {
			currentDay = day;
			Log.d(TAG, "onSelectedDayChange: " + year + "-" + month + "-"
					+ dayOfMonth);
			onNewDateShown(day);
			FragmentManager fm = getActivity().getFragmentManager();
			Vector<String> selectedTypes = getSelectedTypes();
			WastePickerFragment f = WastePickerFragment.newInstance(day,
					factory.getPossibleTypes(),
					selectedTypes.toArray(new String[selectedTypes.size()]));
			f.setListener(this);
			f.show(fm, "DATE");
		}
	}
	private Vector<String> getSelectedTypes() {
		Vector<String> types = new Vector<String>();
		for (Iterator<LocalizedType> it = views.keySet().iterator(); it
				.hasNext();) {
			LocalizedType type = (LocalizedType) it.next();
			WastePickerSummaryBuilder v = views.get(type);
			if (v.contains(currentDay)) {
				types.add(type.value());
			}
		}
		return types;
	}

	@Override
	public void onResult(LocalizedType type, boolean isChecked) {
		WasteContentResolver resolver = new WasteContentResolver(getActivity());
		WastePickerSummaryBuilder wastePickerSummaryBuilder = views.get(type);
		if (isChecked) {
			if (!wastePickerSummaryBuilder.contains(currentDay)){
				wastePickerSummaryBuilder.add(currentDay);
				Log.d(TAG,
						"inserting " + type + " "
								+ DateUtil.getFormattedDay(currentDay));
				resolver.startInsertEvent(type, currentDay);
			}
		} else {
			if (wastePickerSummaryBuilder.contains(currentDay)) {
				Log.d(TAG,
						"deleting " + type + " "
								+ DateUtil.getFormattedDay(currentDay));
				wastePickerSummaryBuilder.remove(currentDay);
				resolver.startDeleteEvent(type, currentDay);
			}
		}
		wastePickerSummaryBuilder.display();
	}

	public void onNewDateShown(IDate date) {
		for (Iterator<LocalizedType> it = views.keySet().iterator(); it
				.hasNext();) {
			LocalizedType type = (LocalizedType) it.next();
			WastePickerSummaryBuilder v = views.get(type);
			v.switchTo(date.month());
			v.display();
		}
	}

}
