package com.techdev.goalbuzz.service.marquee;

import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.featureMain.domain.models.Score;
import com.techdev.goalbuzz.core.util.Constant;
import com.techdev.goalbuzz.core.util.MatchUtil;

import java.util.List;

public class MatchMarqueeService implements IMarqueeService<Match> {

    private static MatchMarqueeService instance;
    private final String[] matchTypes = {"IN_PLAY", "FINISHED", "SCHEDULED"};

    public static MatchMarqueeService getInstance(){
        if (instance == null){
            instance = new MatchMarqueeService();
        }
        return instance;
    }

    @SafeVarargs
    @Override
    public final String get(List<Match>... list) {
        StringBuilder marquee = new StringBuilder();
        int position = 0;
        for (List<Match> matches : list){
            switch (matchTypes[position]){
                case Constant.LIVE_MATCH:{
                    marquee.append(getLive(matches, matchTypes[position]));
                    break;
                }
                case Constant.UPCOMING_MATCH:{
                    marquee.append(getUpcoming(matches, matchTypes[position]));
                    break;
                }
                default:
                    marquee.append(getResult(matches, matchTypes[position]));
                    break;
            }
            position++;
        }
        return marquee.toString();
    }

    @Override
    public String build(List<Match> matchList, String type) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<matchList.size(); i++) {
            Match match = matchList.get(i);
            String endIndent = i < matchList.size() - 1 ? "| " : "";
            String marqueeText = "";
            if (type.equals(Constant.UPCOMING_MATCH)){
                String playTime = MatchUtil.getInstance().getPlayTime(match);
                marqueeText = String.format("Matchday %s • %s Vs %s, will start in %s %s", match.getMatchday(),
                        match.getHomeTeam().getName(), match.getAwayTeam().getName(), playTime, endIndent);
            } else {
                Score score = match.getScore();
                if (score != null){
                    int home = score.getPenalties().getHomeTeam() != null ?
                            score.getPenalties().getHomeTeam() : score.getExtraTime().getHomeTeam() != null ?
                            score.getExtraTime().getHomeTeam() : score.getFullTime().getHomeTeam() != null ?
                            score.getFullTime().getHomeTeam() : score.getHalfTime().getHomeTeam() != null ?
                            score.getHalfTime().getHomeTeam() : 0;
                    int away = score.getPenalties().getAwayTeam() != null ?
                            score.getPenalties().getAwayTeam() : score.getExtraTime().getAwayTeam() != null ?
                            score.getExtraTime().getAwayTeam() : score.getFullTime().getAwayTeam() != null ?
                            score.getFullTime().getAwayTeam() : score.getHalfTime().getAwayTeam() != null ?
                            score.getHalfTime().getAwayTeam() : 0;
                    marqueeText = String.format("Matchday %s • %s %s - %s %s %s", match.getMatchday(),
                            match.getHomeTeam().getName(), home, match.getAwayTeam().getName(), away, endIndent);
                }
            }
            builder.append(marqueeText);
        }
        return builder.toString();
    }

    @Override
    public String getLive(List<Match> list, String type) {
        return list != null && !list.isEmpty() ? " ▣ Live : " + build(list, Constant.LIVE_MATCH) : "";
    }

    @Override
    public String getUpcoming(List<Match> list, String type) {
        return list != null && !list.isEmpty() ? " ▣ Upcoming : " + build(list, Constant.UPCOMING_MATCH) : "";
    }

    @Override
    public String getResult(List<Match> list, String type) {
        return list != null && !list.isEmpty() ? " ▣ Results : " + build(list, Constant.FINISHED_MATCH) : "";
    }
}
