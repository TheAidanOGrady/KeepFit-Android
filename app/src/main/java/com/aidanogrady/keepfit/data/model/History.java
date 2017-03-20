package com.aidanogrady.keepfit.data.model;

import java.util.ArrayList;
import java.util.List;

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
    private long mDate;

    /**
     * The goal attempted on this date.
     */
    private Goal mGoal;

    /**
     * The distance achieved on this day.
     */
    private double mDistance;

    /**
     * A list of updates entered on this date in history.
     */
    private List<Update> mUpdates;


    /**
     * Constructs a new History object with no goal being worked towards.
     *
     * @param date the date of this day in history
     */
    public History(long date) {
        this(date, null, 0, null);
    }

    /**
     * Constructs a new History object.
     *
     * @param date the date of this day in history
     * @param goal the goal being achieved on this day
     */
    public History(long date, Goal goal) {
        this(date, goal, 0, null);
    }

    /**
     * Constructs a new History object.
     *
     * @param date the date of this day in history
     * @param goal the goal being achieved on this day
     * @param distance the distance achieved on this day
     */
    public History(long date, Goal goal, double distance, List<Update> updates) {
        this.mDate = date;
        this.mGoal = goal;
        this.mDistance = distance;
        if (updates == null)
            mUpdates = new ArrayList<>();
        else
            mUpdates = updates;
    }


    /**
     * Returns the date this history item represents.
     *
     * @return date
     */
    public long getDate() {
        return mDate;
    }

    /**
     * Returns the ID of the goal attempted this day.
     *
     * @return goalId
     */
    public Goal getGoal() {
        return mGoal;
    }

    /**
     * Sets the goal of this history to the given goal.
     *
     * @param goal the goal achieved on this day in history
     */
    public void setGoal(Goal goal) {
        this.mGoal = goal;
    }

    /**
     * Returns the distance achieved this day.
     *
     * @return distance
     */
    public double getDistance() {
        return mDistance;
    }

    /**
     * Sets the distance to the given number of steps.
     *
     * @param distance the distance achieved on this day
     */
    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    /**
     * Returns the percentage of goal completion.
     *
     * @return the percentage progress of this history.
     */
    public double getPercentage() {
        if (mGoal == null) {
            return -1;
        }
        return mDistance * 100 / mGoal.getDistance();
    }

    /**
     * Returns the list of updates of this given day.
     *
     * @return updates of this day.
     */
    public List<Update> getUpdates() {
        return mUpdates;
    }

    /**
     * Adds an update to this day.
     *
     * @param update the update to be added
     */
    public void addUpdate(Update update) {
        mUpdates.add(update);
    }
}
