package com.techdev.goalbuzz.service.match;

import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.room.database.RoomManager;

import java.util.List;

public interface IMatchService {
    List<Match> getUpcoming(RoomManager roomManager);
    List<Match> getResults();
    List<Match> getLives();
    List<Match> getFullList();
    int getTotalCount();
}
