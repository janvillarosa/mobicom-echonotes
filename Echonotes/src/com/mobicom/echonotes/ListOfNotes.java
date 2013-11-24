package com.mobicom.echonotes;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfNotes extends Activity {

	ListView list;
	CustomAdapter adapter;
	public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ImageButton newNote;

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

		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);

		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.nav_line_item, drawerListViewItems));

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		);

		// Set actionBarDrawerToggle as the DrawerListener
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		newNote = (ImageButton)findViewById(R.id.newNoteButton);
		
		newNote.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(ListOfNotes.this,RecordNote.class);
				newNote.setImageResource(R.drawable.newnote_button_pressed);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_of_notes, menu);
		menu.findItem(R.id.action_search).getActionView();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns
		// true
		// then it has handled the app icon touch event
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
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
