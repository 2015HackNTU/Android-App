package org.hackntu.hackntu2015;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;


/**
 * Fetches maps from parse and displays them in a touch-enabled photo view
 * Created by Eddie on 2/19/2015.
 */
public class AwardFragment extends Fragment {
    RecyclerView awardView;
    AwardAdapter awardAdapter = new AwardAdapter();

    APIAwardfragment apIawardfragment;
    TopTenAwardFragment topTenFragment;
    PopularityAwardFragment popularityAwardFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        apIawardfragment = new APIAwardfragment();
        topTenFragment = new TopTenAwardFragment();
        popularityAwardFragment = new PopularityAwardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_award, container, false);
        awardView = (RecyclerView) rootView.findViewById(R.id.award_view);
        awardView.setHasFixedSize(true);
        awardView.setLayoutManager(new LinearLayoutManager(getActivity()));

        awardView.setAdapter(awardAdapter);
        awardView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0: // general top 10
                        showGeneralTopFragment();
                        break;
                    case 1: // sponsored
                        showApiAwardFragment();
                        break;
                    case 2: // popularity
                        showPopularityAwardFragment();
                        break;
                }
            }
        }));
	    return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showGeneralTopFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().addToBackStack("award")
                .replace(R.id.content_frame, topTenFragment).commit();
    }

    public void showApiAwardFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().addToBackStack("award")
                .replace(R.id.content_frame, apIawardfragment).commit();
    }

    public void showPopularityAwardFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().addToBackStack("award")
                .replace(R.id.content_frame, popularityAwardFragment).commit();
    }

}

