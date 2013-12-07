package com.mobicom.echonotes.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobicom.echonotes.ListModel;
import com.mobicom.echonotes.R;
import com.mobicom.echonotes.adapters.SearchCustomAdapter;
import com.mobicom.echonotes.database.Annotation;
import com.mobicom.echonotes.database.DatabaseHelper;
import com.mobicom.echonotes.database.Note;

public class SearchResultsActivity extends Activity {

	ListView list;
	private List<Note> searchResults;
	private List<Annotation> anno;
	private SearchCustomAdapter adapter;
	private ArrayList<ListModel> noteListModelArray = new ArrayList<ListModel>();

	DatabaseHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		//if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			dbHelper = new DatabaseHelper(getApplicationContext());
			if (intent.getStringExtra("retrieve").equals("search")) {
				String query = intent.getStringExtra("query");
				try{
				searchResults = dbHelper.getNotesLike(query);
				}
				catch(SQLiteException e){
					e.printStackTrace();
				}
			} else if (intent.getStringExtra("retrieve").equals("tags")) {
				searchResults = dbHelper.getNotesofTag(intent
						.getStringExtra("tag"));// -- get method for retrieving
												// ArrayList<Note> based on tags
			}
			list = (ListView) findViewById(R.id.searchResultsListView);
			
			if(searchResults!=null){
			initializeNoteList();
			}
		//}
	}

	public void initializeNoteList() {
		ListModel noteListModel = new ListModel();
		Note note;
		int an = 0;
		
		for (int i = 0; i < searchResults.size(); i++) {

			note = searchResults.get(i);
			noteListModel = new ListModel();

			try {
				an = dbHelper.getAnnotationsOfNote(
						searchResults.get(i).getNoteName()).size();
			} catch (Exception e) {
				an = 0;
			}

			noteListModel.setNoteName(note.getNoteName());
			noteListModel.setNumAnnotations(an);
			noteListModel.setDateAndTime(note.getDateModified());
			noteListModelArray.add(noteListModel);

			if (noteListModelArray.size() != 0) {
				adapter = new SearchCustomAdapter(this, noteListModelArray,
						getResources());
				list.setAdapter(adapter);
			}

			list.setClickable(true);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView parentView, View childView,
						int position, long id) {
					Intent intent = new Intent(SearchResultsActivity.this,
							PlayNote.class);
					startActivity(intent);
				}
			});
		}
	}
	public void onItemClick(int mPosition) {
		ListModel tempValues = noteListModelArray.get(mPosition);

		Intent intent = new Intent(SearchResultsActivity.this, PlayNote.class);
		intent.putExtra("NOTE_NAME", tempValues.getNoteName());
		intent.putExtra("NUM_ANNOTATIONS", tempValues.getNumAnnotations());

		startActivity(intent);
	}
}
