package com.aidanogrady.keepfit.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Displays the history stored in the data source to the user. The history cannot be modified from
 * this screen.
 */
public class HistoryFragment extends Fragment implements HistoryContract.View {
    /**
     * The presenter of the history contract.
     */
    private HistoryContract.Presenter mPresenter;

    /**
     * The adapter for the history.
     */
    private HistoryAdapter mAdapter;

    /**
     * The RecyclerView responsible for containing the list of history.
     */
    private RecyclerView mHistoryView;

    /**
     * The view indicating to user that there are is history.
     */
    private View mNoHistoryView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HistoryAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryView = (RecyclerView) root.findViewById(R.id.history_recycler_view);
        mHistoryView.setAdapter(mAdapter);
        mNoHistoryView = root.findViewById(R.id.no_history);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mHistoryView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mHistoryView.getContext(),
                        layoutManager.getOrientation());
        mHistoryView.addItemDecoration(dividerItemDecoration);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadHistory(false);
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showHistory(List<History> history) {
        mAdapter.replaceData(history);
        mAdapter.notifyDataSetChanged();
        mHistoryView.setVisibility(View.VISIBLE);
        mNoHistoryView.setVisibility(View.GONE);
    }

    @Override
    public void showNoHistory() {
        mHistoryView.setVisibility(View.GONE);
        mNoHistoryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingHistoryError() {
        showMessage("Cannot load history");
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to be displayed.
     */
    private void showMessage(String message) {
        Snackbar.make(mHistoryView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return false;
    }


    /**
     * The adapter for the history recycler view, handling the updating of the display as the
     * history is updated.
     */
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
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
         * @param history list of goals
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

            holder.dateTextView.setText(History.getLastAchievedAsString(history.getDate()));
            holder.stepsTextView.setText(String.format(Locale.getDefault(), "%d",history.getSteps()));

            if (goal != null) {
                holder.iconTextView.setText(goal.getName().charAt(0));
                holder.goalTextView.setText(goal.getName());
                holder.percentageTextView.setText(
                        String.format(Locale.getDefault(), "%d %%", history.getSteps() * 100)
                );
            } else {
                holder.goalTextView.setText(R.string.goal_not_found);
                holder.percentageTextView.setText("?? %");
            }
        }

        @Override
        public int getItemCount() {
            return mHistory.size();
        }
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
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
