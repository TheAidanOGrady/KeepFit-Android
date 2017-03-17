package com.aidanogrady.keepfit.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.model.Update;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
import com.aidanogrady.keepfit.data.source.HistoryRepository;
import com.aidanogrady.keepfit.data.source.UpdatesRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * The HomePresenter responds to user actions from the UI and retrieves data to update the UI with.
 *
 * @author Aidan O'Grady
 * @since 0.5
 */
public class HomePresenter implements HomeContract.Presenter {
    /**
     * The goals repository to retrieve goals from.
     */
    private final GoalsRepository mGoalsRepository;

    /**
     * The history repository to retrieve history from.
     */
    private final HistoryRepository mHistoryRepository;

    /**
     * The update repository for inserting updates.
     */
    private final UpdatesRepository mUpdatesRepository;

    /**
     * The home view.
     */
    private final HomeContract.View mHomeView;

    /**
     * Today's progress being made by the user.
     */
    private History mCurrentHistory;

    public HomePresenter(Context context, HomeContract.View homeView) {
        this.mGoalsRepository = GoalsRepository.getInstance(context);
        this.mHistoryRepository = HistoryRepository.getInstance(context);
        this.mUpdatesRepository = UpdatesRepository.getInstance(context);

        this.mHomeView = homeView;
        this.mHomeView.setPresenter(this);

        // Get number of days since epoch
        LocalDate epoch = LocalDate.ofEpochDay(0);
        LocalDate now = LocalDate.now();
        long today = ChronoUnit.DAYS.between(epoch, now);

        long date;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("testModeToggle", false)) {
            date = prefs.getLong("testModeDate", today);
        } else {
            date = today;
        }
        
        mHistoryRepository.getHistory(date, new HistoryDataSource.GetHistoryCallback() {
            @Override
            public void onHistoryLoaded(History history) {
                mCurrentHistory = history;
            }

            @Override
            public void onDataNotAvailable() {
                mCurrentHistory = new History(date);
            }
        });
    }

    @Override
    public void start() {
        loadProgress();
    }

    @Override
    public void addSteps() {
        if (mCurrentHistory.getGoal() == null) {
            mHomeView.showSelectGoalMessage();
        } else {
            mHomeView.showAddSteps();
        }
    }

    @Override
    public void addSteps(int steps) {
        long date = mCurrentHistory.getDate();
        long time = LocalTime.now().toSecondOfDay();
        Update update = new Update(date, time, steps);

        mCurrentHistory.setSteps(mCurrentHistory.getSteps() + steps);
        mCurrentHistory.addUpdate(update);
        mUpdatesRepository.insertUpdate(update);
        mHistoryRepository.insertHistory(mCurrentHistory);

        loadProgress();
    }

    @Override
    public void loadProgress() {
        Goal goal = mCurrentHistory.getGoal();

        mHomeView.setCurrentDate(mCurrentHistory.getDate());
        mHomeView.showUpdates(mCurrentHistory.getUpdates());
        if (goal == null) {
            mHomeView.setCurrentGoal("No goal selected");
            mHomeView.setCurrentProgress(0, 0, "");
            mHomeView.setCurrentPercentage(-1);
        } else {
            mHomeView.setCurrentGoal(goal.getName());
            int current = mCurrentHistory.getSteps();
            int target = goal.getDistance();
            int percentage = (current * 100) / target;
            mHomeView.setCurrentProgress(current, target, goal.getUnit().toString());
            mHomeView.setCurrentPercentage(percentage);
        }
    }

    @Override
    public void setCurrentGoal(String id) {
        mGoalsRepository.getGoal(id, new GoalsDataSource.GetGoalCallback() {
            @Override
            public void onGoalLoaded(Goal goal) {
                mCurrentHistory.setGoal(goal);
                loadProgress();
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    @Override
    public void setGoal() {
        mGoalsRepository.getGoals(new GoalsDataSource.LoadGoalsCallback() {
            @Override
            public void onGoalsLoaded(List<Goal> goals) {
                if (goals.size() > 0) {
                    List<String> goalStrings = new ArrayList<>();
                    List<String> goalIds = new ArrayList<>();
                    for (Goal goal : goals) {
                        goalStrings.add(goal.toString());
                        goalIds.add(goal.getId());
                    }
                    Pair<List<String>, List<String>> pair = new Pair<>(goalStrings, goalIds);
                    mHomeView.showSetGoal(pair);
                } else {
                    mHomeView.showNoGoalsMessage();
                }
            }

            @Override
            public void onDataNotAvailable() {
                mHomeView.showNoGoalsMessage();
            }
        });
    }
}
