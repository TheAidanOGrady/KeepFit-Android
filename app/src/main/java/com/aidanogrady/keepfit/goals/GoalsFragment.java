package com.aidanogrady.keepfit.goals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.addeditgoal.AddEditGoalActivity;
import com.aidanogrady.keepfit.data.model.Goal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new GoalsAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        // Set up goals view and no goals view
        mGoalsView = (RecyclerView) root.findViewById(R.id.goals_recycler_view);
        mGoalsView.setAdapter(mAdapter);
        mNoGoalsView = root.findViewById(R.id.no_goals);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab_add_goal);
        fab.setOnClickListener(view -> mPresenter.addNewGoal());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GoalsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showGoals(List<Goal> goals) {
        mAdapter.replaceData(goals);
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
     * The GoalsAdapter handles the recycler view's displaying of each goal.
     */
    private class GoalsAdapter extends RecyclerView.Adapter<GoalsViewHolder> {
        /**
         * The list of goals to be displayed.
         */
        private List<Goal> mGoals;


        /**
         * Constructs a new GoalsAdapter with the given list of goals.
         *
         * @param goals the goals to be adapted
         */
        GoalsAdapter(List<Goal> goals) {
            setList(goals);
        }


        /**
         * Replaces the current list of goals with the given list of goals.
         *
         * @param goals the list of goals to replace existing
         */
        void replaceData(List<Goal> goals) {
            setList(goals);
        }

        /**
         * Sets the list of goals to the given list of goals.
         *
         * @param goals list of goals
         */
        private void setList(List<Goal> goals) {
            if (goals != null) {
                mGoals = goals;
            }
        }

        @Override
        public GoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View goalView = inflater.inflate(R.layout.item_goal, parent, false);
            return new GoalsViewHolder(goalView);
        }

        @Override
        public void onBindViewHolder(GoalsViewHolder holder, int position) {
            Goal goal = mGoals.get(position);

            TextView textView;
            String name = goal.getName();

            textView = holder.iconTextView;
            textView.setText(Character.toString(name.charAt(0)));

            textView = holder.nameTextView;
            textView.setText(name);

            textView = holder.achievedTextView;

            String dateString = Goal.getLastAchievedAsString(goal.getLastAchieved());
            if (dateString == null)
                dateString = "N/A";
            textView.setText(dateString);

            textView = holder.stepsTextView;
            textView.setText(String.format(Locale.getDefault(), "%d", goal.getSteps()));

            textView = holder.stepsUnitTextView;
            textView.setText(R.string.steps);
        }

        @Override
        public int getItemCount() {
            return mGoals.size();
        }
    }

    private class GoalsViewHolder extends RecyclerView.ViewHolder {
        /**
         * The TextView acting as our icon.
         */
        TextView iconTextView;

        /**
         * The TextView containing the name of the goal.
         */
        TextView nameTextView;

        /**
         * The TextView containing the date of the last time the goal was achieved.
         */
        TextView achievedTextView;

        /**
         * The TextView containing the goal's required number of steps.
         */
        TextView stepsTextView;

        /**
         * The TextView containing the units steps are being measured in.
         */
        TextView stepsUnitTextView;

        /**
         * Constructs a new GoalsViewHolder.
         *
         * @param itemView the view of goals
         */
        GoalsViewHolder(View itemView) {
            super(itemView);
            iconTextView = (TextView) itemView.findViewById(R.id.goal_icon);
            nameTextView = (TextView) itemView.findViewById(R.id.goal_name);
            achievedTextView = (TextView) itemView.findViewById(R.id.goal_achieved);
            stepsTextView = (TextView) itemView.findViewById(R.id.goal_steps);
            stepsUnitTextView = (TextView) itemView.findViewById(R.id.steps_unit);
        }
    }
}
