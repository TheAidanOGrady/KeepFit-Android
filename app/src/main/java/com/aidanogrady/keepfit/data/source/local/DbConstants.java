package com.aidanogrady.keepfit.data.source.local;

/**
 * The Class for containing constants related to the SqLite database types.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
class DbConstants {
    /**
     * The string used for indicating the column is of Text type.
     */
    static final String TEXT_TYPE = " TEXT";

    /**
     * The string used for indicating the column is of Integer type.
     */
    static final String INTEGER_TYPE = " INTEGER";

    /**
     * Comma separator.
     */
    static final String SEP = ",";

    /**
     * String denoting that a column is a primary key. Autoincrement is always used.
     */
    static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";

    /**
     * String denoting that th column is unique.
     */
    static final String UNIQUE = " UNIQUE";
}
