package com.lweynant.wastecalendar.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date implements IDate  {

	private final int year;
	private final int dayOfMonth;
	private final int month;

	public Date(int year, int month, int dayOfMonth) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}

	


	@Override
	public boolean equals(Object o) {
		if (o instanceof Date)
		  return isSameAs((Date)o); 
		else			
		 return false;
	}


	@Override
	public boolean isSameAs(IDate other) {
		return other !=null &&(  sameDayOfMonth(other) && sameMonth(other) && sameYear(other));
	}

	private boolean sameYear(IDate other) {
		return year == other.year();
	}

	private boolean sameMonth(IDate other) {
		return month == other.month();
	}

	private boolean sameDayOfMonth(IDate other) {
		return dayOfMonth == other.dayOfMonth();
	}

	@Override
	public int year() {
		return year;
	}

	@Override
	public int month() {
		return month;
	}

	@Override
	public int dayOfMonth() {
		return dayOfMonth;
	}

	@Override
	public boolean isYesterdayFor(IDate day) {
		IDate d = dayBefore(day);
		return isSameAs(d);

	}

	@Override
	public IDate dayBefore() {
		return dayBefore(this);
	}

	@Override
	public IDate dayAfter(){
		return dayAfter(this);
	}
	static private IDate dayBefore(IDate day) {
		Calendar c = day.calendar();
		c.add(Calendar.DAY_OF_MONTH, -1);
		return day(c);
	}

	@Override
	public boolean isTomorrowFor(IDate when) {
		IDate d = dayAfter(when);
		return isSameAs(d);
	}

	static private IDate dayAfter(IDate today) {
		Calendar c = today.calendar();
		c.add(Calendar.DAY_OF_MONTH, +1);
		return day(c);
	}

	static private Date day(Calendar c) {
		return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public boolean isDayAfterTomorrowFor(IDate when) {
		IDate d = dayAfter(dayAfter(when));
		return isSameAs(d);
	}

	@Override
	public boolean before(IDate when) {
		return calendar().before(when.calendar());
	}

	@Override
	public boolean after(IDate when) {
		return calendar().after(when.calendar());
	}

	@Override
	public Calendar calendar() {
		GregorianCalendar c = new GregorianCalendar();
		c.clear();
		c.set(year(), month(), dayOfMonth());
		return c;
	}

	public static IDate today() {
		Calendar c = Calendar.getInstance();
		return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}




	@Override
	public int compareTo(IDate another) {
		if (this.before(another))return -1;
		else if (this.after(another)) return 1;
		return 0;
	}



}
