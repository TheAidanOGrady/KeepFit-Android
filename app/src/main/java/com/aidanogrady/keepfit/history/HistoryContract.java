package com.aidanogrady.keepfit.history;

import com.aidanogrady.keepfit.base.BasePresenter;
import com.aidanogrady.keepfit.base.BaseView;
import com.aidanogrady.keepfit.data.model.History;

import java.util.List;

/**
 * The HistoryContract defines a specification between view and presenter of the app history.
 *
 * @author Aidan O'Grady
 * @since 0.4
 */
class HistoryContract {
    /**
     * The view half of the contract.
     */
    interface View extends BaseView<Presenter> {
        /**
         * Shows the given list of history.
         *
         * @param history the history to be shown
         */
        void showHistory(List<History> history);

        /**
         * Shows view for when there is no history.
         */
        void showNoHistory();

        /**
         * Show the error indicating that loading history encountered an error.
         */
        void showLoadingHistoryError();

        /**
         * Returns whether or not the view is active.
         *
         * @return true if view is active, otherwise false
         */
        boolean isActive();
    }

    /**
     * The presenter half of the contract.
     */
    interface Presenter extends BasePresenter {
        /**
         * Loads history to the presenter.
         *
         * @param forceUpdate whether or not update should be forced.
         */
        void loadHistory(boolean forceUpdate);
    }
}
