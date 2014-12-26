package com.lweynant.wastecalendar.ui;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lweynant.wastecalendar.DateUtil;
import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.Resources;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.provider.CursorReader;
import com.lweynant.wastecalendar.provider.WasteContentResolver;
import com.lweynant.wastecalendar.provider.WasteEventFactory;

public abstract class WasteListFragment extends ListFragment {

	protected static final String TAG = "WasteListFragment";
	protected IDate queryDate = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				SimpleCursorAdapter adapter;
				switch (item.getItemId()) {
					case R.id.menu_item_delete_waste_event :
						adapter = (SimpleCursorAdapter) getListAdapter();
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								Cursor cursor = (Cursor) adapter.getItem(i);
								CursorReader reader = new CursorReader(new WasteEventFactory(new Resources(getActivity())));
								reader.read(cursor);
								IWasteEvent w = reader.getWasteEvent();
								Log.d(TAG, "delete: " + w.toString());
								WasteContentResolver resolver = new WasteContentResolver(getActivity());
								resolver.startDeleteEvent(w.type(), w.collectionDate());
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default :
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.waste_list_item_context, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {

			}
		});
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		if (!Date.today().isSameAs(queryDate)) {
			Log.d(TAG, "onResume date is: " + DateUtil.getFormattedDay(Date.today()));
			reloadData();
		}
	}
	abstract protected void reloadData();

}
