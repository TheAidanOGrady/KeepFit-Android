package com.aidanogrady.keepfit.data.source;

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
}
