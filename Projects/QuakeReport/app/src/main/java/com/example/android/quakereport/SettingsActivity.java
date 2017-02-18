package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minMagnitudePref = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitudePref);

            Preference fetchLimitPref = findPreference(getString(R.string.settings_request_limit_key));
            bindPreferenceSummaryToValue(fetchLimitPref);

            Preference orderByPref = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderByPref);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefStringVal = sharedPref.getString(preference.getKey(), "");
            onPreferenceChange(preference, prefStringVal);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String strVal = newValue.toString();
            if(preference instanceof ListPreference) {
                ListPreference listPref = (ListPreference) preference;
                int index = listPref.findIndexOfValue(strVal);
                if(index >= 0) {
                    CharSequence[] entryLabels = listPref.getEntries();
                    preference.setSummary(entryLabels[index]);
                }
            } else {
                preference.setSummary(strVal);
            }
            return true;
        }
    }
}

