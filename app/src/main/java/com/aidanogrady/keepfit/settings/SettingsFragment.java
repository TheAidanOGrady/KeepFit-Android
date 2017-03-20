package com.aidanogrady.keepfit.settings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.source.HistoryRepository;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;
import com.aidanogrady.keepfit.data.source.UpdatesRepository;
import com.aidanogrady.keepfit.date.DateDialogPreference;
import com.aidanogrady.keepfit.date.DateDialogPreferenceFragment;

import java.util.Arrays;

/**
 * The SettingsFragment displays the various settings that the users can change.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        HistoryRepository historyRepository = HistoryRepository.getInstance(getContext());
        UpdatesRepository updatesRepository = UpdatesRepository.getInstance(getContext());

        addPreferencesFromResource(R.xml.app_preferences);
        Preference pref = getPreferenceManager().findPreference("clearHistory");
        pref.setOnPreferenceClickListener(preference -> {
            historyRepository.deleteAllHistory();
            updatesRepository.deleteAllUpdates();
            return true;
        });

        ListPreference displayUnit = (ListPreference) findPreference("historyDisplayUnit");
        System.out.println(displayUnit == null);
        setHistoryDisplayUnitPreferenceData(displayUnit);
    }

    /**
     * Fills the given listPreference data with the unit information.
     *
     * @param listPreference the list preference to be filled
     */
    private void setHistoryDisplayUnitPreferenceData(ListPreference listPreference) {
        Unit[] units = Unit.values();
        CharSequence[] entries = new CharSequence[units.length + 1];
        CharSequence[] entryValues = new CharSequence[units.length + 1];

        entries[0] = "GOAL'S DEFAULT";
        entryValues[0] = "DEFAULT";
        for (int i = 0; i < units.length; i++) {
            entries[i + 1] = units[i].name();
            entryValues[i + 1] = units[i].name();
        }

        System.out.println(Arrays.toString(entryValues));
        listPreference.setEntries(entries);

        Unit current = SharedPreferencesRepository.getHistoryDisplayUnit();
        String currentStr;
        if (current == null) {
            currentStr = "DEFAULT";
        } else {
            currentStr = String.valueOf(current.ordinal());
        }
        listPreference.setDefaultValue(currentStr);
        listPreference.setEntryValues(entryValues);
        System.out.println(currentStr);

    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof DateDialogPreference) {
            dialogFragment = DateDialogPreferenceFragment.newInstance(preference.getKey());
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "com.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }


}
