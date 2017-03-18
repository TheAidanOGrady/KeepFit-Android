package com.aidanogrady.keepfit.settings;

import android.content.SharedPreferences;

import com.aidanogrady.keepfit.data.model.units.UnitsConverter;

/**
 * The listener fr detecting changes to the mapping between steps and metres.
 *
 * @author Aidan O'Grady
 * @since 0.8
 */
public class StepMetreChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String KEY = "stepsPerMetre";

    private static final String DEFAULT = "1.5";

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(KEY) && sharedPreferences.contains(KEY)) {
            String newVal = sharedPreferences.getString(KEY, DEFAULT);
            UnitsConverter.setSteps(Double.valueOf(newVal));
        }
    }
}
