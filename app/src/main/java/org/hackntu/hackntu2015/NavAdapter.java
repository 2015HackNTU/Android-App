package org.hackntu.hackntu2015;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Eddie on 2/10/2015.
 */
public class NavAdapter extends ArrayAdapter<String> {
	Context context;
	int selected;

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
			viewHolder.icon.setImageResource(GetIcon(navItemTitle, position == selected));
		}
		return convertView;
	}

	public void activate(int idx) {
		selected = idx;
		notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView title;
		ImageView icon;
	}

	private static int GetIcon(String action, boolean active) {
		if (active) {
			switch (action) {
				case "Announcements":
					return R.drawable.announcement;
				case "FAQ":
					return R.drawable.faq;
				case "Schedule":
					return R.drawable.schedule;
				case "Maps":
					return R.drawable.map;
                case "Award":
                    return R.drawable.award;
				default:
					return -1;
			}
		}
		else {
			switch (action) {
				case "Announcements":
					return R.drawable.tabbar_hacks;
				case "FAQ":
					return R.drawable.tabbar_faq;
				case "Schedule":
					return R.drawable.tabbar_schedule;
				case "Maps":
					return R.drawable.tabbar_map;
				case "Report":
					return R.drawable.tabbar_report;
                case "Award":
                    return R.drawable.tabbar_award;
				default:
					return -1;
			}
		}
	}
}
