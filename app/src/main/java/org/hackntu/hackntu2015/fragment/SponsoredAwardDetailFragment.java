package org.hackntu.hackntu2015.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.hackntu.hackntu2015.object.ApiAward;
import org.hackntu.hackntu2015.R;

/**
 * Created by weitang114 on 15/7/24.
 */
public class SponsoredAwardDetailFragment extends Fragment {

    public static final String AWARD = "award";
    ApiAward award;
    ImageLoader imageLoader;

    public static SponsoredAwardDetailFragment newInstance(ApiAward award) {
        SponsoredAwardDetailFragment frag = new SponsoredAwardDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(AWARD, award);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        award = getArguments().getParcelable(AWARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_awarddetail_sponsored, container, false);
        TextView titleText = (TextView) rootView.findViewById(R.id.txt_title);
        ImageView imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        TextView infoText = (TextView) rootView.findViewById(R.id.txt_info);
        TextView prizeText = (TextView) rootView.findViewById(R.id.txt_prize);
        TextView criteriaText = (TextView) rootView.findViewById(R.id.txt_criteria);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.loading);

        titleText.setText(award.companyName);
        infoText.setText(award.companyInfo);
        prizeText.setText(award.prize);
        criteriaText.setText(award.criteria);

        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(award.imageUrl, imgLogo, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });

        return rootView;
    }
}

