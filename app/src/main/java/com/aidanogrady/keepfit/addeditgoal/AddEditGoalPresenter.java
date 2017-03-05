package com.aidanogrady.keepfit.addeditgoal;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;

/**
 * The AddEditGoalPresenter listens for user actions from UI, retrieves data and updates UI as
 * required.
 *
 * @author Aidan O'Grady
 * @since 0.3.1
 */
class AddEditGoalPresenter implements AddEditGoalContract.Presenter,
        GoalsDataSource.GetGoalCallback {
    /**
     * The goals repository to retrieve goals from.
     */
    private final GoalsDataSource mGoalsRepository;

    /**
     * The view for the add/edit goal view.
     */
    private final AddEditGoalContract.View mAddEditGoalView;

    /**
     * The ID of the goal currently being edited.
     */
    private String mGoalId;

    /**
     * Whether or not data is missing.
     */
    private boolean mIsDataMissing;


    /**
     * Constructs a new AddEditGoalPresenter.
     *
     * @param context the context the presenter is being viewed in.
     * @param view the view being presented
     * @param goalId the ID of the goal being shown.
     */
    AddEditGoalPresenter(Context context, AddEditGoalContract.View view, String goalId) {
        mGoalsRepository = GoalsRepository.getInstance(context);
        mAddEditGoalView = view;
        mGoalId = goalId;
    }

    @Override
    public void start() {
        if (!isNewGoal() && mIsDataMissing) {
            populateGoal();
        }
    }

    @Override
    public void saveGoal(String name, int steps) {
        if (isNewGoal()) {
            createGoal(name, steps);
        } else {
            updateGoal(name, steps);
        }
    }

    @Override
    public void populateGoal() {
        if (isNewGoal()) {
            throw new RuntimeException("populateGoal() was called but goal was new");
        }
        mGoalsRepository.getGoal(mGoalId, this);
    }

    @Override
    public void deleteGoal() {
        if (isNewGoal()) {
            throw new RuntimeException("deleteGoal() was called but goal was new");
        }
        mGoalsRepository.deleteGoal(mGoalId);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void onGoalLoaded(Goal goal) {
        if (mAddEditGoalView.isActive()) {
            mAddEditGoalView.setName(goal.getName());
            mAddEditGoalView.setSteps(goal.getSteps());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if (mAddEditGoalView.isActive()) {
            mAddEditGoalView.showEmptyGoalError();
        }
    }

    /**
     * Returns whether or not a new goal is being added.
     *
     * @return true if goal is being added, false is goal is being edited.
     */
    private boolean isNewGoal() {
        return mGoalId == null;
    }

    /**
     * Creates a new goal and inserts it into repository.
     *
     * @param name the name of the goal to be added
     * @param steps the number of steps of the goal to be added
     */
    private void createGoal(String name, int steps) {
        Goal goal = new Goal(name, steps);
        if (goal.getName().isEmpty() && steps < 1) {
            mAddEditGoalView.showEmptyGoalError();
        } else {
            mGoalsRepository.insertGoal(goal);
            mAddEditGoalView.showGoalsList();
        }
    }

    /**
     * Updates a goal for the repository.
     *
     * @param name the name of the goal to be added
     * @param steps the number of steps of the goal to be added
     */
    private void updateGoal(String name, int steps) {
        if (isNewGoal()) {
            throw new RuntimeException("updateGoal() was called but task is new");
        }
        mGoalsRepository.updateGoal(new Goal(name, steps), mGoalId);
        mAddEditGoalView.showGoalsList();
    }
}
