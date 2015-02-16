package com.treehacks.treehacks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

/**
 * Created by Eddie on 2/10/2015.
 */
public class CountdownView extends SurfaceView {
	long startTime;
	long endTime;
	Paint progressPaint;
	Canvas canvas;

	public CountdownView(Context context) {
		super(context);

		// Set submission deadline
		// c.f. times in ScheduleFragment
		Calendar time = Calendar.getInstance();
		time.set(2015,Calendar.FEBRUARY, 20, 12, 0);
		startTime = time.getTimeInMillis();
		time = Calendar.getInstance();
		time.set(2015, Calendar.FEBRUARY, 22, 10, 0);
		endTime = time.getTimeInMillis();

		// Setup drawing resources
		progressPaint = new Paint();
		progressPaint.setColor(getResources().getColor(R.color.treehacks_red));
		progressPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void onDraw(Canvas canvas) {
		long currentTime = System.currentTimeMillis();
		float done = (float )(currentTime - startTime) / (endTime - startTime);
		canvas.drawArc(0, 0, canvas.getWidth(), canvas.getHeight(), 0, done, false, progressPaint);
	}
}
