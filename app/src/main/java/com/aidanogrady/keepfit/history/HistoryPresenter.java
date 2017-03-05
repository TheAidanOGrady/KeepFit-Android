package com.aidanogrady.keepfit.history;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
import com.aidanogrady.keepfit.data.source.HistoryRepository;

import java.util.List;

/**
 * The HistoryPresenter responds to user actions from the UI and retrieves data to update the UI
 * with.
 *
 * @author Aidan O'Grady
 * @since 0.4
 */
public class HistoryPresenter implements HistoryContract.Presenter {
    /**
     * The history repository to retrieve history from.
     */
    private final HistoryRepository mHistoryRepository;

    /**
     * The goals view.
     */
    private final HistoryContract.View mHistoryView;


    public HistoryPresenter(Context context, HistoryContract.View historyView) {
        this.mHistoryRepository = HistoryRepository.getInstance(context);
        this.mHistoryView = historyView;
        mHistoryView.setPresenter(this);
    }


    @Override
    public void start() {
        loadHistory(true);
    }

    @Override
    public void loadHistory(boolean forceUpdate) {
        if (forceUpdate) {
            mHistoryRepository.refreshHistory();
        }

        mHistoryRepository.getHistory(new HistoryDataSource.LoadHistoryCallback() {
            @Override
            public void onHistoryLoaded(List<History> histories) {
                if (histories.isEmpty()) {
                    mHistoryView.showNoHistory();
                } else {
                    mHistoryView.showHistory(histories);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mHistoryView.isActive()) {
                    mHistoryView.showLoadingHistoryError();
                }
            }
        });
    }
}
