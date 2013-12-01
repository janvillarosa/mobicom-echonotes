package com.mobicom.echonotes.activity;

import com.mobicom.echonotes.database.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.mobicom.echonotes.ListModel;
import com.mobicom.echonotes.R;
import com.mobicom.echonotes.adapters.CustomAdapter;
import com.mobicom.echonotes.data.RecordingSession;

public class ListOfNotes extends Activity {

	ListView list;
	CustomAdapter adapter;
	DatabaseHelper db;
	public ArrayList<ListModel> noteListModelArray = new ArrayList<ListModel>();
	private String[] drawerListViewItems;
	private String[] tagsListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ListView tagsListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ImageButton newNote;
	private ArrayList<RecordingSession> allNotes;

	private void openSettings() {
		Intent intent = new Intent(ListOfNotes.this, Preferences.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_notes);

		// database stuff sample
		db = new DatabaseHelper(getApplicationContext());
		db.deleteAllAnnotations();
		db.deleteAllNotes();
		db.deleteAllTags();

		// Creating notes
		Note note1 = new Note("MOBICOM", "Mobicom/path", "3 days ago");
		Note note2 = new Note("ADVSTAT", "Advstat/path", "4 days ago");
		Note note3 = new Note("COMPILE", "Compile/path", "5 minutes ago");
		Note note4 = new Note("ENGLRES", "Englres/path", "a while ago");

		// Inserting tags in db

		/*
		 * long note1_id = db.createNote(note1); long annotation1_id =
		 * db.createAnnotation(annotation1); db.createNoteAnnotation(note1_id,
		 * annotation1_id); Log.d("Note id Ann id",
		 * note1_id+" "+annotation1_id+""); //DISPLAYS IN LOG FOR VERIFICATION
		 * 
		 * long note2_id = db.createNote(note2); long note3_id =
		 * db.createNote(note3); long note4_id = db.createNote(note4);
		 */

		/*
		 * public List<Todo> getAllToDosByTag(String tag_name) { List<Todo>
		 * todos = new ArrayList<Todo>();
		 * 
		 * String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, " +
		 * TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg." + KEY_TAG_NAME
		 * + " = '" + tag_name + "'" + " AND tg." + KEY_ID + " = " + "tt." +
		 * KEY_TAG_ID + " AND td." + KEY_ID + " = " + "tt." + KEY_TODO_ID;
		 * 
		 * Log.e(LOG, selectQuery);
		 * 
		 * SQLiteDatabase db = this.getReadableDatabase(); Cursor c =
		 * db.rawQuery(selectQuery, null);
		 * 
		 * // looping through all rows and adding to list if (c.moveToFirst()) {
		 * do { Todo td = new Todo();
		 * td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
		 * td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
		 * td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		 * 
		 * // adding to todo list todos.add(td); } while (c.moveToNext()); }
		 * 
		 * return todos; }
		 */

		list = (ListView) findViewById(R.id.noteListView); // List defined in
															// XML ( See Below )
		initializeNoteList();

		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);
		tagsListViewItems = getResources().getStringArray(R.array.tags);
		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.buttonsListView_gui);
		tagsListView = (ListView) findViewById(R.id.tagsListView_gui);

		// Set the adapter for the list view
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.navdrawer_list_item, drawerListViewItems));
		tagsListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.navdrawer_list_item, tagsListViewItems));

		drawerListView.setClickable(true);
		drawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView parentView,
							View childView, int position, long id) {
						switch (position) {
						case 0:
							Intent intent = new Intent(ListOfNotes.this,
									RecordNote.class);
							startActivity(intent);
						}
					}
				});

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

		newNote = (ImageButton) findViewById(R.id.newNoteButton);

		newNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListOfNotes.this, RecordNote.class);
				newNote.setImageResource(R.drawable.newnote_button_pressed);
				startActivity(intent);
			}
		});
	}

	public void onResume() {
		super.onResume();
		initializeNoteList();
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.closeDB();
	}
	
	private void initializeNoteList(){
		noteListModelArray.clear();
		ListModel noteListModel = new ListModel();
		Note note;
		int an = 0;
		

		List<Note> allNotes = db.getAllNotes();
		List<Annotation> anno = db.getAllAnnotations();
		for (int i=0; i < allNotes.size(); i++) {
			note = allNotes.get(i);
			
			noteListModel = new ListModel();
			
			try{
			an = db.getAnnotationsOfNote(i+1).size();
			} catch(Exception e){
				an = 0;
			}

			noteListModel.setNoteName(note.getNoteName());
			noteListModel.setNumAnnotations(/*
											 * db.getAnnotationsOfNote(note.
											 * getNoteId()).size()
											 */an);
			noteListModel.setDateAndTime(note.getDateModified());
			noteListModelArray.add(noteListModel);
			Log.d("Note Name", note.getNoteName() + ""); // DISPLAYS IN LOG FOR
															// VERIFICATION
			Log.d("Note Annotations", an + "");// DISPLAYS IN LOG FOR
												// VERIFICATION
			Log.d("Note id", note.getNoteId() + ""); // DISPLAYS IN LOG FOR
														// VERIFICATION
			
			Log.d("Note id Ann id ", anno.get(i).getAnnotationFilePath()+"");

		}

		Log.d("Note Count", "Note Count: " + db.getAllNotes().size()); // DISPLAYS
																		// IN
																		// LOG
																		// FOR
																		// VERIFICATION
		
		if (noteListModelArray.size() != 0) {
			adapter = new CustomAdapter(this, noteListModelArray, getResources());
			list.setAdapter(adapter);
		}

		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView,
					int position, long id) {
				Intent intent = new Intent(ListOfNotes.this, PlayNote.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_of_notes, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	public void setListData(ArrayList<RecordingSession> recordArrayList) {

		for (int i = 0; i < recordArrayList.size(); i++) {
			ListModel noteListModel = new ListModel();

			/******* Firstly take data in model object ******/
			noteListModel.setNoteName(recordArrayList.get(i).getName());
			noteListModel.setNumAnnotations(recordArrayList.get(i)
					.getAnnotationTimer().size());
			noteListModel.setDateAndTime(recordArrayList.get(i)
					.getDateModified() + "");

			noteListModelArray.add(noteListModel);
		}

	}

	public void onItemClick(int mPosition) {
		ListModel tempValues = noteListModelArray.get(mPosition);

		Intent intent = new Intent(ListOfNotes.this, PlayNote.class);
		intent.putExtra("NOTE_NAME", tempValues.getNoteName());
		intent.putExtra("NUM_ANNOTATIONS", tempValues.getNumAnnotations());
		startActivity(intent);
	}

}
