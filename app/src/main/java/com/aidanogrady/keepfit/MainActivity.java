package com.aidanogrady.keepfit;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aidanogrady.keepfit.goals.GoalsFragment;
import com.aidanogrady.keepfit.goals.GoalsPresenter;
import com.aidanogrady.keepfit.history.HistoryFragment;
import com.aidanogrady.keepfit.history.HistoryPresenter;
import com.aidanogrady.keepfit.home.HomeFragment;
import com.aidanogrady.keepfit.home.HomePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MainActivity is the single activity of the app, which contains all the fragments that provide
 * functionality. Its purpose is to allow the user to navigate between these fragments.
 *
 * @author Aidan O'Grady
 * @since 0.3
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up tabLayout and viewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new KeepFitPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * The KeepFitPagerAdapter handles the switching between tabs within the app.
     */
    private class KeepFitPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * A mapping of tab names to the fragment they represent.
         */
        List<Pair<String, Fragment>> mFragments;

        KeepFitPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            Context appContext = getApplicationContext();

            HomeFragment homeFragment = HomeFragment.newInstance();
            homeFragment.setPresenter(new HomePresenter(appContext, homeFragment));
            mFragments.add(new Pair<>(getString(R.string.home_title), homeFragment));

            GoalsFragment goalsFragment = GoalsFragment.newInstance();
            goalsFragment.setPresenter(new GoalsPresenter(appContext, goalsFragment));
            mFragments.add(new Pair<>(getString(R.string.goals_title), goalsFragment));

            HistoryFragment historyFragment = HistoryFragment.newInstance();
            historyFragment.setPresenter(new HistoryPresenter(appContext, historyFragment));
            mFragments.add(new Pair<>(getString(R.string.history_title), historyFragment));

//            mFragments.add(new Pair<String, Fragment>(getString(R.string.statistics_title),
//                    null));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position).second;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).first;
        }
    }
}
