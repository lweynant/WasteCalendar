package com.lweynant.wastecalendar.model;

public interface IRelativeDateFormatter {

	public abstract String getRelativeTimeString(IDate when);

	public abstract void reset(IDate ref);

}