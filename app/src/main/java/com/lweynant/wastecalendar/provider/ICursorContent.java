package com.lweynant.wastecalendar.provider;

import com.lweynant.wastecalendar.model.IWasteEvent;

public interface ICursorContent {

	public abstract IWasteEvent getWasteEvent();

	public abstract int getId();


}