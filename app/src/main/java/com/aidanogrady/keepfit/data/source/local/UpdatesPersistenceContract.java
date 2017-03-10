package com.aidanogrady.keepfit.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save updates locally.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
class UpdatesPersistenceContract {
    /**
     * Private construct to prevent accidental instantiating of the contract class.
     */
    private UpdatesPersistenceContract() {}


    static abstract class UpdateEntry implements BaseColumns {
        static final String TABLE_NAME = "updates";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_TIME = "time";
        static final String COLUMN_NAME_STEPS = "steps";
    }


    /**
     * Returns the string that forms the creation of the Update table.
     *
     * @return create goal table statement
     */
    static String getCreateTable() {
        return "CREATE TABLE " + UpdateEntry.TABLE_NAME + " (" +
                UpdateEntry._ID + DbConstants.INTEGER_TYPE + DbConstants.PRIMARY_KEY +
                DbConstants.SEP +
                UpdateEntry.COLUMN_NAME_DATE + DbConstants.INTEGER_TYPE +
                DbConstants.SEP +
                UpdateEntry.COLUMN_NAME_TIME + DbConstants.INTEGER_TYPE +
                DbConstants.SEP +
                UpdateEntry.COLUMN_NAME_STEPS + DbConstants.INTEGER_TYPE + " )";
    }

    /**
     * Returns the string that forms the drop table statement for the update table.
     *
     * @return drop goal table statement
     */
    static String getDropTable() {
        return "DROP TABLE IF EXISTS " + UpdateEntry.TABLE_NAME;
    }
}
