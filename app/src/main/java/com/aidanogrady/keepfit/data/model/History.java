package com.aidanogrady.keepfit.data.model;

/**
 * History pertains to a historical record of what was achieved on a particular day. It keeps track
 * of which goal was attempted on a given day, and how many steps were recorded on that day.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class History {
    /**
     * The day this history represents, stored as the number of days since the epoch.
     */
    private int mDate;

    /**
     * The ID of the goal that was attempted on this day.
     */
    private String mGoalId;

    /**
     * The number of steps achieved on this day.
     */
    private int mSteps;


    /**
     * Constructs a new History object.
     *
     * @param date the date of this day in history
     * @param goalId the id of the goal being achieved on this day
     */
    public History(int date, String goalId) {
        this(date, goalId, 0);
    }

    /**
     * Constructs a new History object.
     *
     * @param date the date of this day in history
     * @param goalId the id of the goal being achieved on this day
     * @param steps the number of steps achieved on this day
     */
    public History(int date, String goalId, int steps) {
        this.mDate = date;
        this.mGoalId = goalId;
        this.mSteps = steps;
    }


    /**
     * Returns the date this history item represents.
     *
     * @return date
     */
    public int getDate() {
        return mDate;
    }

    /**
     * Returns the ID of the goal attempted this day.
     *
     * @return goalId
     */
    public String getGoalId() {
        return mGoalId;
    }

    /**
     * Returns the number of steps achieved this day.
     *
     * @return steps
     */
    public int getSteps() {
        return mSteps;
    }
}
