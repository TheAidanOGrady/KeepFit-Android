package com.aidanogrady.keepfit.data.source;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.local.GoalsLocalDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load goals from a data source and store them in a cache.
 *
 * @author Aidan O'Grady
 * @since 0.2.3
 */
public class GoalsRepository implements GoalsDataSource {
    /**
     * Singleton instance of the goals repository.
     */
    private static GoalsRepository sInstance = null;

    /**
     * The local data source.
     */
    private GoalsDataSource mGoalsLocalDataSource;

    /**
     * Cache of goals obtained from the database.
     */
    private Map<String, Goal> mCachedGoals;

    /**
     * Flag for indicating cache is invalid, to force updates next time data is requested.
     */
    private boolean mCacheIsDirty = false;


    /**
     * Constructs a new GoalsRepository.
     *
     * @param context the context creating the repository.
     */
    private GoalsRepository(Context context) {
        mGoalsLocalDataSource = GoalsLocalDataSource.getInstance(context);
    }


    /**
     * Returns the singleton instance of the GoalsRepository.
     *
     * @param context the context the repository is being loaded in.
     * @return singleton instance
     */
    public static GoalsRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GoalsRepository(context);
        }
        return sInstance;
    }

    @Override
    public void getGoals(final LoadGoalsCallback callback) {
        if (mCachedGoals != null && !mCacheIsDirty) {
            callback.onGoalsLoaded(new ArrayList<>(mCachedGoals.values()));
            return;
        }

        mGoalsLocalDataSource.getGoals(new LoadGoalsCallback() {
            @Override
            public void onGoalsLoaded(List<Goal> goals) {
                refreshCache(goals);
                callback.onGoalsLoaded(new ArrayList<>(mCachedGoals.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getGoal(String id, final GetGoalCallback callback) {
        Goal cachedGoal = getGoalWithId(id);
        if (cachedGoal != null) {
            callback.onGoalLoaded(cachedGoal);
            return;
        }

        mGoalsLocalDataSource.getGoal(id, new GetGoalCallback() {
            @Override
            public void onGoalLoaded(Goal goal) {
                if (mCachedGoals == null) {
                    mCachedGoals = new LinkedHashMap<>();
                }
                mCachedGoals.put(goal.getId(), goal);
                callback.onGoalLoaded(goal);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void insertGoal(Goal goal) {
        mGoalsLocalDataSource.insertGoal(goal);
        if (mCachedGoals == null) {
            mCachedGoals = new LinkedHashMap<>();
        }
        mCachedGoals.put(goal.getId(), goal);
    }

    @Override
    public void updateGoal(Goal goal, String oldId) {
        mGoalsLocalDataSource.updateGoal(goal, oldId);
        if (mCachedGoals == null) {
            mCachedGoals = new LinkedHashMap<>();
        }
        mCachedGoals.remove(oldId);
        mCachedGoals.put(goal.getId(), goal);
    }

    @Override
    public void refreshGoals() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllGoals() {
        mGoalsLocalDataSource.deleteAllGoals();
        if (mCachedGoals == null) {
            mCachedGoals = new LinkedHashMap<>();
        }
        mCachedGoals.clear();
    }

    @Override
    public void deleteGoal(String id) {
        mGoalsLocalDataSource.deleteGoal(id);
        mCachedGoals.remove(id);
    }

    /**
     * Refreshes the cache.
     *
     * @param goals the goals to refresh cache with.
     */
    private void refreshCache(List<Goal> goals) {
        if (mCachedGoals == null) {
            mCachedGoals = new LinkedHashMap<>();
        }
        mCachedGoals.clear();

        for (Goal goal : goals) {
            mCachedGoals.put(goal.getId(), goal);
        }
        mCacheIsDirty = false;
    }

    /**
     * Returns the goal with the given id from the cache.
     *
     * @param id the id being searched for
     * @return goal if it is found in cache, otherwise null
     */
    private Goal getGoalWithId(String id) {
        if (mCachedGoals == null || mCachedGoals.isEmpty()) {
            return null;
        } else {
            return mCachedGoals.get(id);
        }
    }
}
