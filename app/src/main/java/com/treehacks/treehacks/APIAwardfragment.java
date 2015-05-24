package com.treehacks.treehacks;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;



public class APIAwardfragment extends Fragment {
    RecyclerView apiawardView;
    APIAwardAdapter apiAwardAdapter =new APIAwardAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_apiaward, container, false);
        apiawardView = (RecyclerView) rootView.findViewById(R.id.apiaward_view);
        apiawardView.setHasFixedSize(true);
        apiawardView.setLayoutManager(new LinearLayoutManager(getActivity()));

        apiawardView.setAdapter(apiAwardAdapter);



        apiAwardAdapter.newListener = myListener;


        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            CardView cardView = (CardView) v;
//            showAPIDialog(cardView.);
//            Log.i("test", "success award");
//
//            //TODO
//        }
//    };



    MyListenerClass myListener = new MyListenerClass();



    public void showAPIDialog(int index ){


        DialogFragment apiDialogfragment = APIDialogfragment.newInstance(index);

        apiDialogfragment.show(getFragmentManager(), "API");
    }


    class MyListenerClass  implements APIAwardAdapter.CardViewClickListener {
        @Override
        public void onClick(int index) {
            showAPIDialog(index);
        }
    }




}





