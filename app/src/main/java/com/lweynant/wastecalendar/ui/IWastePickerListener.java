package com.lweynant.wastecalendar.ui;

import com.lweynant.wastecalendar.provider.LocalizedType;

public interface IWastePickerListener {

	void onResult(LocalizedType type, boolean checked);


}
