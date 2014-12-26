package com.lweynant.wastecalendar.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public class WasteProviderMetaData {
	
	public static final String AUTHORITY = "com.lweynant.provider.WasteEventProvider";
	
	public static final String DATABASE_NAME = "wastecalendar.db";
	public static final int    DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "wasteevents";
	
	private WasteProviderMetaData(){};
	
	//uri and MIME type definitions
	public static final Uri    CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.lweynant.wasteevent";
	public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.lweynant.wasteevent";
	//inner class describing the WasteEventTable
	public static final class WasteEventTableMetaData implements BaseColumns 
	{
		public static int _ID_PATH_POSITION = 1;

		private WasteEventTableMetaData() {};

		
		
		//columns
		public static final String WASTE_EVENT_TYPE = "type";
		public static final String WASTE_EVENT_YEAR = "year";
		public static final String WASTE_EVENT_MONTH = "month";
		public static final String WASTE_EVENT_DAY = "day";
		public static final String WASTE_EVENT_COLLECTED = "collected";
		public static final String[] fullProjection ={
			_ID, WASTE_EVENT_TYPE, WASTE_EVENT_YEAR, WASTE_EVENT_MONTH, WASTE_EVENT_DAY,  WASTE_EVENT_COLLECTED
		};
		
		public static final String SORT_ORDER_ASC_DATE = WASTE_EVENT_YEAR + " ASC, "
                + WASTE_EVENT_MONTH + " ASC, "
                + WASTE_EVENT_DAY + " ASC";
		public static final String SORT_ORDER_DESC_DATE = WASTE_EVENT_YEAR + " DESC, "
                + WASTE_EVENT_MONTH + " DESC, "
                + WASTE_EVENT_DAY + " DESC";
		
		
	}
	

}
