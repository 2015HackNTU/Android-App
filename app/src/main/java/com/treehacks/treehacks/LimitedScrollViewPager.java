package com.treehacks.treehacks;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.HashSet;

/**
 * If the child view can scroll horizontally, only scrolls if the edge of the screen is selected.
 * Needed because android week view is a POS and doesn't properly consume motion events.
 * Created by Eddie on 2/1/2015.
 */
public class LimitedScrollViewPager extends ViewPager {
	Context context;
	HashSet<Integer> ignoredPointers;
	PagerTitleStrip pagerTitleStrip;
	int pagerTitleStripBottom;

	private void init (Context context) {
		this.context = context;
		ignoredPointers = new HashSet<>();
	}

	public LimitedScrollViewPager(Context context) {
		super(context);
		init(context);
	}

	public LimitedScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return !ignoredPointers.contains(ev.getPointerId(0)) && super.onTouchEvent(ev);
	}

	// Get position of bottom of pagerTitleStrip so scrolling is not disabled above it
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		int[] location = new int[2];
		pagerTitleStrip.getLocationOnScreen(location);
		pagerTitleStripBottom = location[1] + pagerTitleStrip.getBottom();
	}

	// Limits scrolling when displaying schedule (index 0)
	// Remembers pointer IDs of down events in the center of the screen
	// and ignores them in onTouchEvent
	// Allows scrolling if the user touched the side of the device
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		// Only limit scrolling for schedule view
		if (getCurrentItem() == 0) {

			// Capture down events to ignore pointer
			if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {

				// Get window width
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				int width;
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					Point displaySize = new Point();
					wm.getDefaultDisplay().getSize(displaySize);
					width = displaySize.x;
				}
				else {
					width = wm.getDefaultDisplay().getWidth();
				}

				// Allow scrolling if started in edge 1/8 of screen, or above schedule view
				if (ev.getRawX() < width / 8 || width * 7 / 8 < ev.getRawX() || ev.getRawY() < pagerTitleStripBottom) {
					return true;
				}
				else {
					ignoredPointers.add(ev.getPointerId(0));
					return false;
				}
			}
			// un-ignore pointer on up events
			else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
				ignoredPointers.remove(ev.getPointerId(0));
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void onPageScrolled(int position, float offset, int offsetPixels) {
		super.onPageScrolled(position, offset, offsetPixels);
		ignoredPointers.clear();
	}
}
