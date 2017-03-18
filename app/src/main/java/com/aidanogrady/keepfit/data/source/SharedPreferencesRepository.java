package com.aidanogrady.keepfit.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * The concrete PreferenceRepository retrieves the database from Android's SharedPreferences. This
 * allows for presenters to have no awareness of Android.
 *
 * @author Aidan O'Grady
 * @since 0.8
 */
public class SharedPreferencesRepository implements PreferenceRepository {
    /**
     * Singleton instance.
     */
    private static PreferenceRepository sInstance;

    /**
     * The shared preferences information is retrieved from.
     */
    private SharedPreferences mSharedPreferences;


    /**
     * Constructs a new SharedPreferencesRepository. The constructor is private to ensure singleton
     * is maintained.
     *
     * @param context the context required to retrieve shared preferences.
     */
    private SharedPreferencesRepository(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * Makes a new instance of the singleton.
     *
     * @param context the context
     */
    public static void makeInstance(Context context) {
        sInstance = new SharedPreferencesRepository(context);
    }

    /**
     * Returns the singleton repository.
     *
     * @return singleton instance
     */
    public static PreferenceRepository getInstance() {
        return sInstance;
    }

    /**
     * Returns true if editing goals has been enabled.
     *
     * @return true if goals can be edited, otherwise false
     */
    public static boolean isEditGoalEnabled() {
        return sInstance.getIsEditGoalEnabled();
    }

    /**
     * Returns true if test mode is enabled.
     *
     * @return true if enabled, otherwise false
     */
    public static boolean isTestModeEnabled() {
        return sInstance.getIsTestModeEnabled();
    }

    /**
     * Returns the date of test mode.
     *
     * @return the date of test mode
     */
    public static long getTestModeDate() {
        return sInstance.getTestDate();
    }

    /**
     * Returns the steps/metre conversion.
     *
     * @return steps/metres conversion
     */
    public static double getStepsPerMetre() {
        return sInstance.getStepsToMetres();
    }

    @Override
    public boolean getIsEditGoalEnabled() {
        return mSharedPreferences.getBoolean("editGoalsEnabled", true);
    }

    @Override
    public boolean getIsTestModeEnabled() {
        return mSharedPreferences.getBoolean("testModeEnabled", true);
    }

    @Override
    public long getTestDate() {
        return mSharedPreferences.getLong("testModeDate", -1);
    }

    @Override
    public double getStepsToMetres() {
        return mSharedPreferences.getLong("metersPerStep", Double.doubleToLongBits(1.5));
    }
}
