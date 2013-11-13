package com.mobicom.echonotes;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfNotes extends Activity {

	ListView list;
	CustomAdapter adapter;
	public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
	String[] test = {"test"};
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_notes);

		setListData();

		Resources res = getResources();
		list = (ListView) findViewById(R.id.noteListView); // List defined in
															// XML ( See Below )

		/**************** Create Custom Adapter *********/
		adapter = new CustomAdapter(this, CustomListViewValuesArr, res);
		list.setAdapter(adapter);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        if(mDrawerLayout==null)
        	System.out.println("ERROR");
 

		// Set the adapter for the list view
		//mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer, test));
		// Set the list's click listener
		//mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout, R.drawable.ic_drawer, R.string.dropen,
				R.string.drclose) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("test");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Test");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		//mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_of_notes, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
