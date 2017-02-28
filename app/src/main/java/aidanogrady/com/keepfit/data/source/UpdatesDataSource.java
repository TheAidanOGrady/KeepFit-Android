package aidanogrady.com.keepfit.data.source;

import java.util.List;

import aidanogrady.com.keepfit.data.model.Update;

/**
 * Interface for accessing updates.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public interface UpdatesDataSource extends DataSource {

    /**
     * Interface for the callback when an update are loaded or unavailable.
     */
    interface LoadUpdatesCallback {
        void onUpdatesLoaded(List<Update> update);

        void onDataNotAvailable();
    }

    /**
     * Interface for the callback when updates are loaded or is unavailable.
     */
    interface GetUpdateCallback {
        void onUpdateLoaded(Update update);

        void onDataNotAvailable();
    }


    /**
     * Gets all updates from the data source.
     *
     * @param callback the callback to use when data retrieved.
     */
    void getUpdates(LoadUpdatesCallback callback);

    /**
     * Gets all updates with the given date from the data source.
     *
     * @param date the date to get updates for
     * @param callback the callback to use when data retrieved.
     */
    void getUpdatesForDate(int date, LoadUpdatesCallback callback);

    /**
     * Removes all updates from the data source.
     */
    void deleteAllUpdates();
}
