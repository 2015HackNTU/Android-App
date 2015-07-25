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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by eddie_000 on 1/30/2015.
 */
public class FaqFragment extends Fragment {
	public static final String TAG = "FaqFragment";
	TextView phoneText;
	TextView emailText;
	TextView whereText;
	RecyclerView faqView;
	FaqAdapter faqAdapter;
	ProgressBar progressBar;
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
		phoneText = (TextView) rootView.findViewById(R.id.txt_phone);
		emailText = (TextView) rootView.findViewById(R.id.txt_email);
		whereText = (TextView) rootView.findViewById(R.id.txt_where);
		faqView = (RecyclerView) rootView.findViewById(R.id.faq_view);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

		faqView.setHasFixedSize(true);
		faqView.setLayoutManager(new LinearLayoutManager(getActivity()));
		faqView.setAdapter(faqAdapter);

		progressBar.setVisibility(View.VISIBLE);

		ParseQuery<ParseObject> contactQuery;
		contactQuery = ParseQuery.getQuery("Contact");
		contactQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		contactQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject p, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                    return;
                }
				showContact(p);
			}
		});


		ParseQuery<ParseObject> faqQuery;
		faqQuery = ParseQuery.getQuery("FAQ");
		faqQuery.orderByAscending("question");
		faqQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		faqQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getLocalizedMessage());
					return;
				}
				progressBar.setVisibility(View.GONE);
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

	private void showContact(ParseObject p) {
        phoneText.setText(p.getString("phone"));
        emailText.setText(p.getString("email"));
        whereText.setText(p.getString("where"));
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
