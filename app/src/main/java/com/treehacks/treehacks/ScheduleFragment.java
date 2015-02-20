package com.treehacks.treehacks;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Eddie on 1/20/2015.
 */
public class ScheduleFragment extends Fragment implements WeekView.EventClickListener, WeekView.MonthChangeListener, WeekView.EventLongPressListener {
	WeekView weekView;
	Spinner viewSpinner;
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

		// Set up spinner adapter
		adapter = new ActionBarSpinnerAdapter<String>(getActivity()) {
			@Override
			public String getObjectName(String object) {
				return object;
			}
		};

		adapter.addAll("One-day view", "Three-day view");

		// Sync scheduled events from cloud
		ParseQuery<ParseObject> cloudQuery = ParseQuery.getQuery("Schedule");
		cloudQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> parseCloudEvents, ParseException e) {
				if (e == null) {
					// Skip if no new updates are found
					// Get newest updatedAt from cloud events
					Date newestChange = new Date(1);
					for (ParseObject o : parseCloudEvents) {
						Date change = o.getUpdatedAt();
						if (change.after(newestChange))
							newestChange = change;
					}
					// Get stored updatedAt
					Date storedChange = new Date(0);
					ParseQuery<ParseObject> getStoredChangeTime = ParseQuery.getQuery("EventChangeTime");
					getStoredChangeTime.fromLocalDatastore();
					ParseObject storedChangeTime;
					try {
						storedChangeTime = getStoredChangeTime.getFirst();
						if (storedChangeTime.has("time"))
							storedChange = storedChangeTime.getDate("time");
					} catch (ParseException e1) {
						storedChangeTime = new ParseObject("EventChangeTime");
						try {
							storedChangeTime.pin();
						} catch (ParseException ignored) {
						}
					}
					if (!newestChange.after(storedChange)) {
						Log.d("Parse", "No newer events found in cloud");
						return;
					}
					Log.d("Parse", "Newer events found in cloud, syncing...");
					// Store newer updatedAt
					storedChangeTime.put("time", newestChange);
					// Update local datastore
					ParseObject.unpinAllInBackground("events", new DeleteCallback() {
						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("events", parseCloudEvents, new SaveCallback() {
								@Override
								public void done(ParseException e) {
									Log.d("Parse", "cache events from cloud SUCCESS");
									if (weekView != null) {
										weekView.notifyDatasetChanged();
									}
								}
							});
						}
					});

				} else {
                    e.printStackTrace();
					// We done fucked up
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		// Initialize schedule view
		// Get a reference for the week view in the layout.
		weekView = (WeekView) rootView.findViewById(R.id.week_view);

		weekView.setOnEventClickListener(this);
		weekView.setMonthChangeListener(this);
		weekView.setEventLongPressListener(this);

        weekView.setNumberOfVisibleDays(1);
        weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));

        Calendar treeHacksStart = Calendar.getInstance();
		treeHacksStart.set(2015, Calendar.FEBRUARY, 20, 18, 0);
		Calendar treeHacksEnd = Calendar.getInstance();
		treeHacksEnd.set(2015, Calendar.FEBRUARY, 22, 11, 0);
		if (Calendar.getInstance().getTimeInMillis() < treeHacksStart.getTimeInMillis()) {
			weekView.goToDate(treeHacksStart);
		}
		else if (Calendar.getInstance().getTimeInMillis() > treeHacksEnd.getTimeInMillis()) {
			weekView.goToDate(treeHacksEnd);
		}
		return rootView;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_schedule, menu);
	    viewSpinner = (Spinner) menu.findItem(R.id.menu_schedule_spinner).getActionView();
	    viewSpinner.setAdapter(adapter);
	    viewSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (adapter.getItem(position)) {
					case "One-day view":
						weekView.setNumberOfVisibleDays(1);
						break;
					case "Three-day view":
						weekView.setNumberOfVisibleDays(3);
						break;
				}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {

		    }
	    });
    }

	@Override
	public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {

	}

	@Override
	public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {

	}

	@Override
	public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
		ArrayList<WeekViewEvent> events = new ArrayList<>();
		// Get scheduled events from cache
		ParseQuery<ParseObject> localQuery = ParseQuery.getQuery("Schedule");
		localQuery.fromLocalDatastore();
		Calendar thisMonth = new GregorianCalendar(newYear, newMonth, 0);
		Calendar nextMonth = new GregorianCalendar(newYear + (newMonth == Calendar.DECEMBER ? 1 : 0), (newMonth + 1) % 12, 0);
		localQuery.whereGreaterThanOrEqualTo("eventTime", thisMonth.getTime());
		localQuery.whereLessThan("eventTime", nextMonth.getTime());
		List<ParseObject> parseEvents;
		try {
			parseEvents = localQuery.find();
			Log.d("Parse", "read events from local db SUCCESS");
		} catch (ParseException e) {
			e.printStackTrace();
			parseEvents = new ArrayList<>();
		}
		// Create WeekViewEvents from ParseObjects
		for (ParseObject parseEvent : parseEvents) {
			String title = parseEvent.getString("eventName");
			Date startDate = parseEvent.getDate("eventTime");
			Date endDate = parseEvent.getDate("endTime");
			String descr = parseEvent.getString("eventDescription");
			Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			startTime.setTime(startDate);
			Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			endTime.setTime(endDate);
			WeekViewEvent scheduleEvent = new WeekViewEvent(0, title, startTime, endTime);

			// Set event color based on event type
			int eventColor;
			String type = parseEvent.getString("type");
			if (type != null)
				switch (type) {
					case "admin":
						eventColor = getResources().getColor(R.color.treehacks_red);
						break;
					case "food":
						eventColor = getResources().getColor(R.color.treehacks_orange);
						break;
					default:
						eventColor = getResources().getColor(R.color.bluegrey_400);
						break;
				}
			else
				eventColor = getResources().getColor(R.color.bluegrey_400);

			scheduleEvent.setColor(eventColor);
			events.add(scheduleEvent);
		}
		return events;
	}
}
