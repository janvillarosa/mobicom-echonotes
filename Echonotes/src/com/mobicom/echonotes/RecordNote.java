package com.mobicom.echonotes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class RecordNote extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder_screen);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recording_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
