package com.aidanogrady.keepfit.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The KeepFitDbHelper defines the management of the local SQLite database maintained in the device
 * storage. The creation of, upgrading of and deletion of tables is defined here.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
class KeepFitDbHelper extends SQLiteOpenHelper {
    /**
     * The current version of the database.
     */
    private static final int DATABASE_VERSION = 3;

    /**
     * The name of the database.
     */
    private static final String DATABASE_NAME = "KeepFit.db";

    /**
     * The singleton instance of the DbHelper.
     */
    private static KeepFitDbHelper sInstance;


    /**
     * Constructs a new KeepFitDbHelper
     *
     * @param context the context to create the table in.
     */
    private KeepFitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Returns the singleton instance of the KeepFitDbHelper. If the helper does not yet exist, then
     * a new one is instantiated.
     *
     * @param context the context being request
     * @return singleton db helper
     */
    static KeepFitDbHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new KeepFitDbHelper(context.getApplicationContext());
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(GoalsPersistenceContract.getCreateTable());
        db.execSQL(GoalsPersistenceContract.getCreateTable());
        System.out.println(HistoryPersistenceContract.getCreateTable());
        db.execSQL(HistoryPersistenceContract.getCreateTable());
        db.execSQL(UpdatesPersistenceContract.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(GoalsPersistenceContract.getDropTable());
            db.execSQL(HistoryPersistenceContract.getDropTable());
            db.execSQL(UpdatesPersistenceContract.getDropTable());
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
