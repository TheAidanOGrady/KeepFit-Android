package com.aidanogrady.keepfit.data.source.local;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;

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
    public void getHistory(int date, GetHistoryCallback callback) {

    }

    @Override
    public void insertHistory(History history) {

    }

    @Override
    public void deleteAllHistory() {

    }

    @Override
    public void deleteHistory(int date) {

    }
}
