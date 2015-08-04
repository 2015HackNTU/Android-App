package org.hackntu.hackntu2015.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.hackntu.hackntu2015.R;
import org.hackntu.hackntu2015.adapter.AnnounceAdapter;

import java.util.List;

/**
 * Created by Eddie on 1/20/2015.
 */
public class AnnounceFragment extends Fragment {
	public static final String TAG = "AnnounceFragment";

    RecyclerView announceView;
    AnnounceAdapter announceAdapter;
    ProgressBar progressBar;
	SearchView sv;


	public AnnounceFragment() {

	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
		announceAdapter = new AnnounceAdapter();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_announce, container, false);

		announceView = (RecyclerView) rootView.findViewById(R.id.announce_view);
        announceView.setHasFixedSize(true);
        announceView.setLayoutManager(new LinearLayoutManager(getActivity()));
		announceView.setAdapter(announceAdapter);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

		ParseQuery<ParseObject> query;
		query = ParseQuery.getQuery("Push");
		query.orderByDescending("updatedAt");
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (e != null) {
					Log.e(TAG, "find anncouncement failed:" + e.getLocalizedMessage());
					return;
				}
				progressBar.setVisibility(View.GONE);
				announceAdapter.changeDataSet(list);
			}
		});

        return rootView;
	}

	// Clear searchview on page change
	@Override
	public void onStop() {
		sv.setQuery("", false);
		sv.clearFocus();
		super.onStop();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);

		// Assoc. searchview config to SearchView
		SearchManager sm = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		sv = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
		sv.setSearchableInfo(sm.getSearchableInfo(getActivity().getComponentName()));
		sv.setQueryHint(getResources().getString(R.string.search_hint));
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				announceAdapter.getFilter().filter(s);
				return false;
			}
		});
	}
}
