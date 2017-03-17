package com.aidanogrady.keepfit.data.model;

import com.aidanogrady.keepfit.data.model.units.Unit;

import java.util.UUID;

/**
 * A Goal is a certain distance the user is aiming to walk in a single day. Each goal has a unique
 * name to it which acts as a brief description of the goal in question.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class Goal {
    /**
     * The ID of this goal.
     */
    private String mId;

    /**
     * The name of this goal.
     */
    private String mName;

    /**
     * The distance to walk for this goal.
     */
    private int mDistance;

    /**
     * The unit of distance for this goal.
     */
    private Unit mUnit;

    /**
     * The number of days since this goal was last achieved.
     */
    private int mLastAchieved;


    /**
     * Constructs a new Goal. Use this constructor for brand new goals.
     *
     * @param name the name of the goal
     * @param distance then distance required to achieve this goal
     */
    public Goal(String name, int distance, Unit unit) {
        this(UUID.randomUUID().toString(), name, distance, unit,  -1);
    }

    /**
     * Constructs a new Goal. Use this constructor if the Goal already has an ID (is a copy of
     * another Goal).
     *
     * @param id the id of the new goal
     * @param name the name of the new goal
     * @param distance the distance required to achieve the new goal
     */
    public Goal(String id, String name, int distance, Unit unit, int lastAchieved) {
        this.mId = id;
        this.mName = name;
        this.mDistance = distance;
        this.mUnit = unit;
        this.mLastAchieved = lastAchieved;
    }


    /**
     * Returns the ID of this goal.
     *
     * @return id
     */
    public String getId() {
        return mId;
    }

    /**
     * Returns the name of this goal
     *
     * @return name
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the number of steps required to achieve this goal.
     *
     * @return steps
     */
    public int getDistance() {
        return mDistance;
    }

    /**
     * Returns the unit of distance for this goal.
     */
    public Unit getUnit() {
        return mUnit;
    }

    /**
     * Returns the day this was last achieved.
     *
     * @return -1 if goal never achieved, else the day this goal was last achieved.
     */
    public int getLastAchieved() {
        return mLastAchieved;
    }

    @Override
    public String toString() {
        return mName + " (" + mDistance + " steps)";
    }

    /**
     * Converts a given int to a string.
     *
     * @param lastAchieved the date to be converted
     * @return the converted date
     */
    public static String getLastAchievedAsString(int lastAchieved) {
        if (lastAchieved > 0) {
            return lastAchieved + " days ago";
        }
        return "N/A";
    }
}
