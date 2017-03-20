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
     * Returns the ID of the currently active null.
     *
     * @return null if no goal is active, otherwise ID of active goal.
     */
    String getActiveGoalId();

    /**
     * Sets the ID of the currently active goal ot the given value..
     *
     * @param id the ID of the goal set as active.
     */
    void setActiveGoalId(String id);

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
     * Returns the current display distance unit for the history display.
     *
     * @return the current unit to display
     */
    Unit getCurrentHistoryDisplayUnit();

    /**
     * Returns the current date filter for the history display.
     *
     * @return the current filter
     */
    HistoryDateFilter getCurrentHistoryDateFilter();

    /**
     * Returns the start date of the current custom date range filter.
     *
     * @return the start date of the custom period to filter
     */
    long getCurrentHistoryStartDateFilter();

    /**
     * Returns the end date of the current custom date range filter.
     *
     * @return the end date of the custom period to filter
     */
    long getCurrentHistoryEndDateFilter();

    /**
     * Returns the current goal filter for the history display.
     *
     * @return the current filter
     */
    HistoryGoalFilter getCurrentHistoryGoalFilter();

    /**
     * Returns the value set as the filter for goals above or below certain progress.
     *
     * @return progress filter
     */
    double getCurrentHistoryGoalProgressFilter();
}
