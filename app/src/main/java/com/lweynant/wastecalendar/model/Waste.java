package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.provider.LocalizedType;



public class Waste implements IWasteEvent {


	final private IDate collectionDate;
	final private IDate outdoorsDate;
	final private LocalizedType type;
	private boolean collected = false;
	final private int imageResource;;

	public Waste(LocalizedType type, int imageId, IDate date) {
		this.type = type;
		imageResource = imageId;
		collectionDate = date;
		outdoorsDate = date.dayBefore();
	}

	@Override
	public int compareTo(IWasteEvent another) {
		IDate thisDay = collectionDate();
		IDate otherDay = another.collectionDate();
		if (thisDay.before(otherDay))return -1;
		else if (thisDay.after(otherDay)) return 1;
		else return 0;
	}

	
	
	@Override
	public IDate collectionDate() {
		return collectionDate;
	}

	@Override
	public IDate takeOutDate(){
		return outdoorsDate;
	}


	@Override
	public LocalizedType type() {
		return type;
	}

	@Override
	public int imageResource() {
		return imageResource;
	}

	
	@Override
	public boolean isCollected() {
		return collected;
	}

	@Override
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(type.value());
		buf.append(": ");
		buf.append(collectionDate.year());
		buf.append("-");
		buf.append(collectionDate.month() + 1);
		buf.append("-");
		buf.append(collectionDate.dayOfMonth());
		return buf.toString();
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof Waste){
			Waste other = (Waste)o;
			if (collectionDate.equals(other.collectionDate())){
				if (type.equals(other.type())){
					if (isCollected() == other.isCollected()){
						return true;
					}
				}
			}
		}
		return super.equals(o);
	}

}
