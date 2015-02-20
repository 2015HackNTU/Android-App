package com.treehacks.treehacks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Eddie on 2/20/2015.
 */
public abstract class ActionBarSpinnerAdapter<T> extends ArrayAdapter<T> {

	public ActionBarSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_dropdown_item);
	}

	private View getViewFromResource(int position, View convertView, ViewGroup parent, int layoutResource) {
		if (convertView == null)
			convertView = LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
		convertView.setBackgroundColor(getContext().getResources().getColor(R.color.treehacks_red));
		((TextView) convertView).setTextColor(getContext().getResources().getColor(R.color.primary_text_default_material_dark));
		T o = getItem(position);
		((TextView) convertView).setText(getObjectName(o));
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
	}

	public abstract String getObjectName(T object);
}
