package com.techdev.goalbuzz.util;

import com.techdev.goalbuzz.model.live.Match;

public class MatchUtil {

    private static MatchUtil instance;

    public static MatchUtil getInstance(){
        if (instance == null){
            instance = new MatchUtil();
        }
        return instance;
    }

    public String getPlayTime(Match match){
        return match == null ? "" : String.format("%s %s",
                DateFormatter.getInstance().getDay(DateFormatter.getInstance().getDate(match.getUtcDate())),
                DateFormatter.getInstance().getTime(match.getUtcDate()));
    }

    public String getPlayTime(com.techdev.goalbuzz.model.fixture.Match match){
        return match == null ? "" : String.format("%s %s",
                DateFormatter.getInstance().getDay(DateFormatter.getInstance().getDate(match.getUtcDate())),
                DateFormatter.getInstance().getTime(match.getUtcDate()));
    }
}
