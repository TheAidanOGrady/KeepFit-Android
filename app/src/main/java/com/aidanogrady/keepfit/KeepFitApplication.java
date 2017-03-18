package com.aidanogrady.keepfit;

import android.app.Application;

import com.aidanogrady.keepfit.data.model.units.UnitsConverter;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;
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
        SharedPreferencesRepository.makeInstance(getApplicationContext());
        UnitsConverter.setSteps(SharedPreferencesRepository.getStepsPerMetre());
    }
}
