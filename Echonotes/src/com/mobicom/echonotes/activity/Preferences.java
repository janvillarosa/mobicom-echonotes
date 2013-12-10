package com.mobicom.echonotes.activity;

import com.mobicom.echonotes.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private Editor settingsEditor;

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

		SharedPreferences sharedPreferences = getSharedPreferences(
				"TagPreferences", MODE_PRIVATE);
		settingsEditor = sharedPreferences.edit();

		super.onCreate(savedInstanceState);
		setTheme(R.style.AppBaseTheme);
		addPreferencesFromResource(R.xml.preference);
		PreferenceManager.setDefaultValues(Preferences.this, R.xml.preference,
				false);

		Preference settingPref = findPreference("RecordPreference");
		if (((CheckBoxPreference) settingPref).isChecked()) {
			settingPref
					.setSummary("Echonotes will record when the phone is on standby");
		} else {
			settingPref
					.setSummary("Echonotes will not record when the phone is on standby");

		}
		

		for (int i = 0; i < 6; i++) {
			settingPref = findPreference("etTagPos" + i);
			settingPref.setTitle(getSharedPreferences("TagPreferences",
					MODE_PRIVATE).getString("tagPos" + i, "Tag " + i));
		}

		settingPref = findPreference("SortPreference");
		settingPref.setSummary(((ListPreference) settingPref).getEntry());

		settingPref = findPreference("NotifsPreference");
		if (((CheckBoxPreference) settingPref).isChecked())
			settingPref.setSummary("Notifications for Echonotes is disabled");
		else {
			settingPref.setSummary("Notifications for Echonotes is enabled");
		}
		if (getPreferenceScreen().getSharedPreferences().getBoolean(
				"RecordPreference", true)) {
			settingPref.setEnabled(true);
		} else {
			settingPref.setEnabled(false);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference settingPref = findPreference(key);
		if (key.startsWith("etTagPos")) {
			((EditTextPreference) settingPref)
					.setTitle(((EditTextPreference) settingPref).getText());
			if (key.endsWith("0")) {
				Log.d("tagpos", "ONE WORKS!");
				settingsEditor.putString("tagPos0",
						((EditTextPreference) settingPref).getText());
			} else if (key.endsWith("1")) {
				settingsEditor.putString("tagPos1",
						((EditTextPreference) settingPref).getText());
			} else if (key.endsWith("2")) {
				settingsEditor.putString("tagPos2",
						((EditTextPreference) settingPref).getText());
			} else if (key.endsWith("3")) {
				settingsEditor.putString("tagPos3",
						((EditTextPreference) settingPref).getText());
			} else if (key.endsWith("4")) {
				settingsEditor.putString("tagPos4",
						((EditTextPreference) settingPref).getText());
			} else if (key.endsWith("5")) {
				settingsEditor.putString("tagPos5",
						((EditTextPreference) settingPref).getText());
			}
			
			settingsEditor.commit();
		}
		if (key.equals("RecordPreference")) {
			if (((CheckBoxPreference) settingPref).isChecked()) {
				settingPref
						.setSummary("Echonotes will record even when the phone is on standby");
				boolean isEnabled = sharedPreferences.getBoolean(key, true);
				getPreferenceScreen().findPreference("NotifsPreference")
						.setEnabled(true);
			} else {
				settingPref
						.setSummary("Echonotes will not record when the phone is on standby");
				getPreferenceScreen().findPreference("NotifsPreference")
						.setEnabled(false);
			}
		} else if (key.equals("SortPreference")) {
			settingPref.setSummary(((ListPreference) settingPref).getEntry());
		} else if (key.equals("NotifsPreference")) {
			if (((CheckBoxPreference) settingPref).isChecked())
				settingPref
						.setSummary("Notifications for Echonotes is disabled");
			else {
				settingPref
						.setSummary("Notifications for Echonotes is enabled");
			}
		}

	}
}
