package com.aidanogrady.keepfit.goals;

import android.app.Activity;
import android.content.Context;

import com.aidanogrady.keepfit.addeditgoal.AddEditGoalActivity;
import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;

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

    /**
     * Whether or not this load is the first one.
     */
    private boolean mFirstLoad = true;

    public GoalsPresenter(Context context, GoalsContract.View goalsView) {
        mGoalsRepository = GoalsRepository.getInstance(context);
        mGoalsView = goalsView;
        mGoalsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadGoals(true);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddEditGoalActivity.REQUEST_ADD_GOAL == requestCode && Activity.RESULT_OK == resultCode)
        {
            mGoalsView.showSuccessfullySavedMessage();
        }
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
        mGoalsView.showEditGoal(goal.getId());
    }
}
