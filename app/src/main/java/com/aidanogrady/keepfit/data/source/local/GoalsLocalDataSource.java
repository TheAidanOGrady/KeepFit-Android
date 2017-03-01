package com.aidanogrady.keepfit.data.source.local;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;

/**
 * Concrete implementation of the GoalsDataSource as a local SQLite Database.
 *
 * @author Aidan O'Grady
 * @since 0.2.2
 */
public class GoalsLocalDataSource implements GoalsDataSource {
    /**
     * Singleton instance of the data source.
     */
    private static GoalsLocalDataSource sInstance;

    /**
     * The db helper.
     */
    private KeepFitDbHelper mDbHelper;


    /**
     * Constructs a new GoalsLocalDataSource. The constructor is private to ensure singleton is
     * used.
     *
     * @param context the context the source is being created in
     */
    private GoalsLocalDataSource(Context context) {
        mDbHelper = KeepFitDbHelper.getInstance(context);
    }


    /**
     * Returns the singleton instance of the GoalsLocalDataSource.
     *
     * @param context the context the source instance is being requested in
     * @return the singleton instance
     */
    public static GoalsLocalDataSource getInstance(Context context) {
        if (sInstance == null)
            sInstance = new GoalsLocalDataSource(context);
        return sInstance;
    }

    @Override
    public void getGoals(LoadGoalsCallback callback) {

    }

    @Override
    public void getGoal(String id, GetGoalCallback callback) {

    }

    @Override
    public void insertGoal(Goal goal) {

    }

    @Override
    public void updateGoal(Goal goal) {

    }

    @Override
    public void deleteAllGoals() {

    }

    @Override
    public void deleteGoal(String id) {

    }
}
