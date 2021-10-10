package com.techdev.goalbuzz.service.match;

import com.techdev.goalbuzz.core.util.AppExecutors;
import com.techdev.goalbuzz.core.datasource.local.db.database.DatabaseManager;
import com.techdev.goalbuzz.core.datasource.local.db.entities.Match;
import com.techdev.goalbuzz.core.util.Constant;
import com.techdev.goalbuzz.core.util.DateFormatter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MatchService implements IMatchService {

    private static IMatchService instance;
    private static WeakReference<List<com.techdev.goalbuzz.featureMain.domain.models.Match>> weakReference;

    public static IMatchService init(List<com.techdev.goalbuzz.featureMain.domain.models.Match> matches) {
        weakReference = new WeakReference<>(matches);
        if (instance == null){
            instance = new MatchService();
        }
        return instance;
    }

    @Override
    public List<com.techdev.goalbuzz.featureMain.domain.models.Match> getUpcoming(DatabaseManager databaseManager) {
        List<com.techdev.goalbuzz.featureMain.domain.models.Match> upcoming = new ArrayList<>();
        if (getTotalCount() < 0) return upcoming;

        for (com.techdev.goalbuzz.featureMain.domain.models.Match m : getFullList()) {
            if (m.getStatus().equals(Constant.UPCOMING_MATCH)
                    && !DateFormatter.getInstance().hasTimeCrossedYet(m.getUtcDate())){
                AppExecutors.getInstance().diskIO().execute(() -> {
                    // check if match already in the schedule
                    Match match = databaseManager.resultDao().findById(m.getId());
                    m.setHasScheduled(match != null);
                });
                upcoming.add(m);
            }
        }
        return upcoming;
    }

    @Override
    public List<com.techdev.goalbuzz.featureMain.domain.models.Match> getResults() {
        List<com.techdev.goalbuzz.featureMain.domain.models.Match> matches = new ArrayList<>();
        if (getTotalCount() < 0) return matches;
        for (com.techdev.goalbuzz.featureMain.domain.models.Match m : getFullList()) {
            if (m.getStatus().equals(Constant.FINISHED_MATCH)){
                matches.add(m);
            }
        }
        return matches;
    }

    @Override
    public List<com.techdev.goalbuzz.featureMain.domain.models.Match> getLives() {
        List<com.techdev.goalbuzz.featureMain.domain.models.Match> lives = new ArrayList<>();
        if (getTotalCount() < 0) return lives;
        for (com.techdev.goalbuzz.featureMain.domain.models.Match m : getFullList()) {
            if (m.getStatus().equals(Constant.LIVE_MATCH)){
                lives.add(m);
            }
        }
        return lives;
    }

    @Override
    public List<com.techdev.goalbuzz.featureMain.domain.models.Match> getFullList() {
        return weakReference.get();
    }

    @Override
    public int getTotalCount() {
        return getFullList() != null ? getFullList().size() : 0;
    }
}
