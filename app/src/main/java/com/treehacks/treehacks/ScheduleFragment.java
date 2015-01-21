package com.treehacks.treehacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Eddie on 1/20/2015.
 */
public class ScheduleFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e == null) {
					Toast.makeText(getActivity(), "Schedule items found!", Toast.LENGTH_SHORT).show();
					for (ParseObject object : parseObjects) {
						Toast.makeText(getActivity(), object.getString("eventName"), Toast.LENGTH_SHORT).show();
					}
				} else {
					// We done fucked up
				}
			}
		});
		return rootView;
	}
}
