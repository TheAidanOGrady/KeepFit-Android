package com.aidanogrady.keepfit.home;

import android.support.v4.util.Pair;

import com.aidanogrady.keepfit.base.BasePresenter;
import com.aidanogrady.keepfit.base.BaseView;
import com.aidanogrady.keepfit.data.model.Update;

import java.util.List;

/**
 * The HomeContract is a specification between the view and presenter related to the viewing and
 * modification of the home view and model.
 *
 * @author Aidan O'Grady
 * @since 0.5
 */
public class HomeContract {
    /**
     * The View half of the contract.
     */
    interface View extends BaseView<Presenter> {
        /**
         * Sets the current date to be displayed in the overview.
         *
         * @param days the date expressed as the number of days since Epoch
         */
        void setCurrentDate(long days);

        /**
         * Sets the name of the current goal being worked on.
         *
         * @param goalName the name of the goal
         */
        void setCurrentGoal(String goalName);

        /**
         * Sets the current progress of the user thus far.
         *
         * @param currentSteps the number of steps worked so far, -1 if no goal is set
         * @param targetSteps the target of this goal, 0 if no goal is set
         */
        void setCurrentProgress(int currentSteps, int targetSteps);

        /**
         * Sets the percentage representation of the progress thus far,
         *
         * @param percentage percentage progress, -1 if no goal is set
         */
        void setCurrentPercentage(int percentage);

        /**
         * Displays the view for adding steps to today's progress.
         */
        void showAddSteps();

        /**
         * Display a message indicating that there are no goals to select from.
         */
        void showNoGoalsMessage();

        /**
         * Displays the view for choosing the currently active goal.
         *
         * @param goals a pair of the list of goals' string representations and a list of their IDs.
         */
        void showSetGoal(Pair<List<String>, List<String>> goals);

        /**
         * Display a message indicating that a goal needs to be selected.
         */
        void showSelectGoalMessage();

        void showUpdates(List<Update> updates);
    }

    /**
     * The Presenter half of the contract.
     */
    interface Presenter extends BasePresenter {
        /**
         * Add steps to today's progress.
         */
        void addSteps();

        /**
         * Adds steps to today's progress after user has provided information.
         *
         * @param steps the number of steps to be added.
         */
        void addSteps(int steps);

        /**
         * Loads the progress from the database.
         */
        void loadProgress();

        /**
         * Sets the current goal to the given goal.
         *
         * @param id The ID of the goal to be set as current
         */
        void setCurrentGoal(String id);

        /**
         * Allows the user to set the current goal.
         */
        void setGoal();
    }

}
