package com.aidanogrady.keepfit.historyfilter;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aidanogrady.keepfit.R;

/**
 * The HistoryFilterActivity handles the filtering of the history.
 *
 * @author Aidan O'Grady
 * @since 0.10
 */
public class HistoryFilterActivity extends AppCompatActivity {
    /**
     * The code for requests concerning saved filters.
     */
    public static final int FILTERS_SAVED = 3;

    /**
     * The presenter
     */
    private HistoryFilterPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_filter);

        // Set up with toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        HistoryFilterFragment historyFilterFragment = (HistoryFilterFragment)
                getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (historyFilterFragment == null) {
            historyFilterFragment = HistoryFilterFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, historyFilterFragment);
            ft.commit();
        }

        // Create presenter
        mPresenter = new HistoryFilterPresenter(historyFilterFragment);
        historyFilterFragment.setPresenter(mPresenter);
    }
}
