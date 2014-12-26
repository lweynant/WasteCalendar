package com.lweynant.wastecalendar.provider;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class WasteContentProvider extends ContentProvider {

	private static final String TAG = "WasteEventProvider";
	/**
	 * This class helps open, create, and upgrade the database file. Set to
	 * package visibility for testing purposes.
	 */
	static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context, String name) {
			super(context, name, null,
					WasteProviderMetaData.DATABASE_VERSION);
		}

		/**
		 * 
		 * Creates the underlying database with table name and column names
		 * taken from the NotePad class.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ WasteProviderMetaData.TABLE_NAME
					+ " ("
					+ WasteProviderMetaData.WasteEventTableMetaData._ID
					+ " INTEGER PRIMARY KEY,"
					+ WasteProviderMetaData.WasteEventTableMetaData.WASTE_EVENT_TYPE
					+ " TEXT,"
					+ WasteProviderMetaData.WasteEventTableMetaData.WASTE_EVENT_YEAR
					+ " INTEGER,"
					+ WasteProviderMetaData.WasteEventTableMetaData.WASTE_EVENT_MONTH
					+ " INTEGER,"
					+ WasteProviderMetaData.WasteEventTableMetaData.WASTE_EVENT_DAY
					+ " INTEGER,"
					+ WasteProviderMetaData.WasteEventTableMetaData.WASTE_EVENT_COLLECTED
					+ " INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.wtf(TAG, "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which is not supported!");

			throw new UnsupportedOperationException("Not yet implemented");
		}
	}

	private DatabaseHelper dbHelper;
	private WasteProviderUriMatcher matcher = new WasteProviderUriMatcher();

	public WasteContentProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete uri" + uri + "selection: " + selection);
		validateUriThrowIfNotDir(uri);
		int count = removeFromDatabase(selection, selectionArgs);
        Log.d(TAG, "returned delete count: " + count);
		notifyChange(uri);
        return count;
	}

	private int removeFromDatabase(String selection, String[] selectionArgs) {
		// Opens the database object in "write" mode.
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		int count = db.delete(WasteProviderMetaData.TABLE_NAME,
				selection,
				selectionArgs);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		Log.d(TAG, "getType for uri" + uri);
		matcher.match(uri);
		return matcher.type();
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		Log.d(TAG, "insert uri" + uri + " content values: " + initialValues);

		validateUriThrowIfNotDir(uri);

		WasteContentValues values = new WasteContentValues(initialValues);

		if (!hasEvent(uri, values.getContentValues())){
			Uri insertedWasteEventUri = insertInDatabase(values);
	
			notifyChange(insertedWasteEventUri);
			return insertedWasteEventUri;
		}
		else {
			Log.d(TAG, "the event already exists, not added");
			return uri;
		}
	}

	private boolean hasEvent(Uri uri, ContentValues cv) {
		SQLBuilder sqlBuilder = new SQLBuilder();
		sqlBuilder.query(cv.getAsString(WasteEventTableMetaData.WASTE_EVENT_TYPE), 
				new Date(cv.getAsInteger(WasteEventTableMetaData.WASTE_EVENT_YEAR),
						cv.getAsInteger(WasteEventTableMetaData.WASTE_EVENT_MONTH),
						cv.getAsInteger(WasteEventTableMetaData.WASTE_EVENT_DAY)));
		Cursor c = query(uri, WasteEventTableMetaData.fullProjection,
				sqlBuilder.selection(), sqlBuilder.selectionArgs(), WasteEventTableMetaData.SORT_ORDER_DESC_DATE);
		boolean hasEvent = c.getCount() >0;
		c.close();
		return hasEvent;
	}
	
	private void notifyChange(Uri changedUri) {
		// Notifies observers registered against this provider that the data
		// changed.
		getContext().getContentResolver().notifyChange(changedUri, null);
	}

	private Uri insertInDatabase(WasteContentValues values) {
		// Opens the database object in "write" mode.
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		long rowId = db.insert(WasteProviderMetaData.TABLE_NAME,
				WasteEventTableMetaData.WASTE_EVENT_TYPE,
				values.getContentValues());
		// If the insert succeeded, the row ID exists.
		if (rowId > 0) {
			Uri insertedWasteEventUri = ContentUris.withAppendedId(
					WasteProviderMetaData.CONTENT_URI, rowId);

			return insertedWasteEventUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an
		// exception.
		throw new SQLException("Failed to insert row into "
				+ WasteProviderMetaData.TABLE_NAME);
	}


	private void validateUriThrowIfNotDir(Uri uri) {
		matcher.match(uri);
		if (!matcher.isDir()) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		// Creates a new helper object. Note that the database itself isn't
		// opened until
		// something tries to access it, and it's only created if it doesn't
		// already exist.  
		File dir = getContext().getExternalFilesDir(null);
		String name = new File(dir, WasteProviderMetaData.DATABASE_NAME).getPath();
		Log.d(TAG, "onCreate: file " + name);
		dbHelper = new DatabaseHelper(getContext(), name);
		// Assumes that any failures will be reported by a thrown exception.
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = createWasteEventTableQueryBuilder(uri);

		String orderBy = getSortOrder(sortOrder);

		Cursor c = queryDatabase(uri, projection, selection, selectionArgs,
				queryBuilder, orderBy);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	private Cursor queryDatabase(Uri uri, String[] projection,
			String selection, String[] selectionArgs,
			SQLiteQueryBuilder queryBuilder, String orderBy) {
		// Opens the database object in "read" mode, since no writes need to be
		// done.
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = queryBuilder.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
				);

		return c;
	}

	private String getSortOrder(String sortOrder) {
		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = WasteEventTableMetaData.SORT_ORDER_ASC_DATE;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}
		return orderBy;
	}

	private SQLiteQueryBuilder createWasteEventTableQueryBuilder(Uri uri) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(WasteProviderMetaData.TABLE_NAME);

		/**
		 * Adjust the "where" clause based on URI pattern-matching.
		 */
		matcher.match(uri);
		if (matcher.isItem()) {
			queryBuilder.appendWhere(WasteEventTableMetaData._ID
					+ "="
					+
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							WasteEventTableMetaData._ID_PATH_POSITION));

		}
		return queryBuilder;
	}

	@Override
	public int update(Uri uri, ContentValues initialValues, String selection,
			String[] selectionArgs) {
		Log.d(TAG, "update uri" + uri + " content values: " + initialValues);

		validateUriThrowIfNotDir(uri);

		WasteContentValues values = new WasteContentValues(initialValues);

		int count = updateInDatabase(values, selection, selectionArgs);

		notifyChange(uri);

		return count;
	}

	private int updateInDatabase(WasteContentValues values, String selection,
			String[] selectionArgs) {
		// Opens the database object in "write" mode.
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		int count = db.update(WasteProviderMetaData.TABLE_NAME,
				values.getContentValues(),
				selection,
				selectionArgs);
		return count;
	}
}
