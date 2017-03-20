package com.aidanogrady.keepfit.filters.history;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.HistoryDateFilter;
import com.aidanogrady.keepfit.data.model.HistoryGoalFilter;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;
import com.aidanogrady.keepfit.date.DateDialogPreference;
import com.aidanogrady.keepfit.date.DateDialogPreferenceFragment;

/**
 * The HistoryFilterFragment displays the filter preferences available.
 *
 * @author Aidan O'Grady
 * @since 0.11
 */
public class HistoryFilterFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.history_filters);
        ListPreference dateFilters = (ListPreference) findPreference("historyDateFilter");
        setDateFilterListPreferenceData(dateFilters);
        ListPreference goalFilters = (ListPreference) findPreference("historyGoalFilter");
        setGoalFilterListPreferenceData(goalFilters);

        EditTextPreference goalProgressFilter =
                (EditTextPreference) findPreference("historyGoalProgressFilter");
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

    /**
     * Fills the Date filter list preference with the required values.
     *
     * @param listPreference the list preference to be filled.
     */
    private void setDateFilterListPreferenceData(ListPreference listPreference) {
        HistoryDateFilter[] filters = HistoryDateFilter.values();
        CharSequence[] entries = new CharSequence[filters.length];
        CharSequence[] entryValues = new CharSequence[filters.length];
        for (int i = 0; i < filters.length; i++) {
            entries[i] = filters[i].toString();
            entryValues[i] = filters[i].name();
        }
        listPreference.setEntries(entries);
        String current = String.valueOf(
                SharedPreferencesRepository.getHistoryDateFilter().ordinal()
        );
        listPreference.setDefaultValue(current);
        listPreference.setEntryValues(entryValues);
    }

    /**
     * Fills the goal filter list preference with the required values.
     *
     * @param listPreference the list preference to be filled.
     */
    private void setGoalFilterListPreferenceData(ListPreference listPreference) {
        HistoryGoalFilter[] filters = HistoryGoalFilter.values();
        CharSequence[] entries = new CharSequence[filters.length];
        CharSequence[] entryValues = new CharSequence[filters.length];
        for (int i = 0; i < filters.length; i++) {
            entries[i] = filters[i].toString();
            entryValues[i] = filters[i].name();
        }
        listPreference.setEntries(entries);
        String current = String.valueOf(
                SharedPreferencesRepository.getHistoryGoalFilter().ordinal()
        );
        listPreference.setDefaultValue(current);
        listPreference.setEntryValues(entryValues);
    }
}
