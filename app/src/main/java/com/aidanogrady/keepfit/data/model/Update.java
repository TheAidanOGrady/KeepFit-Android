package com.aidanogrady.keepfit.data.model;

import com.aidanogrady.keepfit.data.model.units.Unit;

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
     * The distance for this update.
     */
    private double mDistance;

    /**
     * The unit of measurement of this update.
     */
    private Unit mUnit;


    /**
     * Constructs a new Update with the given date, time and steps.
     *
     * @param date the date of this update
     * @param time the time of this update
     * @param distance the distance of this update
     * @param unit the unit of measurement of this update
     */
    public Update(long date, long time, double distance, Unit unit) {
        this.mDate = date;
        this.mTime = time;
        this.mDistance = distance;
        this.mUnit = unit;
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
     * Returns the distance of this update.
     *
     * @return update distance
     */
    public double getDistance() {
        return mDistance;
    }

    /**
     * Returns the unit of distance for this goal.
     */
    public Unit getUnit() {
        return mUnit;
    }

}
