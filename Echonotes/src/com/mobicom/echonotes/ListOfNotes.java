package com.mobicom.echonotes;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfNotes extends Activity {

	ListView list;
	CustomAdapter adapter;
	public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
	private String[] test = {"test"};
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private static LayoutInflater inflater = null;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_notes);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setListData();

		Resources res = getResources();
		list = (ListView) findViewById(R.id.noteListView); // List defined in
															// XML ( See Below )

		/**************** Create Custom Adapter *********/
		adapter = new CustomAdapter(this, CustomListViewValuesArr, res);
		list.setAdapter(adapter);
		
		View navDrawer = inflater.inflate(R.layout.nav_drawer, null);
		
		mDrawerLayout = (DrawerLayout) navDrawer.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) navDrawer.findViewById(R.id.left_drawer);
 

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer, test));
		// Set the list's click listener
		//mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.dropen,  /* "open drawer" description */
                R.string.drclose  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("My Echonotes");
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflaterMenu = getMenuInflater();
		inflaterMenu.inflate(R.menu.list_of_notes, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.left_drawer).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

	public void setListData() {

		for (int i = 0; i < 11; i++) {

			final ListModel sched = new ListModel();

			/******* Firstly take data in model object ******/
			sched.setNoteName("Test Note");
			sched.setNumAnnotations("2 annotations");
			sched.setDateAndTime("5 minutes ago");

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}

	}

	public void onItemClick(int mPosition) {
		ListModel tempValues = (ListModel) CustomListViewValuesArr
				.get(mPosition);

		// SHOW ALERT

		Toast.makeText(this, "Selected " + mPosition, Toast.LENGTH_LONG).show();
	}
}
