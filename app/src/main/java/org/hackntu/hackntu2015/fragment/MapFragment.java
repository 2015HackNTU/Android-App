package org.hackntu.hackntu2015.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.hackntu.hackntu2015.R;
import org.hackntu.hackntu2015.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Fetches maps from parse and displays them in a touch-enabled photo view
 * Created by Eddie on 2/19/2015.
 */
public class MapFragment extends Fragment {
	public static final String TAG = "MapFragment";
	PhotoViewAttacher attacher;
	List<Map> maps = new ArrayList<>();
	ViewPager viewPager;
	ImageLoader imageLoader;
    PagerAdapter pagerAdapter;
    PagerTabStrip pagerStrip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imageLoader = ImageLoader.getInstance();
        pagerAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(getActivity(), R.layout.page_map, null);
                final ImageView img = (ImageView) view.findViewById(R.id.map_view);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.map_load_progress);

                progressBar.setVisibility(View.VISIBLE);
                imageLoader.displayImage(maps.get(position).file.getUrl(), img, new ImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Make imageview interactive
                        attacher = new PhotoViewAttacher(img);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {}
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                });

                container.addView(view);
                return view;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return maps.get(position).name;
            }

            @Override
            public int getCount() {
                return maps.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		viewPager = (HackyViewPager) rootView.findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);

        pagerStrip = (PagerTabStrip) rootView.findViewById(R.id.pagerstrip);
        pagerStrip.setDrawFullUnderline(false);
        pagerStrip.setTabIndicatorColor(Color.WHITE);
        pagerStrip.setTextColor(Color.WHITE);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Maps");
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.orderByAscending("name");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e != null) {
					Log.e(TAG, "load map failed:" + e.getLocalizedMessage());
					return;
				}
				setMaps(parseObjects);
			}
		});

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setMaps(List<ParseObject> parseMaps) {
		if (parseMaps == null) {
			return;
		}
        maps = new ArrayList<>();
		for (ParseObject p : parseMaps) {
			Map map = new Map(p.getString("name"), p.getParseFile("file"));
			maps.add(map);
		}

        pagerAdapter.notifyDataSetChanged();
	}


	class Map {
		public Map(String name, ParseFile file) {
			this.name = name;
			this.file = file;
		}

		public String name;
		public ParseFile file;
	}
}
