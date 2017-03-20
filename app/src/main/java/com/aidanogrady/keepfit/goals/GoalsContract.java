package com.aidanogrady.keepfit.goals;

import com.aidanogrady.keepfit.base.BasePresenter;
import com.aidanogrady.keepfit.base.BaseView;
import com.aidanogrady.keepfit.data.model.Goal;

import java.util.List;

/**
 * The GoalsContract is a specification between the view and presenter related to the viewing and
 * modification of goals.
 *
 * @author Aidan O'Grady
 * @since 0.3
 */
class GoalsContract {
    /**
     * The View half of the contract.
     */
    interface View extends BaseView<Presenter> {
        /**
         * Shows the given list of goals.
         *
         * @param goals the goals to be shown
         */
        void showGoals(List<Goal> goals);

        /**
         * Shows the adding goal view.
         */
        void showAddGoal();

        /**
         * Shows the edit goal view.
         *
         * @param goalId the goal to be edited.
         */
        void showEditGoal(String goalId);

        /**
         * Shows view for when there are no goals.
         */
        void showNoGoals();

        /**
         * Show the error indicating that loading goals encountered an error.
         */
        void showLoadingGoalsError();

        /**
         * Shows the message indicating that a goal was successfully saved.
         */
        void showSuccessfullySavedMessage();

        /**
         * Shows the message indicating that a goal was successfully deleted.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Returns whether or not the view is active.
         *
         * @return true if view is active, otherwise false
         */
        boolean isActive();
    }

    /**
     * The Presenter half of the contract.
     */
    interface Presenter extends BasePresenter {
        /**
         * Loads goals to the presenter.
         *
         * @param forceUpdate whether or not update should be forced.
         */
        void loadGoals(boolean forceUpdate);

        /**
         * Presents the add goal view to the user.
         */
        void addNewGoal();

        /**
         * Presents the editor for the given goal.
         *
         * @param goal the goal to be edited
         */
        void editGoal(Goal goal);
    }
}
