package com.aidanogrady.keepfit;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * The main application class, for configuration the Date/Time library.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
public class KeepFitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}