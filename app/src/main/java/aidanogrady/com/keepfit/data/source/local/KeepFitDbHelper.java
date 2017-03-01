package aidanogrady.com.keepfit.data.source.local;

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
public class KeepFitDbHelper extends SQLiteOpenHelper {
    /**
     * The current version of the database.
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * The name of the database.
     */
    public static final String DATABASE_NAME = "KeepFit.db";

    /**
     * Constructs a new KeepFitDbHelper
     *
     * @param context the context to create the table in.
     */
    public KeepFitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GoalsPersistenceContract.getCreateTable());
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
