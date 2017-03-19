package com.aidanogrady.keepfit.data.source;

import com.aidanogrady.keepfit.data.model.HistoryDateFilter;
import com.aidanogrady.keepfit.data.model.HistoryGoalFilter;
import com.aidanogrady.keepfit.data.model.units.Unit;

/**
 * The PreferenceRepository is a repository for the various settings of the KeepFit application. The
 * interface allows presenters to have no awareness of the SharedPreferences Android API, ensuring
 * separation in MVP.
 *
 * @author Aidan O'Grady
 * @since 0.8
 */
public interface PreferenceRepository {
    /**
     * Returns true if editing goals has been enabled.
     *
     * @return true if goals can be edited, otherwise false
     */
    boolean getIsEditGoalEnabled();

    /**
     * Returns true if test mode is enabled.
     *
     * @return true if enabled, otherwise false
     */
    boolean getIsTestModeEnabled();

    /**
     * Returns the date of test mode.
     *
     * @return the date of test mode
     */
    long getTestDate();

    /**
     * Returns the exchange from steps to metres.
     *
     * @return exchange from steps to metres
     */
    double getStepsToMetres();

    /**
     * Returns the current date filter for the history display.
     *
     * @return the current filter
     */
    HistoryDateFilter getCurrentHistoryDateFilter();

    /**
     * Sets the current history date filter to the given value.
     *
     * @param historyDateFilter the new filter
     */
    void setCurrentHistoryDateFilter(HistoryDateFilter historyDateFilter);

    /**
     * Returns the current goal filter for the history display.
     *
     * @return the current filter
     */
    HistoryGoalFilter getCurrentHistoryGoalFilter();

    /**
     * Sets the current history goal filter to the given value.
     *
     * @param historyGoalFilter the new filter
     */
    void setCurrentHistoryGoalFilter(HistoryGoalFilter historyGoalFilter);

    /**
     * Returns the value set as the filter for goals above or below certain progress.
     *
     * @return progress filter
     */
    double getCurrentHistoryGoalProgressFilter();

    /**
     * Sets the value of the filter for goals above or below certain progress.
     *
     * @param historyGoalProgressFilter the new progress filter
     */
    void setCurrentHistoryGoalProgressFilter(double historyGoalProgressFilter);


    /**
     * Returns the current display distance unit for the history display.
     *
     * @return the current unit to display
     */
    Unit getCurrentHistoryDisplayUnit();

    /**
     * Sets the current history display unit to the given value.
     *
     * @param unit the new
     */
    void setCurrentHistoryDisplayUnit(Unit unit);
}
