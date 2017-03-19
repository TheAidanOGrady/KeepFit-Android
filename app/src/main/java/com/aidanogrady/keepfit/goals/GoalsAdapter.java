package com.aidanogrady.keepfit.goals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.Goal;

import java.util.List;
import java.util.Locale;

/**
 * The GoalsAdapter adapts the RecyclerView for displaying Goals, handling the refreshing of the
 * view when changes are made.
 *
 * @author Aidan O'Grady
 * @since 0.9
 */
class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {
    /**
     * The list of goals to be displayed.
     */
    private List<Goal> mGoals;

    /**
     * The listener for when a goal is clicked.
     */
    private GoalsFragment.GoalItemListener mGoalItemListener;


    /**
     * Constructs a new GoalsAdapter with the given list of goals.
     *
     * @param goals the goals to be adapted
     * @param mGoalItemListener the listener
     */
    GoalsAdapter(List<Goal> goals, GoalsFragment.GoalItemListener mGoalItemListener) {
        setList(goals);
        this.mGoalItemListener = mGoalItemListener;
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
        textView.setText(String.format(Locale.getDefault(), "%.2f", goal.getDistance()));

        textView = holder.stepsUnitTextView;
        textView.setText(goal.getUnit().toString());

        holder.itemView.setOnClickListener(view -> mGoalItemListener.onGoalClick(goal));
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }


    /**
     * The ViewHolder for a singular goal.
     */
    class GoalsViewHolder extends RecyclerView.ViewHolder {
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
