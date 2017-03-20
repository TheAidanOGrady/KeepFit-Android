package com.aidanogrady.keepfit.history;

import android.content.Context;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.model.HistoryDateFilter;
import com.aidanogrady.keepfit.data.model.HistoryGoalFilter;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.model.units.UnitsConverter;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
import com.aidanogrady.keepfit.data.source.HistoryRepository;
import com.aidanogrady.keepfit.data.source.PreferenceRepository;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
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
        this.mHistoryView.setPresenter(this);
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
                histories = getFilteredHistory(histories);
                if (histories.isEmpty()) {
                    mHistoryView.showNoHistory();
                } else {
                    mHistoryView.showHistory(histories);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mHistoryView.showNoHistory();
                if (mHistoryView.isActive()) {
                    mHistoryView.showLoadingHistoryError();
                }
            }
        });
    }

    /**
     * Returns the filtered list of history given.
     *
     * @param histories the history list to be filtered
     * @return the filtered list
     */
    private List<History> getFilteredHistory(List<History> histories) {
        histories = filterGoal(histories);
        histories = filterDate(histories);

        // Handle unit conversion
        Unit unit = SharedPreferencesRepository.getHistoryDisplayUnit();
        if (unit != null) {
            histories.stream()
                    .forEachOrdered(history -> history = convertHistory(history, unit));
        }
        return histories;
    }

    /**
     * Converts the given history object to be a certain unit.
     *
     * @param history the history being converted
     * @param unit the unit to convert to
     * @return converted history
     */
    private History convertHistory(History history, Unit unit) {
        Goal goal = history.getGoal();

        double distance = UnitsConverter.convert(unit, goal.getUnit(), history.getDistance());
        history.setDistance(distance);

        double target = UnitsConverter.convert(unit, goal.getUnit(), goal.getDistance());
        Goal convertedGoal = new Goal(goal.getName(), target, goal.getUnit());
        history.setGoal(convertedGoal);

        return history;
    }

    /**
     * Filters the given list of history based on the current goal filter set.
     *
     * @param histories the history to be filtered
     * @return the filtered list
     */
    private List<History> filterGoal(List<History> histories) {
        List<History> filteredList = new ArrayList<>();

        HistoryGoalFilter filter = SharedPreferencesRepository.getHistoryGoalFilter();
        double progress = SharedPreferencesRepository.getHistoryGoalProgressFilter();

        switch (filter) {
            case NONE:
                filteredList.addAll(histories);
                break;
            case COMPLETED:
                histories.stream()
                        .filter(history -> history.getPercentage() >= 100)
                        .forEachOrdered(filteredList::add);
                break;
            case BELOW:
                histories.stream()
                        .filter(history -> history.getPercentage() <= progress)
                        .forEachOrdered(filteredList::add);
                break;
            case ABOVE:
                histories.stream()
                        .filter(history -> history.getPercentage() >= progress)
                        .forEachOrdered(filteredList::add);
                break;
        }
        return filteredList;
    }

    /**
     * Filters the given list of history based on the current date filter set.
     *
     * @param histories the history to be filtered
     * @return the filtered list
     */
    private List<History> filterDate(List<History> histories) {
        List<History> filteredList = new ArrayList<>();

        LocalDate now = LocalDate.now();
        HistoryDateFilter filter = SharedPreferencesRepository.getHistoryDateFilter();

        long start;
        switch (filter) {
            case NONE:
                filteredList.addAll(histories);
                break;
            case WEEK:
                LocalDate weekAgo = now.minusDays(7);
                start = weekAgo.toEpochDay();
                histories.stream()
                        .filter(history -> (start - history.getDate()) < 7)
                        .forEachOrdered(filteredList::add);
                break;
            case MONTH:
                start = now.minusDays(now.getDayOfMonth()).toEpochDay();
                histories.stream()
                        .filter(history -> history.getDate() > start)
                        .forEachOrdered(filteredList::add);
                break;
            case CUSTOM:
                start = SharedPreferencesRepository.getHistoryStartDateFilter();
                long end = SharedPreferencesRepository.getHistoryEndDateFilter();
                histories.stream()
                        .filter(history -> history.getDate() >= start)
                        .filter(history -> history.getDate() <= end)
                        .forEachOrdered(filteredList::add);
                break;
        }
        return filteredList;
    }

    @Override
    public void loadHistoryFilter() {
        mHistoryView.showHistoryFilter();
    }
}
