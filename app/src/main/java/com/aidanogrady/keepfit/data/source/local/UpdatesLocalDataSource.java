package com.aidanogrady.keepfit.data.source.local;

import android.content.Context;

import com.aidanogrady.keepfit.data.source.UpdatesDataSource;

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
     * Constructs a new UpdatesLocalDataSource. The constructor is private to
     * ensure singleton is used.
     *
     * @param context the context the source is being created in
     */
    private UpdatesLocalDataSource(Context context) {
        mDbHelper = KeepFitDbHelper.getInstance(context);
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

    }

    @Override
    public void getUpdatesForDate(int date, LoadUpdatesCallback callback) {

    }

    @Override
    public void deleteAllUpdates() {

    }
}
