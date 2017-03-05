package com.aidanogrady.keepfit.data.source;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.source.local.HistoryLocalDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load history from a data source and store them in a cache.
 *
 * @author Aidan O'Grady
 * @since 0.2.3
 */
public class HistoryRepository implements HistoryDataSource {
    /**
     * Singleton instance of the history repository.
     */
    private static HistoryRepository sInstance = null;

    /**
     * The local data source.
     */
    private HistoryDataSource mHistoryLocalDataSource;

    /**
     * Cache of History obtained from the database.
     */
    private Map<Integer, History> mCachedHistory;

    /**
     * Flag for indicating cache is invalid, to force updates next time data is requested.
     */
    private boolean mCacheIsDirty = false;


    /**
     * Constructs a new HistoryRepository.
     *
     * @param context the context creating the repository.
     */
    private HistoryRepository(Context context) {
        mHistoryLocalDataSource = HistoryLocalDataSource.getInstance(context);
    }


    /**
     * Returns the singleton instance of the HistoryRepository.
     *
     * @param context the context the repository is being loaded in.
     * @return singleton instance
     */
    public static HistoryRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HistoryRepository(context);
        }
        return sInstance;
    }

    @Override
    public void getHistory(final LoadHistoryCallback callback) {
        if (mCachedHistory != null && !mCacheIsDirty) {
            callback.onHistoryLoaded(new ArrayList<>(mCachedHistory.values()));
            return;
        }

        mHistoryLocalDataSource.getHistory(new LoadHistoryCallback() {
            @Override
            public void onHistoryLoaded(List<History> histories) {
                refreshCache(histories);
                callback.onHistoryLoaded(new ArrayList<>(mCachedHistory.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getHistory(int date, final GetHistoryCallback callback) {
        History history = getHistoryWithDate(date);
        if (history != null) {
            callback.onHistoryLoaded(history);
            return;
        }

        mHistoryLocalDataSource.getHistory(date, new GetHistoryCallback() {
            @Override
            public void onHistoryLoaded(History history) {
                if (mCachedHistory == null) {
                    mCachedHistory = new LinkedHashMap<>();
                }
                mCachedHistory.put(history.getDate(), history);
                callback.onHistoryLoaded(history);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void insertHistory(History history) {
        mHistoryLocalDataSource.insertHistory(history);
        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.put(history.getDate(), history);
    }

    @Override
    public void refreshHistory() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllHistory() {
        mHistoryLocalDataSource.deleteAllHistory();
        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.clear();
    }

    @Override
    public void deleteHistory(int date) {
        mHistoryLocalDataSource.deleteHistory(date);
        mCachedHistory.remove(date);
    }

    /**
     * Refreshes the cache.
     *
     * @param histories the history to refresh cache with.
     */
    private void refreshCache(List<History> histories) {
        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.clear();

        for (History history : histories) {
            mCachedHistory.put(history.getDate(), history);
        }
        mCacheIsDirty = false;
    }

    /**
     * Returns the history with the given date from the cache.
     *
     * @param date the date being searched for
     * @return history if it is found in cache, otherwise null
     */
    private History getHistoryWithDate(int date) {
        if (mCachedHistory == null || mCachedHistory.isEmpty()) {
            return null;
        } else {
            return mCachedHistory.get(date);
        }
    }
}
