package com.aidanogrady.keepfit.addeditgoal;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.model.units.UnitsConverter;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

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
        if (!isNewGoal()) {
            populateGoal();
        }
    }

    @Override
    public void saveGoal(String name, double steps, String unitName) {
        Unit unit = Unit.valueOf(unitName);
        if (isNewGoal()) {
            createGoal(name, steps, unit);
        } else {
            updateGoal(name, steps, unit);
        }
    }

    @Override
    public void populateGoal() {
        if (isNewGoal()) {
            throw new RuntimeException("populateGoal() was called but goal was new");
        }
        mGoalsRepository.getGoal(mGoalId, false, this);
    }

    @Override
    public void populateUnits() {
        mAddEditGoalView.setUnits(UnitsConverter.AVAILABLE_UNIT_NAMES);
    }

    @Override
    public void deleteGoal() {
        if (isNewGoal()) {
            throw new RuntimeException("deleteGoal() was called but goal was new");
        }
        mGoalsRepository.deleteGoal(mGoalId);
        mAddEditGoalView.showGoalsList();
    }

    @Override
    public void onGoalLoaded(Goal goal) {
        if (mAddEditGoalView.isActive()) {
            mAddEditGoalView.setName(goal.getName());
            mAddEditGoalView.setSteps(goal.getDistance());
        }
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
     * @param unit the unit to be saved
     */
    private void createGoal(String name, double steps, Unit unit) {
        if (Strings.isNullOrEmpty(name) || steps < 1) {
            mAddEditGoalView.showEmptyGoalError();
        } else {
            Goal goal = new Goal(name, steps, unit);
            mGoalsRepository.insertGoal(goal);
            mAddEditGoalView.showGoalsList();
        }
    }

    /**
     * Updates a goal for the repository.
     *
     * @param name the name of the goal to be added
     * @param distance the distance of the goal to be added
     * @param unit the unit to be saved
     */
    private void updateGoal(String name, double distance, Unit unit) {
        if (isNewGoal()) {
            throw new RuntimeException("updateGoal() was called but task is new");
        }

        List<String> names = new ArrayList<>();
        mGoalsRepository.getGoals(new GoalsDataSource.LoadGoalsCallback() {
            @Override
            public void onGoalsLoaded(List<Goal> goals) {
                goals.forEach(goal -> names.add(goal.getName()));
            }

            @Override
            public void onDataNotAvailable() {}
        });

        if (names.contains(name) && isNewGoal()) {
            mAddEditGoalView.showNameExistsError();
        } else if (Strings.isNullOrEmpty(name) || distance < 1) {
            mAddEditGoalView.showEmptyGoalError();
        } else {
            mGoalsRepository.updateGoal(new Goal(name, distance, unit), mGoalId);
            mAddEditGoalView.showGoalsList();
        }
    }
}
