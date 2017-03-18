package com.aidanogrady.keepfit.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save goals locally.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
class HistoryPersistenceContract {
    /**
     * Private construct to prevent accidental instantiating of the contract class.
     */
    private HistoryPersistenceContract() {}


    static abstract class HistoryEntry implements BaseColumns {
        static final String TABLE_NAME = "history";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_GOAL = "goal";
        static final String COLUMN_NAME_DISTANCE = "distance";
    }


    /**
     * Returns the string that forms the creation of the Goal table.
     *
     * @return create goal table statement
     */
    static String getCreateTable() {
        return "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                HistoryEntry._ID + DbConstants.INTEGER_TYPE + DbConstants.PRIMARY_KEY +
                DbConstants.SEP +
                HistoryEntry.COLUMN_NAME_DATE + DbConstants.INTEGER_TYPE + DbConstants.UNIQUE +
                DbConstants.SEP +
                HistoryEntry.COLUMN_NAME_GOAL + DbConstants.TEXT_TYPE +
                DbConstants.SEP +
                HistoryEntry.COLUMN_NAME_DISTANCE + DbConstants.REAL_TYPE + " )";
    }

    /**
     * Returns the string that forms the drop table statement for the Goal table.
     *
     * @return drop goal table statement
     */
    static String getDropTable() {
        return "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;
    }
}
