package com.lweynant.wastecalendar.model;


public abstract  class RelativeDateFormatter implements IRelativeDateFormatter {

	protected IDate ref;
	final protected ILocalizer localizer;
	final private DateFormatter df;
	public RelativeDateFormatter(IDate ref, ILocalizer localizer) {
		this.ref = ref;
		this.localizer = localizer;
		df = new DateFormatter(localizer);
	}
	public void reset(IDate ref){
		this.ref = ref;
	}
	protected boolean isSameDay(IDate when) {
		return when.isSameAs(ref);
	}
	protected String date(int format, IDate when) {
		String day = capitalizeFirstLetter(df.day(when));
		String month = df.month(when);
		String d = date(format, day, month, when.dayOfMonth());
		return d;
	}
	private String date(int format, String day, String month, int dayOfMonth) {
		return df.format(format, day, month, dayOfMonth);
	}
	private String capitalizeFirstLetter(String day) {
		return Character.toUpperCase(day.charAt(0)) + day.substring(1);
	}

}
