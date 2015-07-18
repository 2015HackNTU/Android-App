package org.hackntu.hackntu2015;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Eddie on 1/20/2015.
 */
public class AnnounceFragment extends Fragment {
    RecyclerView announceView;
    AnnounceAdapter announceAdapter;
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

		ParseQuery<ParseObject> query;
		query = ParseQuery.getQuery("Push");
		query.orderByDescending("updatedAt");
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (e != null) {
					Toast.makeText(getActivity(), "e=" + e.getLocalizedMessage(), Toast
							.LENGTH_SHORT).show();
					return;
				}
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
