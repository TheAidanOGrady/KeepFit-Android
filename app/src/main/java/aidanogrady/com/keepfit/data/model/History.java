package aidanogrady.com.keepfit.data.model;

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
    private int mGoalId;

    /**
     * The number of steps achieved on this day.
     */
    private int mSteps;


    /**
     * Constructs a new History object.
     */
    public History(int date, int goalId, int steps) {
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
    public int getGoalId() {
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
