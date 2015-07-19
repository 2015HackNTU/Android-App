package org.hackntu.hackntu2015;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


public class APIAwardfragment extends Fragment {
    RecyclerView apiawardView;
    APIAwardAdapter apiAwardAdapter;
    MyListenerClass myListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myListener = new MyListenerClass();
        apiAwardAdapter = new APIAwardAdapter(myListener);
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
        apiAwardAdapter.mListener = myListener;
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showAPIDialog(int index){
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





