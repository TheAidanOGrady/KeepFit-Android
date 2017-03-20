package com.aidanogrady.keepfit.home;

import android.content.Context;
import android.support.v4.util.Pair;

import com.aidanogrady.keepfit.data.model.Goal;
import com.aidanogrady.keepfit.data.model.History;
import com.aidanogrady.keepfit.data.model.Update;
import com.aidanogrady.keepfit.data.model.units.Unit;
import com.aidanogrady.keepfit.data.model.units.UnitsConverter;
import com.aidanogrady.keepfit.data.source.GoalsDataSource;
import com.aidanogrady.keepfit.data.source.GoalsRepository;
import com.aidanogrady.keepfit.data.source.HistoryDataSource;
import com.aidanogrady.keepfit.data.source.HistoryRepository;
import com.aidanogrady.keepfit.data.source.SharedPreferencesRepository;
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
        loadCurrent();
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
            mHomeView.showAddSteps(UnitsConverter.AVAILABLE_UNIT_NAMES);
        }
    }

    @Override
    public void addSteps(double dist, String unitStr) {
        long date = mCurrentHistory.getDate();
        long time = LocalTime.now().toSecondOfDay();

        Unit unit = Unit.valueOf(unitStr);
        Update update = new Update(date, time, dist, unit);
        double distance = UnitsConverter.convert(mCurrentHistory.getGoal().getUnit(), unit, dist);

        mCurrentHistory.setDistance(mCurrentHistory.getDistance() + distance);
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
            double current = mCurrentHistory.getDistance();
            double target = goal.getDistance();
            mHomeView.setCurrentProgress(current, target, goal.getUnit().toString());
            mHomeView.setCurrentPercentage(mCurrentHistory.getPercentage());
        }
    }

    @Override
    public void setCurrentGoal(String id) {
        mGoalsRepository.getGoal(id, new GoalsDataSource.GetGoalCallback() {
            @Override
            public void onGoalLoaded(Goal goal) {
                mCurrentHistory.setGoal(goal);
                double newDist = 0.0;
                Unit unit = goal.getUnit();
                for (Update update: mCurrentHistory.getUpdates()) {
                    newDist += UnitsConverter.convert(unit, update.getUnit(), update.getDistance());
                }
                mCurrentHistory.setDistance(newDist);
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

    @Override
    public void loadCurrent() {
        LocalDate epoch = LocalDate.ofEpochDay(0);
        LocalDate now = LocalDate.now();
        long today = ChronoUnit.DAYS.between(epoch, now);

        long date;
        if (SharedPreferencesRepository.isTestModeEnabled()) {
            date = SharedPreferencesRepository.getTestModeDate();
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
                if (mCurrentHistory != null && mCurrentHistory.getDistance() > 0) {
                    mCurrentHistory = new History(date);
                } else if (mCurrentHistory == null) {
                    mCurrentHistory = new History(date);
                }
            }
        });
    }
}
