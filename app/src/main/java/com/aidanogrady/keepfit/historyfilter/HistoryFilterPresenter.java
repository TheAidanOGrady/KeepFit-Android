package com.aidanogrady.keepfit.historyfilter;

import com.aidanogrady.keepfit.data.model.HistoryDateFilter;
import com.aidanogrady.keepfit.data.model.HistoryGoalFilter;
import com.aidanogrady.keepfit.data.source.PreferenceRepository;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;

/**
 * The presenter for history filters.
 *
 * @author Aidan O'Grady
 * @since 0.10
 */
class HistoryFilterPresenter implements HistoryFilterContract.Presenter {
    /**
     * The view for the add/edit goal view.
     */
    private final HistoryFilterContract.View mHistoryFilterView;

    /**
     * The repository for storing and retrieving shared preferences.
     */
    private PreferenceRepository mPreferencesRepository;

    /**
     * Constructs a new HistoryFilterPresenter.
     *
     * @param view the view to be presented
     */
    HistoryFilterPresenter(HistoryFilterContract.View view) {
        this.mHistoryFilterView = view;
        mPreferencesRepository = SharedPreferencesRepository.getInstance();
    }

    @Override
    public void start() {
        populateFilters();
    }

    @Override
    public void populateFilters() {
        // Populate filter for handling goal progression
        HistoryDateFilter historyDateFilters[] = HistoryDateFilter.values();
        String[] historyDateFilterNames = new String[historyDateFilters.length];
        for (int i = 0; i < historyDateFilters.length; i++) {
            historyDateFilterNames[i] = historyDateFilters[i].name();
        }
        int currentDateFilterIndex = mPreferencesRepository.getCurrentHistoryDateFilter().ordinal();
        mHistoryFilterView.setDateFilters(historyDateFilterNames, currentDateFilterIndex);

        // Populate filter for handing date progression
        HistoryGoalFilter historyGoalFilters[] = HistoryGoalFilter.values();
        String[] historyGoalFilterNames = new String[historyGoalFilters.length];
        for (int i = 0; i < historyGoalFilters.length; i++) {
            historyGoalFilterNames[i] = historyGoalFilters[i].name();
        }
        int currentGoldFilterIndex = mPreferencesRepository.getCurrentHistoryGoalFilter().ordinal();
        mHistoryFilterView.setGoalFilters(historyGoalFilterNames, currentGoldFilterIndex);

        double currentGoalProgress= mPreferencesRepository.getCurrentHistoryGoalProgressFilter();
        mHistoryFilterView.setGoalProgress(currentGoalProgress);
    }

    @Override
    public void saveFilters(String date, long start, long end, String goal, double percentage) {
        mPreferencesRepository.setCurrentHistoryDateFilter(HistoryDateFilter.valueOf(date));
        mPreferencesRepository.setCurrentHistoryGoalFilter(HistoryGoalFilter.valueOf(goal));
        mPreferencesRepository.setCurrentHistoryGoalProgressFilter(percentage);

        // Go back
        mHistoryFilterView.showHistory();
    }
}
