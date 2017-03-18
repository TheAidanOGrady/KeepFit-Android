package com.aidanogrady.keepfit.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.local.GoalsPersistenceContract.GoalEntry;

import java.util.ArrayList;
import java.util.List;

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
     * The units enum.
     */
    private Unit[] mUnits;


    /**
     * Constructs a new GoalsLocalDataSource. The constructor is private to ensure singleton is
     * used.
     *
     * @param context the context the source is being created in
     */
    private GoalsLocalDataSource(Context context) {
        mDbHelper = KeepFitDbHelper.getInstance(context);
        mUnits = Unit.values();
    }


    /**
     * Returns the singleton instance of the GoalsLocalDataSource.
     *
     * @param context the context the source instance is being requested in
     * @return the singleton instance
     */
    public static GoalsLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GoalsLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void getGoals(LoadGoalsCallback callback) {
        List<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GoalEntry.COLUMN_NAME_ID,
                GoalEntry.COLUMN_NAME_NAME,
                GoalEntry.COLUMN_NAME_DISTANCE,
                GoalEntry.COLUMN_NAME_UNIT,
                GoalEntry.COLUMN_NAME_LAST_ACHIEVED
        };

        Cursor c = db.query(GoalEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String goalId = c.getString(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_ID));
                String name = c.getString(c.getColumnIndex(GoalEntry.COLUMN_NAME_NAME));
                int distance = c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_DISTANCE));
                Unit unit = mUnits[c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_UNIT))];
                int last = c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_LAST_ACHIEVED));
                Goal goal = new Goal(goalId, name, distance, unit, last);
                goals.add(goal);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (goals.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onGoalsLoaded(goals);
        }
    }

    @Override
    public void getGoal(String id, GetGoalCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GoalEntry.COLUMN_NAME_ID,
                GoalEntry.COLUMN_NAME_NAME,
                GoalEntry.COLUMN_NAME_DISTANCE,
                GoalEntry.COLUMN_NAME_UNIT,
                GoalEntry.COLUMN_NAME_LAST_ACHIEVED
        };

        String selection = GoalEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { id };

        Cursor c = db.query(
                GoalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        Goal goal = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String goalId = c.getString(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_ID));
            String name = c.getString(c.getColumnIndex(GoalEntry.COLUMN_NAME_NAME));
            int distance = c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_DISTANCE));
            Unit unit = mUnits[c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_UNIT))];
            int last = c.getInt(c.getColumnIndexOrThrow(GoalEntry.COLUMN_NAME_LAST_ACHIEVED));
            goal = new Goal(goalId, name, distance, unit, last);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (goal == null) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onGoalLoaded(goal);
        }
    }

    @Override
    public void insertGoal(Goal goal) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GoalEntry.COLUMN_NAME_ID, goal.getId());
        values.put(GoalEntry.COLUMN_NAME_NAME, goal.getName());
        values.put(GoalEntry.COLUMN_NAME_DISTANCE, goal.getDistance());
        values.put(GoalEntry.COLUMN_NAME_UNIT, goal.getUnit().ordinal());
        values.put(GoalEntry.COLUMN_NAME_LAST_ACHIEVED, goal.getLastAchieved());

        db.insertOrThrow(GoalEntry.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void updateGoal(Goal goal, String oldId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GoalEntry.COLUMN_NAME_NAME, goal.getName());
        values.put(GoalEntry.COLUMN_NAME_DISTANCE, goal.getDistance());
        values.put(GoalEntry.COLUMN_NAME_UNIT, goal.getUnit().ordinal());
        values.put(GoalEntry.COLUMN_NAME_LAST_ACHIEVED, goal.getLastAchieved());

        String where = GoalEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] whereArgs = { oldId };
        db.update(GoalEntry.TABLE_NAME, values, where, whereArgs);
    }

    @Override
    public void refreshGoals() {
        // Logic handled by repository
    }

    @Override
    public void deleteAllGoals() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(GoalEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void deleteGoal(String id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = GoalEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { id };
        db.delete(GoalEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
