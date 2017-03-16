package com.aidanogrady.keepfit.addeditgoal;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aidanogrady.keepfit.R;

/**
 * The Activity for adding or editing goals.
 *
 * @author Aidan O'Grady
 * @since 0.3.1
 */
public class AddEditGoalActivity extends AppCompatActivity {
    /**
     * The code for a request to create a new goal.
     */
    public static final int REQUEST_ADD_GOAL = 1;

    /**
     * The code for a request to edit an already existing goal.
     */
    public static final int REQUEST_EDIT_GOAL = 2;

    /**
     * The key for the value of the ID of the goal to be edit.
     */
    public static final String EXTRA_GOAL_ID = "GOAL_ID";

    /**
     * The presenter for adding/editing goals.
     */
    private AddEditGoalPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_goal);

        // Set up with toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        AddEditGoalFragment addEditGoalFragment = (AddEditGoalFragment)
                getSupportFragmentManager().findFragmentById(R.id.content_frame);

        String goalId = getIntent().getStringExtra(EXTRA_GOAL_ID);
        if (addEditGoalFragment == null) {
            addEditGoalFragment = AddEditGoalFragment.newInstance();

            if (goalId != null && actionBar != null) {
                actionBar.setTitle("Edit Goal");
                Bundle bundle = new Bundle();
                bundle.putString(AddEditGoalFragment.ARGUMENT_EDIT_GOAL_ID, goalId);
                addEditGoalFragment.setArguments(bundle);
            } else if (actionBar != null) {
                actionBar.setTitle("New Goal");
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, addEditGoalFragment);
            transaction.commit();
        }

        // Create presenter
        mPresenter = new AddEditGoalPresenter(getApplicationContext(), addEditGoalFragment, goalId);
        addEditGoalFragment.setPresenter(mPresenter);
    }
}
