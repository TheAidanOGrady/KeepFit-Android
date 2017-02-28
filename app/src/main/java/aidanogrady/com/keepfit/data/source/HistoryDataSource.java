package aidanogrady.com.keepfit.data.source;

import java.util.List;

import aidanogrady.com.keepfit.data.model.History;

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
        void onHistoryLoaded(List<History> history);

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
     * Gets the history with the given ID and enacts the given callback.
     *
     * @param date the date of the history
     * @param callback the callback to enact when history is retrieved.
     */
    void gethistory(int date, GetHistoryCallback callback);

    /**
     * Inserts this history to the data source.
     *
     * @param history the history to be inserted.
     */
    void inserthistory(History history);


    /**
     * Deletes all history from the data source.
     */
    void deleteAllhistorys();

    /**
     * Deletes the history with the given date from the data source.
     *
     * @param date the history to be deleted from data source.
     */
    void deletehistory(int date);
}
