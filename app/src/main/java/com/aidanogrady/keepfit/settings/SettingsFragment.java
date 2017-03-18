package com.aidanogrady.keepfit.settings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.source.HistoryRepository;

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

        addPreferencesFromResource(R.xml.app_preferences);
        Preference pref = getPreferenceManager().findPreference("clearHistory");
        pref.setOnPreferenceClickListener(preference -> {
            historyRepository.deleteAllHistory();
            return true;
        });
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
