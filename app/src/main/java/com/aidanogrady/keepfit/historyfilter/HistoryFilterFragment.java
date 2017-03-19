package com.aidanogrady.keepfit.historyfilter;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.aidanogrady.keepfit.R;
import com.google.common.base.Strings;

/**
 * The fragment for the history filter.
 *
 * @author Aidan O'Grady
 * @since 0.10
 */
public class HistoryFilterFragment extends Fragment implements HistoryFilterContract.View {
    /**
     * The presenter for this view.
     */
    private HistoryFilterContract.Presenter mPresenter;

    /**
     * The spinner containing available history date filters.
     */
    private Spinner mDateFilterSpinner;

    /**
     * The spinner for handling the start date of a custom range filter.
     */
    private Spinner mStartDateFilterSpinner;

    /**
     * The spinner for handling the end date of a custom range filter.
     */
    private Spinner mEndDateFilerSpinner;

    /**
     * The spinner containing the available goal filters.
     */
    private Spinner mGoalFilterSpinner;

    /**
     * The txt editor for choosing the upper or lower limit for goal progress.
     */
    private EditText mGoalProgressFilter;


    /**
     * Required empty constructor.
     */
    public HistoryFilterFragment() {}


    /**
     * Returns a new instance of the fragment.
     *
     * @return new instance
     */
    public static HistoryFilterFragment newInstance() { return new HistoryFilterFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history_filter, container, false);
        mGoalFilterSpinner = (Spinner) root.findViewById(R.id.history_goal_filter_spinner);
        mGoalProgressFilter = (EditText) root.findViewById(R.id.progress_filter);
        mDateFilterSpinner = (Spinner) root.findViewById(R.id.history_date_filter_spinner);
        mStartDateFilterSpinner = (Spinner) root.findViewById(R.id.start_date);
        mEndDateFilerSpinner = (Spinner) root.findViewById(R.id.end_date);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                storeFilter();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.populateFilters();
    }

    @Override
    public void setPresenter(HistoryFilterContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setDateFilters(String[] filters, int startIndex) {
        if (mDateFilterSpinner != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_spinner_item, filters
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDateFilterSpinner.setAdapter(adapter);
            mDateFilterSpinner.setSelection(startIndex);

        }
    }

    @Override
    public void setDateRange(long start, long end) {
        // TODO later
    }

    @Override
    public void setGoalFilters(String[] filters, int startIndex) {
        if (mGoalFilterSpinner != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_spinner_item, filters
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mGoalFilterSpinner.setAdapter(adapter);
            mGoalFilterSpinner.setSelection(startIndex);
        }
    }

    @Override
    public void setGoalProgress(double percentage) {
        mGoalProgressFilter.setText(String.valueOf(percentage));
    }

    @Override
    public void showHistory() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showInvalidRangeError() {
        Snackbar.make(getView(), R.string.range_error_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void storeFilter() {
        String dateFilter = mDateFilterSpinner.getSelectedItem().toString();
        long startDate = -1;
        long endDate = -1;
        String goalFilter = mGoalFilterSpinner.getSelectedItem().toString();
        String progressStr = mGoalProgressFilter.getText().toString();
        double progress = Strings.isNullOrEmpty(progressStr) ? 0 : Double.valueOf(progressStr);
        mPresenter.saveFilters(dateFilter, startDate, endDate, goalFilter, progress);
    }
}
