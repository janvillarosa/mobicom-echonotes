package com.mobicom.echonotes.activity;

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.mobicom.echonotes.ListModel;
import com.mobicom.echonotes.NavBarListModel;
import com.mobicom.echonotes.R;
import com.mobicom.echonotes.adapters.CustomAdapter;
import com.mobicom.echonotes.data.RecordingSession;

public class ListOfNotes extends Activity {

	ListView list;
	CustomAdapter adapter; 
	public ArrayList<ListModel> noteListModelArray = new ArrayList<ListModel>();
	public ArrayList<NavBarListModel> navButtonListModelArray = new ArrayList<NavBarListModel>();
	public ArrayList<NavBarListModel> navTagsListModelArray = new ArrayList<NavBarListModel>();
	private String[] drawerListViewItems;
	private String[] tagsListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ListView tagsListView;
	private EditText editSearch;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ImageButton newNote;
	private ArrayList<RecordingSession> allNotes;
	
	
	private void openSettings(){
		Intent intent = new Intent(ListOfNotes.this,Preferences.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//writeToNoteArray();
		setContentView(R.layout.list_of_notes);

		ListModel noteListModel = new ListModel();

		noteListModel.setNoteName("ADVSTAT");
		noteListModel.setNumAnnotations(5);
		noteListModel.setDateAndTime("10 hours ago");

		noteListModelArray.add(noteListModel);
		
		noteListModel = new ListModel();
		
		noteListModel.setNoteName("MOBICOM");
		noteListModel.setNumAnnotations(8);
		noteListModel.setDateAndTime("10 days ago");

		noteListModelArray.add(noteListModel);
		
		noteListModel = new ListModel();
		
		noteListModel.setNoteName("COMPILE");
		noteListModel.setNumAnnotations(20);
		noteListModel.setDateAndTime("3 days ago");

		noteListModelArray.add(noteListModel);
		//until here once File Reader has values
		
		
		Resources res = getResources();
		list = (ListView) findViewById(R.id.noteListView); // List defined in
															// XML ( See Below )

		/**************** Create Custom Adapter *********/
		adapter = new CustomAdapter(this, noteListModelArray, res);
		list.setAdapter(adapter);
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView, int position, long id)
			{
				Intent intent = new Intent(ListOfNotes.this,PlayNote.class);
				startActivity(intent);	
			}
		});

		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);
		tagsListViewItems = getResources().getStringArray(R.array.tags);
		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.buttonsListView_gui);
		tagsListView = (ListView) findViewById(R.id.tagsListView_gui);

		// Set the adapter for the list view
		//drawerListView.setAdapter(new NavBarListAdapter(this, navButtonListModelArray, res));
		tagsListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.nav_line_item, tagsListViewItems));
		
		drawerListView.setClickable(true);
		drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView, int position, long id)
			{
				switch(position){
				case 0: Intent intent = new Intent(ListOfNotes.this,RecordNote.class);
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
		
		newNote = (ImageButton)findViewById(R.id.newNoteButton);
		
		newNote.setOnClickListener(new View.OnClickListener(){
			@Override
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
		SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView =
		            (SearchView) menu.findItem(R.id.action_search).getActionView();
		    searchView.setSearchableInfo(
		            searchManager.getSearchableInfo(getComponentName()));
		return true;
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
		
		switch (item.getItemId()) {
        case R.id.action_search:
            //openSearch();
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
		
		
		
			for(int i = 0; i<recordArrayList.size();i++){
			ListModel noteListModel = new ListModel();

			/******* Firstly take data in model object ******/
			noteListModel.setNoteName(recordArrayList.get(i).getName());
			noteListModel.setNumAnnotations(recordArrayList.get(i).getAnnotationTimer().size());
			noteListModel.setDateAndTime(recordArrayList.get(i).getDateModified()+"");

			noteListModelArray.add(noteListModel);
			}

	}

	public void onItemClick(int mPosition) {
		ListModel tempValues = noteListModelArray.get(mPosition);

		Intent intent = new Intent(ListOfNotes.this, PlayNote.class);
		intent.putExtra("NOTE_NAME",tempValues.getNoteName());
		intent.putExtra("NUM_ANNOTATIONS",tempValues.getNumAnnotations());
		startActivity(intent);
	}
	
	public void readFromNoteArray(){
		BufferedReader reader = null;
		
		try{
			File file = new File(Environment.getExternalStorageDirectory().getPath()+"Echonotes/notes.txt");
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			int i=0;
			while((line = reader.readLine()) !=null){
				allNotes.get(i).setName(reader.readLine());
				allNotes.get(i).setRecordingFilePath(reader.readLine());
				allNotes.get(i).setDateModified(reader.readLine());
				allNotes.get(i).setCategory(reader.readLine());
				for(int j=0;j<allNotes.get(i).getListOfTextAnnotations().size();j++){
					allNotes.get(i).getListOfTextAnnotations().add(reader.readLine());
				}
				for(int j=0;j<allNotes.get(i).getListOfPicturePathAnnotations().size();j++){
					allNotes.get(i).getListOfPicturePathAnnotations().add(reader.readLine());
				}
				for(int j=0;j<allNotes.get(i).getAnnotationTimer().size();j++){
				allNotes.get(i).getAnnotationTimer().add(Long.parseLong(reader.readLine()));
				}
				i++;
			}
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		
		
	}
}
