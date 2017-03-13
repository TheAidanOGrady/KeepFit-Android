package com.aidanogrady.keepfit.data.model;

/**
 * An update denotes a timestamp and the number of steps the user manually recorded at this given
 * time. The date and time are recorded separately for easy retrieval of all updates of a given
 * day. The updates have no awareness regarding the goal being attempte on this day.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class Update {
    /**
     * The date of the update.
     */
    private long mDate;

    /**
     * The time of the update.
     */
    private long mTime;

    /**
     * The number of steps for this update.
     */
    private int mSteps;


    /**
     * Constructs a new Update with the given date, time and steps.
     *
     * @param date the date of this update
     * @param time the time of this update
     * @param steps the number of steps of this update
     */
    public Update(long date, long time, int steps) {
        this.mDate = date;
        this.mTime = time;
        this.mSteps = steps;
    }


    /**
     * Returns the date of this update.
     *
     * @return the epoch days of this update
     */
    public long getDate() {
        return mDate;
    }

    /**
     * Returns the time of this update.
     *
     * @return update time
     */
    public long getTime() {
        return mTime;
    }

    /**
     * Returns the number of steps of this update.
     *
     * @return update steps
     */
    public int getSteps() {
        return mSteps;
    }
}
