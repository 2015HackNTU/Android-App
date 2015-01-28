package com.treehacks.treehacks;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Because Parse is a piece of shit.
 * Created by Eddie on 1/27/2015.
 */
public class ParseApplication extends Application {
	@Override
	public void onCreate() {

		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);

		Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
		Log.d("Parse", "initialized");

		// Register for 'broadcast' channel
		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe to push notifications", e);
				}
			}
		});
	}
}
