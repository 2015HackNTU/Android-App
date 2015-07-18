package org.hackntu.hackntu2015;

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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by eddie_000 on 1/30/2015.
 */
public class FaqFragment extends Fragment {
	public static final String TAG = "FaqFragment";
	RecyclerView faqView;
	FaqAdapter faqAdapter;
	SearchView sv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		faqAdapter = new FaqAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_faq, container, false);
		faqView = (RecyclerView) rootView.findViewById(R.id.faq_view);
		faqView.setHasFixedSize(true);
		faqView.setLayoutManager(new LinearLayoutManager(getActivity()));
		faqView.setAdapter(faqAdapter);

		ParseQuery<ParseObject> query;
		query = ParseQuery.getQuery("FAQ");
		query.orderByAscending("question");
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getLocalizedMessage());
					return;
				}
				faqAdapter.changeDataSet(list);
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
				faqAdapter.getFilter().filter(s);
				return false;
			}
		});
	}

}
