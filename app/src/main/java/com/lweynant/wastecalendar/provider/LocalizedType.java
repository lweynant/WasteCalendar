package com.lweynant.wastecalendar.provider;

public class LocalizedType {
	final private String type;
	public LocalizedType(String type){
    	this.type = type;
    }
	public String value() {
		return type;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof LocalizedType){
			return ((LocalizedType)o).value().equals(type);
		}
		else return false;
	}
	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
}
