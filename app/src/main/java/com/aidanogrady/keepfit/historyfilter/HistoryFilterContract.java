package com.aidanogrady.keepfit.historyfilter;

import com.aidanogrady.keepfit.base.BasePresenter;
import com.aidanogrady.keepfit.base.BaseView;

/**
 * The contract between view and presenter for displaying the History Filter.
 *
 * @author Aidan O'Grady
 * @since 0.10
 */
class HistoryFilterContract {
    /**
     * The view half of the contract.
     */
    interface View extends BaseView<Presenter> {
        /**
         * Sets the potential date filters.
         *
         * @param filters the filters to be displayed
         * @param startIndex the index of the initial item to be displayed
         */
        void setDateFilters(String[] filters, int startIndex);

        /**
         * Sets the date range to the given values.
         *
         * @param start the start date in epoch days
         * @param end the end day in epoch days
         */
        void setDateRange(long start, long end);

        /**
         * Sets the potential goal filters.
         *
         * @param filters the filters to be displayed
         * @param startIndex the index of the initial item to be displayed
         */
        void setGoalFilters(String[] filters, int startIndex);

        /**
         * Sets the goal progress to the given percentage.
         *
         * @param percentage the percentage value to be set
         */
        void setGoalProgress(double percentage);

        /**
         * Finishes the activity and re-displays history.
         */
        void showHistory();

        /**
         * Displays message indicating invalid date range.
         */
        void showInvalidRangeError();

        /**
         * Returns whether or not view is active.
         *
         * @return view is active
         */
        boolean isActive();
    }


    /**
     * The view half of the contract.
     */
    interface Presenter extends BasePresenter {
        /**
         * Populates the filters with their current currents.
         */
        void populateFilters();

        /**
         * Saves the current filters.
         *
         * @param goal the goal filter
         * @param percentage the percentage chosen
         * @param date the date filter chosen
         * @param start the start date
         * @param end the end date
         */
        void saveFilters(String date, long start, long end, String goal, double percentage);
    }
}
