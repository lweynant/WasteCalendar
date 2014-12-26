package com.lweynant.wastecalendar.provider;


public class DatabaseType {

	private String type;

	public DatabaseType(String type){
		this.type = type;
	}
	public String value(){
		return type;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof DatabaseType){
			return ((DatabaseType)o).value().equals(type);
		}
		else return false;
	}
	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
}
