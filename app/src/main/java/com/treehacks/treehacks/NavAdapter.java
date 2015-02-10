package com.treehacks.treehacks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eddie on 2/10/2015.
 */
public class NavAdapter extends ArrayAdapter<String> {
	Context context;

	public NavAdapter(Context context) {
		super(context, R.layout.nav_drawer_item, context.getResources().getStringArray(R.array.page_names));
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		// If we have to create the view from scratch
		if (convertView == null) {

			// Inflate view
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.nav_drawer_item, parent, false);

			// Setup viewholder
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.nav_item_title);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.nav_item_icon);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String navItemTitle = getItem(position);
		if (navItemTitle != null) {
			viewHolder.title.setText(navItemTitle);
			viewHolder.icon.setImageResource(GetIcon(navItemTitle));
		}
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		ImageView icon;
	}

	private static int GetIcon(String action) {
		switch (action) {
			case "Schedule":
				return R.drawable.tabbar_schedule;
			case "Announcements":
				return R.drawable.tabbar_hacks; // TODO: replace?
			case "FAQ":
				return R.drawable.tabbar_faq;
			case "Report":
				return R.drawable.tabbar_report;
			default:
				return -1;
		}
	}
}
