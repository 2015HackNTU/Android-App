package com.treehacks.treehacks;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
    AwardAdapter awardAdapter=new AwardAdapter();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_award, container, false);
        awardView = (RecyclerView) rootView.findViewById(R.id.award_view);
        awardView.setHasFixedSize(true);
        awardView.setLayoutManager(new LinearLayoutManager(getActivity()));

        awardView.setAdapter(awardAdapter);
	return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);




    }

    ;


}

