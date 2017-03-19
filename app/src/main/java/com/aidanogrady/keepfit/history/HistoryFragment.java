package com.aidanogrady.keepfit.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.historyfilter.HistoryFilterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the history stored in the data source to the user. The history cannot be modified from
 * this screen.
 *
 * @author Aidan O'Grady
 * @since 0.4
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

    /**
     * The menu item for handling the filter.
     */
    private MenuItem mFilterMenuItem;


    /**
     * Returns a new history fragment instance.
     *
     * @return new history fragment
     */
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.loadHistory(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HistoryAdapter(new ArrayList<>());
        setHasOptionsMenu(true);
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mHistoryView.getContext(),
                layoutManager.getOrientation());
        mHistoryView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mFilterMenuItem = menu.add("Filter");
        mFilterMenuItem.setIcon(R.drawable.ic_filter_list_white_48dp);
        mFilterMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(mFilterMenuItem)) {
            mPresenter.loadHistoryFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadHistory(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mPresenter.loadHistory(true);
        }
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
    public void showHistoryFilter() {
        Intent intent = new Intent(getContext(), HistoryFilterActivity.class);
        startActivityForResult(intent, HistoryFilterActivity.FILTERS_SAVED);
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
}
