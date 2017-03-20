package com.aidanogrady.keepfit.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.model.Update;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
import com.aidanogrady.keepfit.data.source.UpdatesDataSource;
import com.aidanogrady.keepfit.data.source.local.HistoryPersistenceContract.HistoryEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the HistoryDataSource as a local SQLite Database.
 *
 * @author Aidan O'Grady
 * @since 0.2.2
 */
public class HistoryLocalDataSource implements HistoryDataSource {
    /**
     * Singleton instance of the data source.
     */
    private static HistoryLocalDataSource sInstance;

    /**
     * The data source for goals.
     */
    private GoalsDataSource mGoalsDataSource;

    /**
     * The data source for updates, used to retrieve updates of a given day.
     */
    private UpdatesDataSource mUpdatesDataSource;

    /**
     * The db helper.
     */
    private KeepFitDbHelper mDbHelper;


    /**
     * Constructs a new HistoryLocalDataSource. The constructor is private to
     * ensure singleton is used.
     *
     * @param context the context the source is being created in
     */
    private HistoryLocalDataSource(Context context) {
        mDbHelper = KeepFitDbHelper.getInstance(context);
        mGoalsDataSource = GoalsLocalDataSource.getInstance(context);
        mUpdatesDataSource = UpdatesLocalDataSource.getInstance(context);
    }


    /**
     * Returns the singleton instance of the HistoryLocalDataSource.
     *
     * @param context the context the source instance is being requested in
     * @return the singleton instance
     */
    public static HistoryLocalDataSource getInstance(Context context) {
        if (sInstance == null)
            sInstance = new HistoryLocalDataSource(context);
        return sInstance;
    }

    @Override
    public void getHistory(LoadHistoryCallback callback) {
        List<History> histories = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HistoryEntry.COLUMN_NAME_DATE,
                HistoryEntry.COLUMN_NAME_GOAL,
                HistoryEntry.COLUMN_NAME_DISTANCE
        };

        Cursor c = db.query(HistoryEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {

                String goalId = c.getString(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_GOAL));
                int date = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DATE));
                int steps = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DISTANCE));
                Goal goal = getGoalWithId(goalId);
                List<Update> updates = getUpdatesWithDate(date);

                History history = new History(date, goal, steps, updates);
                histories.add(history);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (histories.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onHistoryLoaded(histories);
        }
    }

    @Override
    public void getHistory(long date, GetHistoryCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HistoryEntry.COLUMN_NAME_DATE,
                HistoryEntry.COLUMN_NAME_GOAL,
                HistoryEntry.COLUMN_NAME_DISTANCE
        };

        String selection = HistoryEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(date) };

        Cursor c = db.query(
                HistoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        History history = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String goalId = c.getString(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_GOAL));
            Goal goal = getGoalWithId(goalId);
            int date_ = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DATE));
            int steps = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DISTANCE));
            List<Update> updates = getUpdatesWithDate(date_);

            history = new History(date, goal, steps, updates);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (history == null) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onHistoryLoaded(history);
        }
    }

    @Override
    public void insertHistory(History history) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryEntry.COLUMN_NAME_DATE, history.getDate());
        values.put(HistoryEntry.COLUMN_NAME_GOAL, history.getGoal().getId());
        values.put(HistoryEntry.COLUMN_NAME_DISTANCE, history.getDistance());

        db.replace(HistoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public void refreshHistory() {
        // No need to do anything, Repository handles this.
    }

    @Override
    public void deleteAllHistory() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HistoryEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void deleteHistory(long date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = HistoryEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(date) };
        db.delete(HistoryEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    /**
     * Returns the goal with the given ID from the goals local source.
     *
     * @param id the id of the goal being searched for
     * @return goal if exists, otherwise null
     */
    private Goal getGoalWithId(String id) {
        final Goal[] goals = new Goal[1]; // Final workaround
        mGoalsDataSource.getGoal(id, true, new GoalsDataSource.GetGoalCallback() {
            @Override
            public void onGoalLoaded(Goal goal) {
                goals[0] = goal;
            }

            @Override
            public void onDataNotAvailable() {
                goals[0] = null;
            }
        });

        return goals[0];
    }

    /**
     * Returns a list of updates from the given date.
     *
     * @param date the date being searched for
     * @return list of updates with that date, empty list if none exist
     */
    private List<Update> getUpdatesWithDate(long date) {
        final List<Update> fUpdates = new ArrayList<>();
        mUpdatesDataSource.getUpdatesForDate(date, new UpdatesDataSource.LoadUpdatesCallback() {
            @Override
            public void onUpdatesLoaded(List<Update> updates) {
                fUpdates.addAll(updates);
            }

            @Override
            public void onDataNotAvailable() {
                fUpdates.clear();
            }
        });
        return fUpdates;
    }
}
