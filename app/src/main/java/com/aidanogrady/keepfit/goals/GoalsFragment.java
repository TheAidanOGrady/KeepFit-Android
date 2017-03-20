package com.aidanogrady.keepfit.goals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.addeditgoal.AddEditGoalActivity;
import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Displays the list of goals. Users can choose to select a goal or create new goals.
 *
 * @author Aidan O'Grady
 * @since 0.3
 */
public class GoalsFragment extends Fragment implements GoalsContract.View {
    /**
     * The presenter of the goals contract.
     */
    private GoalsContract.Presenter mPresenter;

    /**
     * The adapter for the goals.
     */
    private GoalsAdapter mAdapter;

    /**
     * The RecyclerView responsible for containing the list of goals.
     */
    private RecyclerView mGoalsView;

    /**
     * The view indicating to user that there are no goals.
     */
    private View mNoGoalsView;


    /**
     * Returns a new goals fragment instance.
     *
     * @return new goals fragment
     */
    public static GoalsFragment newInstance() {
        return new GoalsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoalItemListener mGoalItemListener = new ActiveGoalItemListener();
        mAdapter = new GoalsAdapter(new ArrayList<>(), mGoalItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        // Set up goals view and no goals view
        mGoalsView = (RecyclerView) root.findViewById(R.id.goals_recycler_view);
        mGoalsView.setAdapter(mAdapter);
        mNoGoalsView = root.findViewById(R.id.no_goals);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mGoalsView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mGoalsView.getContext(), layoutManager.getOrientation());
        mGoalsView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab_add_goal);
        fab.setOnClickListener(view -> mPresenter.addNewGoal());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadGoals(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mPresenter.loadGoals(true);
        }
    }

    @Override
    public void setPresenter(GoalsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showGoals(List<Goal> goals) {
        mAdapter.replaceData(goals);
        mAdapter.notifyDataSetChanged();
        mGoalsView.setVisibility(View.VISIBLE);
        mNoGoalsView.setVisibility(View.GONE);
    }

    @Override
    public void showAddGoal() {
        Intent intent = new Intent(getContext(), AddEditGoalActivity.class);
        startActivityForResult(intent, AddEditGoalActivity.REQUEST_ADD_GOAL);
    }

    @Override
    public void showEditGoal(String goalId) {
        Intent intent = new Intent(getContext(), AddEditGoalActivity.class);
        intent.putExtra(AddEditGoalActivity.EXTRA_GOAL_ID, goalId);
        startActivityForResult(intent, AddEditGoalActivity.REQUEST_EDIT_GOAL);
    }

    @Override
    public void showNoGoals() {
        mGoalsView.setVisibility(View.GONE);
        mNoGoalsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingGoalsError() {
        showMessage("Cannot load goals");
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage("Goal successfully saved");
    }

    @Override
    public void showSuccessfullyDeletedMessage() {
        showMessage("Goal successfully deleted");
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to be displayed.
     */
    private void showMessage(String message) {
        Snackbar.make(mGoalsView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    /**
     * A basic listener for goal items.
     */
    interface GoalItemListener {
        /**\
         * On a goal being clicked, something should happen to the goal stored in the view.
         *
         * @param goal the goal clicked on
         */
        void onGoalClick(Goal goal);
    }

    /**
     * A class for handling the prevention of the active goal being edited.
     */
    private class ActiveGoalItemListener implements GoalItemListener {
        @Override
        public void onGoalClick(Goal goal) {
            String active = SharedPreferencesRepository.getActiveGoal();
            String id = goal.getId();
            if (!id.equals(active)) {
                mPresenter.editGoal(goal);
            } else {
                Snackbar.make(getView(), R.string.active_goal_message, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
