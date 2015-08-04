package org.hackntu.hackntu2015.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.hackntu.hackntu2015.object.Award;
import org.hackntu.hackntu2015.R;
import org.hackntu.hackntu2015.utils.RecyclerItemClickListener;
import org.hackntu.hackntu2015.adapter.TopTenAwardAdapter;

import java.util.List;

/**
 * Created by weitang114 on 15/7/24.
 */
public class TopTenAwardFragment extends Fragment {
    public static final String TAG = "APIAwardFragment";
    RecyclerView awardsView;
    TopTenAwardAdapter adapter;
    ImageLoader imageLoader;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_apiaward, container, false);
        adapter = new TopTenAwardAdapter(getActivity());
        awardsView = (RecyclerView) rootView.findViewById(R.id.apiaward_view);
        awardsView.setHasFixedSize(true);
        awardsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        awardsView.setAdapter(adapter);
        awardsView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showAwardDetail(position);
                    }
                }));

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Top10Prize");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "find api awards failed:" + e.getLocalizedMessage());
                    return;
                }
                adapter.changeDataset(list);
                progressBar.setVisibility(View.GONE);
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showAwardDetail(int index) {
        Award award = adapter.getItem(index);
        AwardDetailFragment frag = AwardDetailFragment.newInstance(award);
        getFragmentManager().beginTransaction()
                .addToBackStack("detail")
                .replace(R.id.content_frame, frag)
                .commit();
    }
}

