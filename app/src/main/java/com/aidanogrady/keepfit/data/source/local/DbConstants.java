package com.aidanogrady.keepfit.data.source.local;

/**
 * The Class for containing constants related to the SqLite database types.
 *
 * @author Aidan O'Grady
 * @since 0.2.1
 */
public class DbConstants {
    /**
     * The string used for indicating the column is of Text type.
     */
    public static final String TEXT_TYPE = " TEXT";

    /**
     * The string used for indicating the column is of Integer type.
     */
    public static final String INTEGER_TYPE = " INTEGER";

    /**
     * Comma separator.
     */
    public static final String SEP = ",";

    /**
     * String denoting that a column is a primary key. Autoincrement is always used.
     */
    public static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT,";

    /**
     * String denoting that th column is unique.
     */
    public static final String UNIQUE = " UNIQUE";
}
