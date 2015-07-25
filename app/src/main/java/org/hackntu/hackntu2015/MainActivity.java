package org.hackntu.hackntu2015;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    NavAdapter navAdapter;

    ScheduleFragment scheduleFragment;
    AnnounceFragment announceFragment;
    FaqFragment faqFragment;
    MapFragment mapFragment;
    AwardFragment awardFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize pages
        announceFragment = new AnnounceFragment();
        faqFragment = new FaqFragment();
        scheduleFragment = new ScheduleFragment();
        mapFragment = new MapFragment();
        awardFragment = new AwardFragment();

        // Show hamburger menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize navigation list
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.nav_drawer);
        navAdapter = new NavAdapter(this);
        drawerList.setAdapter(navAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get page to display
                String fragTitle = ((NavAdapter.ViewHolder) view.getTag()).title.getText().toString();
                Fragment page = getPage(fragTitle);

                // Show page
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction()
                        .replace(R.id.content_frame, page).commit();

                // Highlight the selected item, update the title, and close the drawer
                navAdapter.activate(position);
                drawerList.setItemChecked(position, true);
                setTitle(fragTitle);
                drawerLayout.closeDrawer(drawerList);
            }
        });

        // Initialize drawer behavior
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // Show first page
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_frame, announceFragment).commit();
        drawerList.setItemChecked(0, true);
        setTitle("Announcements");
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
            case "Announcements":
                return announceFragment;
            case "FAQ":
                return faqFragment;
            case "Schedule":
                return scheduleFragment;
            case "Maps":
                return mapFragment;
            case "Award":
                return awardFragment;
            default:
                return null;
        }
    }
};


