package com.aidanogrady.keepfit.data.source;

import com.aidanogrady.keepfit.data.model.History;

import java.util.List;

/**
 * Interface for accessing history data.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public interface HistoryDataSource extends DataSource {
    /**
     * Interface for the callback when all history loaded or unavailable.
     */
    interface LoadHistoryCallback {
        void onHistoryLoaded(List<History> histories);

        void onDataNotAvailable();
    }

    /**
     * Interface for the callback when a history is loaded or is unavailable.
     */
    interface GetHistoryCallback {
        void onHistoryLoaded(History history);

        void onDataNotAvailable();
    }


    /**
     * Gets all history from the data source..
     *
     * @param callback the callback to enact when history is retrieved.
     */
    void getHistory(LoadHistoryCallback callback);

    /**
     * Gets the history with the given ID and enacts the given callback.
     *
     * @param date the date of the history
     * @param callback the callback to enact when history is retrieved.
     */
    void getHistory(int date, GetHistoryCallback callback);

    /**
     * Inserts this history to the data source.
     *
     * @param history the history to be inserted.
     */
    void insertHistory(History history);

    /**
     * Refreshes the data source.
     */
    void refreshHistory();

    /**
     * Deletes all history from the data source.
     */
    void deleteAllHistory();

    /**
     * Deletes the history with the given date from the data source.
     *
     * @param date the history to be deleted from data source.
     */
    void deleteHistory(int date);
}
