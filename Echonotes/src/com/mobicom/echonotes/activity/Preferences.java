package com.mobicom.echonotes.activity;

import com.mobicom.echonotes.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppBaseTheme);
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.setDefaultValues(Preferences.this, R.xml.preference, false);
        Preference connectionPref = findPreference("RecordPreference");
        if(((CheckBoxPreference)connectionPref).isChecked())
			connectionPref.setSummary("Echonotes will record even when the phone is on standby");
		else{
			connectionPref.setSummary("Echonotes will not record when the phone is on standby");
		}
        connectionPref = findPreference("SortPreference");
        connectionPref.setSummary(((ListPreference)connectionPref).getEntry());
        connectionPref = findPreference("NotifsPreference");
        if(((CheckBoxPreference)connectionPref).isChecked())
			connectionPref.setSummary("Notifications for Echonotes is disabled");
		else{
			connectionPref.setSummary("Notifications for Echonotes is enabled");
		}
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference connectionPref = findPreference(key);
		if(key.equals("RecordPreference")){
			if(((CheckBoxPreference)connectionPref).isChecked()){
				connectionPref.setSummary("Echonotes will record even when the phone is on standby");
				
			}
			else{
				connectionPref.setSummary("Echonotes will not record when the phone is on standby");
			}
		}
		else if(key.equals("SortPreference")){
			connectionPref.setSummary(((ListPreference)connectionPref).getEntry());
		}
		else if(key.equals("NotifsPreference")){
				if(((CheckBoxPreference)connectionPref).isChecked())
					connectionPref.setSummary("Notifications for Echonotes is disabled");
				else{
					connectionPref.setSummary("Notifications for Echonotes is enabled");
				}
		}
		
	}
}
