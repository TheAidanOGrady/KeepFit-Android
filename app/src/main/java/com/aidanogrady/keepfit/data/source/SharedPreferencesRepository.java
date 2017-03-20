package com.aidanogrady.keepfit.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.aidanogrady.keepfit.data.model.HistoryDateFilter;
import com.aidanogrady.keepfit.data.model.HistoryGoalFilter;
import com.aidanogrady.keepfit.data.model.units.Unit;

import org.threeten.bp.LocalDate;

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
     * @param listeners  the listeners to be registered.
     */
    private SharedPreferencesRepository(Context context,
                                        OnSharedPreferenceChangeListener[] listeners) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (OnSharedPreferenceChangeListener listener: listeners) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }
    }


    /**
     * Makes a new instance of the singleton.
     *
     * @param context the context
     * @param listeners the listeners for the shared preferences
     */
    public static void makeInstance(Context context, OnSharedPreferenceChangeListener[] listeners) {
        sInstance = new SharedPreferencesRepository(context, listeners);
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

    /**
     * Returns the display unit for history.
     *
     * @return display unit
     */
    public static Unit getHistoryDisplayUnit() {
        return sInstance.getCurrentHistoryDisplayUnit();
    }

    /**
     * Returns the stored history date filter.
     *
     * @return date filter
     */
    public static HistoryDateFilter getHistoryDateFilter() {
        return sInstance.getCurrentHistoryDateFilter();
    }

    /**
     * Returns the stored history goal filter.
     *
     * @return goal  filter
     */
    public static HistoryGoalFilter getHistoryGoalFilter() {
        return sInstance.getCurrentHistoryGoalFilter();
    }

    /**
     * Returns the stored history goal progress.
     *
     * @return goal progress
     */
    public static double getHistoryGoalProgressFilter() {
        return sInstance.getCurrentHistoryGoalProgressFilter();
    }

    /**
     * Returns the start date of the history dates filter.
     *
     * @return start date
     */
    public static long getHistoryStartDateFilter() {
        return sInstance.getCurrentHistoryStartDateFilter();
    }

    /**
     * Returns the end date of the history dates filter.
     *
     * @return end date
     */
    public static long getHistoryEndDateFilter() {
        return sInstance.getCurrentHistoryEndDateFilter();
    }

    @Override
    public boolean getIsEditGoalEnabled() {
        return mSharedPreferences.getBoolean("editGoalsEnabled", true);
    }

    @Override
    public boolean getIsTestModeEnabled() {
        return mSharedPreferences.getBoolean("testModeEnabled", false);
    }

    @Override
    public long getTestDate() {
        return mSharedPreferences.getLong("testModeDate", LocalDate.now().toEpochDay());
    }

    @Override
    public Unit getCurrentHistoryDisplayUnit() {
        String value = mSharedPreferences.getString("historyDisplayUnit", "DEFAULT");
        return Unit.valueOf(value);
    }

    @Override
    public double getStepsToMetres() {
        String value = mSharedPreferences.getString("stepsPerMetre", "1.5");
        return Double.valueOf(value);
    }

    @Override
    public HistoryDateFilter getCurrentHistoryDateFilter() {
        String value = mSharedPreferences.getString("historyDateFilter", "NONE");
        return HistoryDateFilter.valueOf(value);
    }

    @Override
    public long getCurrentHistoryStartDateFilter() {
        LocalDate now = LocalDate.now();
        long defaultValue = now.minusDays(now.getDayOfMonth()).toEpochDay();
        return mSharedPreferences.getLong("historyStartDateFilter", defaultValue);
    }

    @Override
    public long getCurrentHistoryEndDateFilter() {
        return mSharedPreferences.getLong("historyEndDateFilter", LocalDate.now().toEpochDay());
    }

    @Override
    public HistoryGoalFilter getCurrentHistoryGoalFilter() {
        String value = mSharedPreferences.getString("historyGoalFilter", "NONE");
        return HistoryGoalFilter.valueOf(value);
    }

    @Override
    public double getCurrentHistoryGoalProgressFilter() {
        String value = mSharedPreferences.getString("historyGoalProgressFilter", "0");
        return Double.valueOf(value);
    }
}
