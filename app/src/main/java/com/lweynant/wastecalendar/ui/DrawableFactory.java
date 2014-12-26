package com.lweynant.wastecalendar.ui;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class DrawableFactory {
	public HashMap<Integer, Drawable> cache = new HashMap<Integer, Drawable>();
	private Resources resources;
	public DrawableFactory(Resources resources) {
		this.resources = resources;
	}
	
	public Drawable getDrawable(int imageResource) {
		Integer key = Integer.valueOf(imageResource);
		Drawable d = cache.get(key);
		if (d == null) {
			d = resources.getDrawable(imageResource);
			cache.put(key, d);
		}
		return d;
	}

}