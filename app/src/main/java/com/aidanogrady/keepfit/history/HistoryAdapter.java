package com.aidanogrady.keepfit.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.history.HistoryAdapter.HistoryViewHolder;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Locale;

/**
 * The Adapter for the list of history items.
 *
 * @author Aidan O'Grady
 * @since 0.9
 */
class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> implements Filterable {
    /**
     * The list of history to be displayed.
     */
    private List<History> mHistory;


    /**
     * Constricts a mew HistoryAdapter for the given list of history.
     *
     * @param history the history to be adapted
     */
    HistoryAdapter(List<History> history) {
        setList(history);
    }

    /**
     * Replaces the current list of history with the given list of history.
     *
     * @param history the list of history to replace existing
     */
    void replaceData(List<History> history) {
        setList(history);
    }

    /**
     * Sets the list of history to the given list of history.
     *
     * @param history list of history
     */
    private void setList(List<History> history) {
        if (history != null) {
            mHistory = history;
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View historyView = inflater.inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(historyView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        History history = mHistory.get(position);
        Goal goal = history.getGoal();

        LocalDate localDate = LocalDate.ofEpochDay(history.getDate());
        holder.dateTextView.setText(localDate.toString());

        if (goal != null) {
            holder.iconTextView.setText(Character.toString(goal.getName().charAt(0)));
            holder.goalTextView.setText(goal.getName());

            String progress = history.getDistance() + " / " + goal.getDistance() + " " +
                    goal.getUnit().toString();
            holder.stepsTextView.setText(progress);

            holder.percentageTextView.setText(
                    String.format(Locale.getDefault(), "%.2f %%",
                            history.getDistance() * 100 / goal.getDistance())
            );
        } else {
            holder.goalTextView.setText(R.string.goal_not_found);
            holder.percentageTextView.setText("?? %");
            holder.stepsTextView.setText(String.format(Locale.getDefault(), "%.2f",
                    history.getDistance()));
        }
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    /**
     * The ViewHolder for a single History item.
     */
    class HistoryViewHolder extends RecyclerView.ViewHolder {
        /**
         * The TextView acting as our icon.
         */
        TextView iconTextView;

        /**
         * The TextView displaying the history date.
         */
        TextView dateTextView;

        /**
         * The TextView displaying the goal name.
         */
        TextView goalTextView;

        /**
         * The TextView displaying the number of steps achieved.
         */
        TextView stepsTextView;

        /**
         * The TextView displaying the goal percentage.
         */
        TextView percentageTextView;

        /**
         * Constricts a new HistoryViewHolder.
         *
         * @param itemView the view for the history items.
         */
        HistoryViewHolder(View itemView) {
            super(itemView);
            iconTextView = (TextView) itemView.findViewById(R.id.history_icon);
            dateTextView = (TextView) itemView.findViewById(R.id.history_date);
            goalTextView = (TextView) itemView.findViewById(R.id.history_goal);
            stepsTextView = (TextView) itemView.findViewById(R.id.history_steps);
            percentageTextView = (TextView) itemView.findViewById(R.id.history_percentage);
        }
    }
}
