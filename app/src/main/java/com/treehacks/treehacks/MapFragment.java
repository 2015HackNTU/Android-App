package com.treehacks.treehacks;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Fetches maps from parse and displays them in a touch-enabled photo view
 * Created by Eddie on 2/19/2015.
 */
public class MapFragment extends Fragment {
	Spinner mapSpinner;
	ArrayAdapter<ParseObject> adapter;
	ImageView mapView;
	PhotoViewAttacher attacher;
	ProgressBar progressBar; // Useless for now, because I couldn't figure out animations in time

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		// Set up spinner adapter
		adapter = new ActionBarSpinnerAdapter<ParseObject>(getActivity()) {
			@Override
			public String getObjectName(ParseObject object) {
				return object.getString("name");
			}
		};

		// Check cloud for newer maps
		ParseQuery<ParseObject> cloudQuery = ParseQuery.getQuery("Maps");
		cloudQuery.orderByDescending("updatedAt");
		cloudQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> parseCloudMaps, ParseException e) {
				if (e == null) {
					// Skip if no new updates are found
					// Get newest updatedAt from cloud maps
					Date newestChange;
					if (!parseCloudMaps.isEmpty())
						newestChange = parseCloudMaps.get(0).getUpdatedAt();
					else
						newestChange = new Date(1);
					// Get stored updatedAt
					Date storedChange = new Date(0);
					ParseQuery<ParseObject> getStoredChangeTime = ParseQuery.getQuery("MapChangeTime");
					getStoredChangeTime.fromLocalDatastore();
					ParseObject storedChangeTime;
					try {
						storedChangeTime = getStoredChangeTime.getFirst();
						if (storedChangeTime.has("time"))
							storedChange = storedChangeTime.getDate("time");
					} catch (ParseException e1) {
						storedChangeTime = new ParseObject("MapChangeTime");
						try {
							storedChangeTime.pin();
						} catch (ParseException ignored) {
						}
					}
					if (!newestChange.after(storedChange)) {
						Log.d("Parse", "No newer maps found in cloud");
						return;
					}
					Log.d("Parse", "Newer maps found in cloud, syncing...");
					// Store newer updatedAt
					storedChangeTime.put("time", newestChange);
					// Update local datastore
					ParseObject.unpinAllInBackground("maps", new DeleteCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								ParseObject.pinAllInBackground("faqs", parseCloudMaps, new SaveCallback() {
									@Override
									public void done(ParseException e) {
										if (e == null)
											Log.d("Parse", "cache maps from cloud SUCCESS");
										else
											e.printStackTrace();
									}
								});
							} else {
								e.printStackTrace();
							}
						}
					});
					setMaps(adapter, parseCloudMaps);
				} else {
					e.printStackTrace();
					// We done fucked up
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		mapView = (ImageView) rootView.findViewById(R.id.map_view);
//		progressBar = (ProgressBar) rootView.findViewById(R.id.map_load_progress);
//		progressBar.setAlpha(0);
//		progressBar.setVisibility(View.INVISIBLE);

		// Get maps from cache
		ParseQuery<ParseObject> localQuery = ParseQuery.getQuery("Maps");
		localQuery.fromLocalDatastore();
		localQuery.orderByAscending("name");
		localQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				setMaps(adapter, parseObjects);

				// Open first map
				if (!parseObjects.isEmpty()) {
					ParseFile mapFile = parseObjects.get(0).getParseFile("file");
					loadParseImage(mapFile, mapView, progressBar);
				}
			}
		});

		// Make imageview interactive
		attacher = new PhotoViewAttacher(mapView);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_map, menu);
		mapSpinner = (Spinner) menu.findItem(R.id.menu_map_spinner).getActionView();
		mapSpinner.setAdapter(adapter);
		mapSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ParseFile mapFile = adapter.getItem(position).getParseFile("file");
				loadParseImage(mapFile, mapView, progressBar);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void setMaps(ArrayAdapter<ParseObject> adapter, List<ParseObject> maps) {
		adapter.clear();
		adapter.addAll(maps);
		adapter.notifyDataSetChanged();
	}

	private void loadParseImage(ParseFile mapFile, final ImageView imageView, final ProgressBar progressBar) {
//		progressBar.setVisibility(View.VISIBLE);
//		AlphaAnimation fadeIn = new AlphaAnimation(progressBar.getAlpha(), 1);
//		fadeIn.setInterpolator(new DecelerateInterpolator());
//		fadeIn.setDuration(300);
//		progressBar.startAnimation(fadeIn);
		mapFile.getDataInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] bytes, ParseException e) {
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
				attacher.update();
//				AlphaAnimation fadeOut = new AlphaAnimation(progressBar.getAlpha(), 0);
//				fadeOut.setAnimationListener(new Animation.AnimationListener() {
//					@Override
//					public void onAnimationStart(Animation animation) {
//
//					}
//
//					@Override
//					public void onAnimationEnd(Animation animation) {
////						progressBar.setVisibility(View.INVISIBLE);
//					}
//
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//				});
//				fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
//				fadeOut.setDuration(600);
//				progressBar.startAnimation(fadeOut);
//				progressBar.setVisibility(View.INVISIBLE);
			}
		}); // Parse hasn't implemented the progress callback yet
	}
}
