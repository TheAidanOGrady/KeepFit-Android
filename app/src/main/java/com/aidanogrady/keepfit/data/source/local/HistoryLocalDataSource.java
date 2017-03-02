package com.aidanogrady.keepfit.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
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
                HistoryEntry.COLUMN_NAME_STEPS
        };

        Cursor c = db.query(HistoryEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int date = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DATE));
                String goal = c.getString(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_GOAL));
                int steps = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_STEPS));

                History history = new History(date, goal, steps);
                histories.add(history);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (histories.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onHistoryLoaded(histories);
    }

    @Override
    public void getHistory(int date, GetHistoryCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HistoryEntry.COLUMN_NAME_DATE,
                HistoryEntry.COLUMN_NAME_GOAL,
                HistoryEntry.COLUMN_NAME_STEPS
        };

        String selection = HistoryEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(date) };

        Cursor c = db.query(
                HistoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        History history = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int date_ = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_DATE));
            String goal = c.getString(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_GOAL));
            int steps = c.getInt(c.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_STEPS));
            history = new History(date_, goal, steps);
        }

        if (c != null)
            c.close();
        db.close();


        if (history == null)
            callback.onDataNotAvailable();
        else
            callback.onHistoryLoaded(history);
    }

    @Override
    public void insertHistory(History history) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryEntry.COLUMN_NAME_DATE, history.getDate());
        values.put(HistoryEntry.COLUMN_NAME_GOAL, history.getGoalId());
        values.put(HistoryEntry.COLUMN_NAME_STEPS, history.getSteps());

        db.replace(HistoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public void deleteAllHistory() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HistoryEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void deleteHistory(int date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = HistoryEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(date) };
        db.delete(HistoryEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
