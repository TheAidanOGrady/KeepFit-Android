package com.aidanogrady.keepfit.addeditgoal;

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
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.google.common.base.Strings;

/**
 * Main UI for the add/edit goal fragment. Users can provide the name of a goal, the number of steps
 * for that goal.
 *
 * @author Aidan O'Grady
 * @since 0.3.1
 */
public class AddEditGoalFragment extends Fragment implements AddEditGoalContract.View {
    /**
     * The argument for editing goal id.
     */
    public static final String ARGUMENT_EDIT_GOAL_ID = "EDIT_GOAL_ID";

    /**
     * The presenter for this view.
     */
    private AddEditGoalContract.Presenter mPresenter;

    /**
     * The text view for the goal's name.
     */
    private TextView mName;

    /**
     * The text view for the goal's number of steps.
     */
    private TextView mSteps;

    /**
     * Is a new goal being created or an existing one being added?
     */
    private boolean mIsNewGoal;

    /**
     * Required empty constructor.
     */
    public AddEditGoalFragment() {}


    /**
     * Returns a new instance of the fragment.
     *
     * @return new instance
     */
    public static AddEditGoalFragment newInstance() { return new AddEditGoalFragment(); }

    @Override
    public void setPresenter(AddEditGoalContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            String id = getArguments().getString(AddEditGoalFragment.ARGUMENT_EDIT_GOAL_ID);
            mIsNewGoal = (id == null);
        } else {
            mIsNewGoal = true;
        }
        View root = inflater.inflate(R.layout.fragment_add_edit_goal, container, false);
        mName = (TextView) root.findViewById(R.id.goal_name);
        mSteps = (TextView) root.findViewById(R.id.goal_steps);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_goal_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_delete);
        if (mIsNewGoal) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mPresenter.deleteGoal();
                break;
            case R.id.menu_save:
                String name = mName.getText().toString();
                String stepsString = mSteps.getText().toString();
                int steps = Strings.isNullOrEmpty(stepsString) ? 0 : Integer.valueOf(stepsString);
                mPresenter.saveGoal(name, steps);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsNewGoal)
            mPresenter.populateGoal();
    }

    @Override
    public void showEmptyGoalError() {
        Snackbar.make(mName, "Please provide goal name and steps", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGoalsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setName(String name) {
        mName.setText(name);
    }

    @Override
    public void setSteps(int steps) {
        mSteps.setText(String.valueOf(steps));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
