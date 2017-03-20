package com.aidanogrady.keepfit.goals;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;

import java.util.List;

/**
 * The GoalsPresenter responds to user actions from the UI and retrieves data to update the UI with.
 *
 * @author Aidan O'Grady
 * @since 0.3
 */
public class GoalsPresenter implements GoalsContract.Presenter {
    /**
     * The goals repository to retrieve goals from.
     */
    private final GoalsRepository mGoalsRepository;

    /**
     * The goals view.
     */
    private final GoalsContract.View mGoalsView;


    public GoalsPresenter(Context context, GoalsContract.View goalsView) {
        this.mGoalsRepository = GoalsRepository.getInstance(context);
        this.mGoalsView = goalsView;
        this.mGoalsView.setPresenter(this);
    }


    @Override
    public void start() {
        loadGoals(true);
    }

    @Override
    public void loadGoals(boolean forceUpdate) {
        if (forceUpdate) {
            mGoalsRepository.refreshGoals();
        }

        mGoalsRepository.getGoals(new GoalsDataSource.LoadGoalsCallback() {
            @Override
            public void onGoalsLoaded(List<Goal> goals) {
                if (goals.isEmpty()) {
                    mGoalsView.showNoGoals();
                } else {
                    mGoalsView.showGoals(goals);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mGoalsView.isActive()) {
                    mGoalsView.showLoadingGoalsError();
                }
            }
        });
    }

    @Override
    public void addNewGoal() {
        mGoalsView.showAddGoal();
    }

    @Override
    public void editGoal(Goal goal) {
        if (SharedPreferencesRepository.isEditGoalEnabled()) {
            mGoalsView.showEditGoal(goal.getId());
        }
    }
}
