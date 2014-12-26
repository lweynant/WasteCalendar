package com.lweynant.wastecalendar.provider;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class SQLBuilder {


	private String selection;
	private String[] selectionArgs;

	public void queryFrom(IDate date) {
		selection = String.format("%s > ? OR ( (%s = ?  AND %s > ?) OR (%s = ? AND %s = ?  AND %s >= ?))", 
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_MONTH,
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_MONTH,
				WasteEventTableMetaData.WASTE_EVENT_DAY
				);
		selectionArgs = new String[]{
				Integer.toString(date.year()),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
		
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("\nSelection: \n");
		b.append(selection);
		b.append("\nArguments: ");
		for (int i = 0; i < selectionArgs.length; i++) {
			b.append("\n\t");
			b.append(selectionArgs[i]);
		}
		return b.toString();
	}

	public String selection() {
		return selection;
	}

	public String[] selectionArgs() {
		return selectionArgs;
	}

	public void queryUntil(IDate date) {
		selection = String.format("%s < ? OR ( (%s = ?  AND %s < ?) OR (%s = ? AND %s = ?  AND %s <= ?))", 
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_MONTH,
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_MONTH,
				WasteEventTableMetaData.WASTE_EVENT_DAY
				);
		selectionArgs = new String[]{
				Integer.toString(date.year()),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
	}

	public void query(String string, Date date) {
		selection = String.format("%s = ? AND %s = ? AND %s = ? AND %s =?", 
				WasteEventTableMetaData.WASTE_EVENT_TYPE,
				WasteEventTableMetaData.WASTE_EVENT_YEAR,
				WasteEventTableMetaData.WASTE_EVENT_MONTH,
				WasteEventTableMetaData.WASTE_EVENT_DAY
				);
		selectionArgs = new String[]{
				string,
				Integer.toString(date.year()),
				Integer.toString(date.month()),
				Integer.toString(date.dayOfMonth())
		};
	}

}
