package com.techdev.goalbuzz.service.match;

import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.core.datasource.local.db.database.DatabaseManager;

import java.util.List;

public interface IMatchService {
    List<Match> getUpcoming(DatabaseManager databaseManager);
    List<Match> getResults();
    List<Match> getLives();
    List<Match> getFullList();
    int getTotalCount();
}
