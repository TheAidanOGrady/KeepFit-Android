package com.aidanogrady.keepfit.data.model;

/**
 * The HistoryDateFilter contains all the various types of date filtering available. The user is
 * able to filter between the last week, month and a history range.
 *
 * @author Aidan O'Grady
 * @since 0.9
 */
public enum HistoryDateFilter {
    NONE,
    WEEK,
    MONTH,
    CUSTOM
}
