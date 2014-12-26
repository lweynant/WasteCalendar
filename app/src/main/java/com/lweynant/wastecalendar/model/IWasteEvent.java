package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.provider.LocalizedType;



public interface IWasteEvent extends Comparable<IWasteEvent> {
	abstract public IDate collectionDate();
	abstract public IDate takeOutDate();
	abstract public int imageResource();
	abstract public LocalizedType type();
	abstract public boolean isCollected();
	abstract public void setCollected(boolean collected);
}
