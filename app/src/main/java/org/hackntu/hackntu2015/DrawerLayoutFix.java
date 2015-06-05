package org.hackntu.hackntu2015;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Simple wrapper for DrawerLayout because it has a bug when used with PhotoView.
 * See https://github.com/chrisbanes/PhotoView/issues/72
 * Created by Eddie on 2/19/2015.
 */
public class DrawerLayoutFix extends DrawerLayout {


	public DrawerLayoutFix(Context context) {
		super(context);
	}

	public DrawerLayoutFix(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawerLayoutFix(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}
}
