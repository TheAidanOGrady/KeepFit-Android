package com.aidanogrady.keepfit;

import android.app.Application;
import android.content.SharedPreferences;

import com.aidanogrady.keepfit.data.model.units.UnitsConverter;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;
import com.aidanogrady.keepfit.settings.StepMetreChangeListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * The main application class, for configuration the Date/Time library and steps/metre conversion.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
public class KeepFitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        SharedPreferences.OnSharedPreferenceChangeListener[] listeners =
                {new StepMetreChangeListener()};
        SharedPreferencesRepository.makeInstance(getApplicationContext(), listeners);
        UnitsConverter.setSteps(SharedPreferencesRepository.getStepsPerMetre());
    }
}
