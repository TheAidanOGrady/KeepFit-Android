package com.aidanogrady.keepfit.data.source;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Update;
import com.aidanogrady.keepfit.data.source.local.UpdatesLocalDataSource;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation to load updates from a data source and store them in a cache.
 *
 * @author Aidan O'Grady
 * @since 0.2.3
 */
public class UpdatesRepository implements UpdatesDataSource {
    /**
     * Singleton instance of the Updates repository.
     */
    private static UpdatesRepository sInstance = null;

    /**
     * The local data source.
     */
    private UpdatesLocalDataSource mUpdatesLocalDataSource;

    /**
     * Cache of Updates obtained from the database.
     */
    private Multimap<Long, Update> mCachedUpdates;

    /**
     * Flag for indicating cache is invalid, to force updates next time data is requested.
     */
    private boolean mCacheIsDirty = false;


    /**
     * Constructs a new UpdatesRepository.
     *
     * @param context the context creating the repository.
     */
    private UpdatesRepository(Context context) {
        mUpdatesLocalDataSource = UpdatesLocalDataSource.getInstance(context);
    }


    /**
     * Returns the singleton instance of the UpdatesRepository.
     *
     * @param context the context the repository is being loaded in.
     * @return singleton instance
     */
    public static UpdatesRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UpdatesRepository(context);
        }
        return sInstance;
    }


    @Override
    public void getUpdates(final LoadUpdatesCallback callback) {
        if (mCachedUpdates != null && !mCacheIsDirty) {
            callback.onUpdatesLoaded(new ArrayList<>(mCachedUpdates.values()));
            return;
        }

        mUpdatesLocalDataSource.getUpdates(new LoadUpdatesCallback() {
            @Override
            public void onUpdatesLoaded(List<Update> updates) {
                refreshCache(updates);
                callback.onUpdatesLoaded(new ArrayList<>(mCachedUpdates.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getUpdatesForDate(final long date, final LoadUpdatesCallback callback) {
        List<Update> updates = getUpdatesWithDate(date);
        if (updates != null) {
            callback.onUpdatesLoaded(updates);
            return;
        }

        mUpdatesLocalDataSource.getUpdatesForDate(date, new LoadUpdatesCallback() {
            @Override
            public void onUpdatesLoaded(List<Update> updates) {
                if (mCachedUpdates == null) {
                    mCachedUpdates = LinkedHashMultimap.create();
                }
                mCachedUpdates.putAll(date, updates);
                callback.onUpdatesLoaded(updates);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void insertUpdate(Update update) {
        mUpdatesLocalDataSource.insertUpdate(update);
        if (mCachedUpdates == null) {
            mCachedUpdates = LinkedHashMultimap.create();
        }
        mCachedUpdates.put(update.getDate(), update);
    }

    @Override
    public void deleteAllUpdates() {
        mUpdatesLocalDataSource.deleteAllUpdates();
        if (mCachedUpdates == null) {
            mCachedUpdates = LinkedHashMultimap.create();
        }
        mCachedUpdates.clear();
    }

    /**
     * Refreshes the cache.
     *
     * @param updates the updates to refresh cache with.
     */
    private void refreshCache(List<Update> updates) {
        if (mCachedUpdates == null) {
            mCachedUpdates = LinkedHashMultimap.create();
        }
        mCachedUpdates.clear();

        for (Update update : updates) {
            mCachedUpdates.put(update.getDate(), update);
        }
    }

    /**
     * Returns the update with the given date from the cache.
     *
     * @param date the being searched for
     * @return update if it is found in cache, otherwise null
     */
    private List<Update> getUpdatesWithDate(long date) {
        if (mCachedUpdates == null || mCachedUpdates.isEmpty()) {
            return null;
        } else {
            List<Update> updates = new ArrayList<>();
            updates.addAll(mCachedUpdates.get(date));
            return updates;
        }
    }
}
