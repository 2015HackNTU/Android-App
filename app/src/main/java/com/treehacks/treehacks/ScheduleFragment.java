package com.treehacks.treehacks;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Eddie on 1/20/2015.
 */
public class ScheduleFragment extends Fragment implements WeekView.EventClickListener, WeekView.MonthChangeListener, WeekView.EventLongPressListener {
	WeekView weekView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		// Initialize schedule view
		// Get a reference for the week view in the layout.
		weekView = (WeekView) rootView.findViewById(R.id.week_view);

		// TODO: set date

		// Set an action when any event is clicked.
		weekView.setOnEventClickListener(this);

		// The week view has infinite scrolling horizontally. We have to provide the events of a
		// month every time the month changes on the week view.
		weekView.setMonthChangeListener(this);

		// Set long press listener for events.
		weekView.setEventLongPressListener(this);
		return rootView;
	}

	@Override
	public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {

	}

	@Override
	public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {

	}

	@Override
	public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

		// Populate the week view with some events.
		List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 3);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		Calendar endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR, 1);
		endTime.set(Calendar.MONTH, newMonth-1);
		WeekViewEvent event = new WeekViewEvent(1, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_01));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 3);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.set(Calendar.HOUR_OF_DAY, 4);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.MONTH, newMonth-1);
		event = new WeekViewEvent(10, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_02));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 4);
		startTime.set(Calendar.MINUTE, 20);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.set(Calendar.HOUR_OF_DAY, 5);
		endTime.set(Calendar.MINUTE, 0);
		event = new WeekViewEvent(10, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_03));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 5);
		startTime.set(Calendar.MINUTE, 30);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR_OF_DAY, 2);
		endTime.set(Calendar.MONTH, newMonth-1);
		event = new WeekViewEvent(2, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_02));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 5);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		startTime.add(Calendar.DATE, 1);
		endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR_OF_DAY, 3);
		endTime.set(Calendar.MONTH, newMonth - 1);
		event = new WeekViewEvent(3, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_03));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.DAY_OF_MONTH, 15);
		startTime.set(Calendar.HOUR_OF_DAY, 3);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR_OF_DAY, 3);
		event = new WeekViewEvent(4, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_04));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		startTime.set(Calendar.HOUR_OF_DAY, 3);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR_OF_DAY, 3);
		event = new WeekViewEvent(5, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_01));
		events.add(event);

		startTime = Calendar.getInstance();
		startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		startTime.set(Calendar.HOUR_OF_DAY, 15);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.MONTH, newMonth-1);
		startTime.set(Calendar.YEAR, newYear);
		endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR_OF_DAY, 3);
		event = new WeekViewEvent(5, "test", startTime, endTime);
		event.setColor(getResources().getColor(R.color.event_color_02));
		events.add(event);

		return events;
//		// Get scheduled events
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
//		query.findInBackground(new FindCallback<ParseObject>() {
//			@Override
//			public void done(List<ParseObject> parseObjects, ParseException e) {
//				if (e == null) {
//					Toast.makeText(getActivity(), "Schedule items found!", Toast.LENGTH_SHORT).show();
//					for (ParseObject object : parseObjects) {
//						Toast.makeText(getActivity(), object.getString("eventName"), Toast.LENGTH_SHORT).show();
//					}
//				} else {
//					// We done fucked up
//				}
//			}
//		});
//		return new ArrayList<WeekViewEvent>();
	}
}
