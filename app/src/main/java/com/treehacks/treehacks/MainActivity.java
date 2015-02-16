package com.treehacks.treehacks;

import java.util.Locale;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;

	ScheduleFragment scheduleFragment;
	AnnounceFragment announceFragment;
	FaqFragment faqFragment;
	ReportFragment reportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    // Initialize pages
	    scheduleFragment = new ScheduleFragment();
	    announceFragment = new AnnounceFragment();
	    faqFragment = new FaqFragment();
	    reportFragment = new ReportFragment();

	    // Show hamburger menu icon
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	    // Initialize navigation list
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    drawerList = (ListView) findViewById(R.id.nav_drawer);
	    drawerList.setAdapter(new NavAdapter(this));
	    drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    // Get page to display
			    String fragTitle = ((NavAdapter.ViewHolder) view.getTag()).title.getText().toString();
			    Fragment page = getPage(fragTitle);

			    // Show page
				FragmentManager fm = getSupportFragmentManager();
			    fm.beginTransaction()
					    .replace(R.id.content_frame, page).commit();

			    // Highlight the selected item, update the title, and close the drawer
			    drawerList.setItemChecked(position, true);
			    setTitle(fragTitle);
			    drawerLayout.closeDrawer(drawerList);
		    }
	    });

	    // Initialize drawer behavior
	    drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
	    drawerLayout.setDrawerListener(drawerToggle);

	    // Show first page
	    FragmentManager fm = getSupportFragmentManager();
	    fm.beginTransaction()
			    .replace(R.id.content_frame, scheduleFragment).commit();
	    drawerList.setItemChecked(0, true);
	    setTitle(getString(R.string.title_schedule));
    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    return drawerToggle.onOptionsItemSelected(item) ||
	        super.onOptionsItemSelected(item);
    }

	Fragment getPage(String title) {
		switch (title) {
			case "Schedule":
				return scheduleFragment;
			case "Announcements":
				return announceFragment;
			case "FAQ":
				return faqFragment;
			case "Report":
				return reportFragment;
			default:
				return null;
		}
	}

}
