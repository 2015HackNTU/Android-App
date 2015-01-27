package com.treehacks.treehacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 1/20/2015.
 */
public class AnnounceFragment extends Fragment {
    RecyclerView announceView;
    AnnounceAdapter announceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Sync announcements from cloud
        ParseQuery<ParseObject> cloudQuery = ParseQuery.getQuery("Push");
	    cloudQuery.orderByAscending("updatedAt");
        cloudQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> parseCloudAnnouncements, ParseException e) {
                if (e == null) {
                    // Update local datastore
                    ParseObject.unpinAllInBackground("announcements", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("announcements", parseCloudAnnouncements, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                            Log.d("Parse", "cache announcements from cloud SUCCESS");
                                        else
                                            e.printStackTrace();
                                    }
                                });
                            }
                            else {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (announceAdapter != null) {
                        announceAdapter.announcements = parseCloudAnnouncements;
                        announceAdapter.notifyDataSetChanged();
                    }
                } else {
                    e.printStackTrace();
                    // We done fucked up
                }
            }
        });
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_announce, container, false);
		announceView = (RecyclerView) rootView.findViewById(R.id.announce_view);
        announceView.setHasFixedSize(true);
        announceView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get announcements from cache
        ParseQuery<ParseObject> localQuery = ParseQuery.getQuery("Push");
        localQuery.fromLocalDatastore();
		localQuery.orderByAscending("updatedAt");
        List<ParseObject> pushes;
        try {
            pushes = localQuery.find();
            Log.d("Parse", "read announcements from local db SUCCESS");
        } catch (ParseException e) {
            e.printStackTrace();
            pushes = new ArrayList<>();
        }

        announceAdapter = new AnnounceAdapter(pushes);
        announceView.setAdapter(announceAdapter);
        return rootView;
	}

}
