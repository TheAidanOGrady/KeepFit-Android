package com.aidanogrady.keepfit.addeditgoal;

import com.aidanogrady.keepfit.base.BasePresenter;
import com.aidanogrady.keepfit.base.BaseView;

/**
 * The contract between view and presenter for displaying the Goal Editor.
 *
 * @author Aidan O'Grady
 * @since 0.3.1
 */
class AddEditGoalContract {

    interface View extends BaseView<Presenter> {
        /**
         * Displays an error when the goal data is empty.
         */
        void showEmptyGoalError();

        /**
         * Shows the list of goals.
         */
        void showGoalsList();

        /**
         * Sets the name to the given name.
         *
         * @param name the name to be displayed
         */
        void setName(String name);

        /**
         * Sets the number of steps to the given value.
         *
         * @param steps the number of steps to be displayed
         */
        void setSteps(int steps);

        /**
         * Returns whether or not view is active.
         *
         * @return view is active
         */
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        /**
         * Save goal with the given name and steps.
         *
         * @param name the name of the goal to be saved
         * @param steps the number of steps of the goal to be saved
         */
        void saveGoal(String name, int steps);

        /**
         * Populates the editor with the correct data.
         */
        void populateGoal();

        /**
         * Deletes the current goal.
         */
        void deleteGoal();

        /**
         * Returns whether or not data is missing.
         */
        boolean isDataMissing();
    }
}
