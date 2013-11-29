package com.mobicom.echonotes.activity;

import com.mobicom.echonotes.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_DarkText);
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.setDefaultValues(Preferences.this, R.xml.preference, false);
    }
}
