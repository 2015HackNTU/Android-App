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

import org.hackntu.hackntu2015.object.ApiAward;
import org.hackntu.hackntu2015.R;
import org.hackntu.hackntu2015.utils.RecyclerItemClickListener;
import org.hackntu.hackntu2015.adapter.APIAwardAdapter;

import java.util.List;


public class APIAwardfragment extends Fragment {
    public static final String TAG = "APIAwardFragment";
    RecyclerView apiawardView;
    APIAwardAdapter apiAwardAdapter;
    ImageLoader imageLoader;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiAwardAdapter = new APIAwardAdapter();
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
        apiawardView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showAPIDialog(position);
                    }
                }));

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("API_Enterprise");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "find api awards failed:" + e.getLocalizedMessage());
                    return;
                }
                apiAwardAdapter.changeDataset(list);
                progressBar.setVisibility(View.GONE);
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showAPIDialog(int index){
        ApiAward award = apiAwardAdapter.getItem(index);
        SponsoredAwardDetailFragment frag =
                SponsoredAwardDetailFragment.newInstance(award);

        getFragmentManager().beginTransaction()
                .addToBackStack("detail")
                .replace(R.id.content_frame, frag)
                .commit();

//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.show();
//
//        final ApiAward award = apiAwardAdapter.getItem(index);
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.loadImage(award.imageUrl, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                DialogFragment apiDialogfragment = APIDialogfragment.newInstance(award, loadedImage);
//                apiDialogfragment.show(getFragmentManager(), "API");
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                super.onLoadingFailed(imageUri, view, failReason);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                super.onLoadingCancelled(imageUri, view);
//                progressDialog.dismiss();
//            }
//        });
    }
}





