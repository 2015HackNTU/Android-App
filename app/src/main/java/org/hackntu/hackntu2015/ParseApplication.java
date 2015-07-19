package org.hackntu.hackntu2015;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.File;

/**
 * Initializes Parse for the application
 * Created by Eddie on 1/27/2015.
 */
public class ParseApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// Enable Local Datastore.
//		Parse.enableLocalDatastore(this);

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

		initImageLoader(getApplicationContext());
	}

	private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "data");
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
				.memoryCache(new LruMemoryCache(1024 * 1024))
				.memoryCacheSize(1 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 MiB
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}
}
