package com.aidanogrady.keepfit.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save goals locally.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
class GoalsPersistenceContract {
    /**
     * Private construct to prevent accidental instantiating of the contract class.
     */
    private GoalsPersistenceContract() {}


    static abstract class GoalEntry implements BaseColumns {
        static final String TABLE_NAME = "goal";
        static final String COLUMN_NAME_ID = "uuid";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_STEPS = "steps";
        static final String COLUMN_NAME_LAST_ACHIEVED = "last_achieved";
    }


    /**
     * Returns the string that forms the creation of the Goal table.
     *
     * @return create goal table statement
     */
    static String getCreateTable() {
        return "CREATE TABLE " + GoalEntry.TABLE_NAME + " (" +
                GoalEntry._ID + DbConstants.INTEGER_TYPE + DbConstants.PRIMARY_KEY +
                DbConstants.SEP +
                GoalEntry.COLUMN_NAME_ID + DbConstants.TEXT_TYPE + DbConstants.UNIQUE +
                DbConstants.SEP +
                GoalEntry.COLUMN_NAME_NAME + DbConstants.TEXT_TYPE + DbConstants.UNIQUE +
                DbConstants.SEP +
                GoalEntry.COLUMN_NAME_STEPS + DbConstants.INTEGER_TYPE +
                DbConstants.SEP +
                GoalEntry.COLUMN_NAME_LAST_ACHIEVED + DbConstants.INTEGER_TYPE + " )";
    }

    /**
     * Returns the string that forms the drop table statement for the Goal table.
     *
     * @return drop goal table statement
     */
    static String getDropTable() {
        return "DROP TABLE IF EXITS " + GoalEntry.TABLE_NAME;
    }
}
