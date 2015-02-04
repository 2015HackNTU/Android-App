package com.treehacks.treehacks;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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
		if (ignoredPointers.contains(ev.getPointerId(0))) {
			return false;
		}
		return super.onTouchEvent(ev);
	}

	// Limits scrolling when displaying schedule (index 0)
	// Remembers pointer IDs of down events in the center of the screen
	// and ignores them in onTouchEvent
	// Allows scrolling if the user touched the side of the device
	// TODO: allow view scrolling on the pager title strip
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (getCurrentItem() == 0) {
			if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
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
				if (ev.getRawX() < width / 8 || width * 7 / 8 < ev.getRawX()) {
					return true;
				}
				else {
					ignoredPointers.add(ev.getPointerId(0));
					return false;
				}
			}
			else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
				int pointerId = ev.getPointerId(0);
				if (ignoredPointers.contains(pointerId))
					ignoredPointers.remove(pointerId);
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
