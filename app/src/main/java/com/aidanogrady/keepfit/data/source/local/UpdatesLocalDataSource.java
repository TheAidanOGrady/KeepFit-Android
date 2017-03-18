package com.aidanogrady.keepfit.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aidanogrady.keepfit.data.model.Update;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.source.UpdatesDataSource;
import com.aidanogrady.keepfit.data.source.local.UpdatesPersistenceContract.UpdateEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the UpdatesDataSource as a local SQLite Database.
 *
 * @author Aidan O'Grady
 * @since 0.2.2
 */
public class UpdatesLocalDataSource implements UpdatesDataSource {
    /**
     * Singleton instance of the data source.
     */
    private static UpdatesLocalDataSource sInstance;

    /**
     * The db helper.
     */
    private KeepFitDbHelper mDbHelper;

    /**
     * The units enum.
     */
    private Unit[] mUnits;


    /**
     * Constructs a new UpdatesLocalDataSource. The constructor is private to
     * ensure singleton is used.
     *
     * @param context the context the source is being created in
     */
    private UpdatesLocalDataSource(Context context) {
        mDbHelper = KeepFitDbHelper.getInstance(context);
        mUnits = Unit.values();
    }


    /**
     * Returns the singleton instance of the UpdatesLocalDataSource.
     *
     * @param context the context the source instance is being requested in
     * @return the singleton instance
     */
    public static UpdatesLocalDataSource getInstance(Context context) {
        if (sInstance == null)
            sInstance = new UpdatesLocalDataSource(context);
        return sInstance;
    }

    @Override
    public void getUpdates(LoadUpdatesCallback callback) {
        List<Update> updates = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                UpdateEntry.COLUMN_NAME_DATE,
                UpdateEntry.COLUMN_NAME_TIME,
                UpdateEntry.COLUMN_NAME_DISTANCE,
                UpdateEntry.COLUMN_NAME_UNIT
        };

        Cursor c = db.query(UpdateEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int date_ = c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_DATE));
                long time = c.getLong(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_TIME));
                int steps = c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_DISTANCE));
                Unit unit = mUnits[c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_UNIT))];

                Update update = new Update(date_, time, steps, unit);
                updates.add(update);
            }
        }

        if (c != null)
            c.close();
        db.close();

        if (updates.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onUpdatesLoaded(updates);
        }
    }

    @Override
    public void getUpdatesForDate(long date, LoadUpdatesCallback callback) {
        List<Update> updates = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                UpdateEntry.COLUMN_NAME_DATE,
                UpdateEntry.COLUMN_NAME_TIME,
                UpdateEntry.COLUMN_NAME_DISTANCE
        };

        String selection = UpdateEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(date) };

        Cursor c = db.query(
                UpdateEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int date_ = c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_DATE));
                long time = c.getLong(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_TIME));
                int steps = c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_DISTANCE));
                Unit unit = mUnits[c.getInt(c.getColumnIndexOrThrow(UpdateEntry.COLUMN_NAME_UNIT))];

                Update update = new Update(date_, time, steps, unit);
                updates.add(update);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (updates.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onUpdatesLoaded(updates);
        }
    }

    @Override
    public void insertUpdate(Update update) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(UpdateEntry.COLUMN_NAME_DATE, update.getDate());
        values.put(UpdateEntry.COLUMN_NAME_TIME, update.getTime());
        values.put(UpdateEntry.COLUMN_NAME_DISTANCE, update.getDistance());

        db.replace(UpdateEntry.TABLE_NAME, null, values);
    }

    @Override
    public void deleteAllUpdates() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(UpdateEntry.TABLE_NAME, null, null);
        db.close();
    }
}
