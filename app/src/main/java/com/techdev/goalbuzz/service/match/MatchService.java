package com.techdev.goalbuzz.service.match;

import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.DateFormatter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MatchService implements IMatchService {

    private static IMatchService instance;
    private static WeakReference<List<Match>> weakReference;

    public static IMatchService init(List<Match> matches) {
        weakReference = new WeakReference<>(matches);
        if (instance == null){
            instance = new MatchService();
        }
        return instance;
    }

    @Override
    public List<Match> getUpcoming(RoomManager roomManager) {
        List<Match> upcoming = new ArrayList<>();
        if (getTotalCount() < 0) return upcoming;

        for (Match m : getFullList()) {
            if (m.getStatus().equals(Constant.UPCOMING_MATCH)
                    && !DateFormatter.getInstance().hasTimeCrossedYet(m.getUtcDate())){
                AppExecutors.getInstance().diskIO().execute(() -> {
                    // check if match already in the schedule
                    Result result = roomManager.resultDao().findById(m.getId());
                    m.setHasScheduled(result!= null);
                });
                upcoming.add(m);
            }
        }
        return upcoming;
    }

    @Override
    public List<Match> getResults() {
        List<Match> results = new ArrayList<>();
        if (getTotalCount() < 0) return results;
        for (Match m : getFullList()) {
            if (m.getStatus().equals(Constant.FINISHED_MATCH)){
                results.add(m);
            }
        }
        return results;
    }

    @Override
    public List<Match> getLives() {
        List<Match> lives = new ArrayList<>();
        if (getTotalCount() < 0) return lives;
        for (Match m : getFullList()) {
            if (m.getStatus().equals(Constant.LIVE_MATCH)){
                lives.add(m);
            }
        }
        return lives;
    }

    @Override
    public List<Match> getFullList() {
        return weakReference.get();
    }

    @Override
    public int getTotalCount() {
        return getFullList() != null ? getFullList().size() : 0;
    }
}
