package org.hackntu.hackntu2015;

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

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Fetches maps from parse and displays them in a touch-enabled photo view
 * Created by Eddie on 2/19/2015.
 */
public class MapFragment extends Fragment {
	public static final String TAG = "MapFragment";
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		mapView = (ImageView) rootView.findViewById(R.id.map_view);
		progressBar = (ProgressBar) rootView.findViewById(R.id.map_load_progress);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Maps");
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.orderByAscending("name");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e != null) {
					Log.e(TAG, "load map failed:" + e.getLocalizedMessage());
					return;
				}
				setMaps(parseObjects);

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

	private void setMaps(List<ParseObject> maps) {
		if (maps == null) {
			return;
		}
		adapter.clear();
		adapter.addAll(maps);
		adapter.notifyDataSetChanged();
	}

	private void loadParseImage(ParseFile mapFile, final ImageView imageView, final ProgressBar progressBar) {
		imageView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);

		mapFile.getDataInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] bytes, ParseException e) {
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
				imageView.setVisibility(View.VISIBLE);
				attacher.update();
				progressBar.setVisibility(View.GONE);
			}
		});
	}
}
