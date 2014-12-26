package com.lweynant.wastecalendar.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.lweynant.wastecalendar.model.IDate;

public class WastePickerSummaryBuilder {

	private HashMap<Integer, List<IDate>> dates = new HashMap<Integer, List<IDate>>();
	private IDisplay display;
	private int currentMonth;
	public WastePickerSummaryBuilder(IDisplay display) {
		this.display = display;
	}

	public void display() {
		List<IDate> d = dates.get(currentMonth);
		StringBuffer b = new StringBuffer();
		if (d!= null){
			Collections.sort(d);
			for (int i = 0; i < d.size(); i++) {
				if (i!=0){
					b.append(", ");
				}
				b.append(d.get(i).dayOfMonth());
			}
		}
		display.diplay(b.toString());
	}

	public WastePickerSummaryBuilder add(IDate iDate) {
		currentMonth = iDate.month();
		List<IDate> d = dates.get(currentMonth);
		if (d == null) {
			d = new ArrayList<IDate>();
			dates.put(currentMonth, d);
		}
		if (!d.contains(iDate)){
			d.add(iDate);
		}
		return this;
	}

	public WastePickerSummaryBuilder switchTo(int month) {
		currentMonth = month;
		return this;
	}

	public boolean contains(IDate date) {
		List<IDate> d = dates.get(date.month());
		if (d==null) return false;
		else return d.contains(date);
	}

	public void remove(IDate day) {
		currentMonth = day.month();
		List<IDate> d = dates.get(currentMonth);
		if (d != null && d.contains(day)){
			d.remove(day);
		}
	}

}
