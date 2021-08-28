package com.techdev.goalbuzz.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.techdev.goalbuzz.model.live.Match;


@Entity(tableName = "result")
public class Result extends BaseEntity {

    @ColumnInfo(name ="competition")
    private String competition;
    @ColumnInfo(name ="season")
    private String season;
    @ColumnInfo(name ="utcDate")
    private String utcDate;
    @ColumnInfo(name ="status")
    private String status;
    @ColumnInfo(name ="matchday")
    private Integer matchDay;
    @ColumnInfo(name ="stage")
    private String stage;
    @ColumnInfo(name ="group")
    private String group;
    @ColumnInfo(name ="lastUpdated")
    private String lastUpdated;
    @ColumnInfo(name ="home_score")
    private String homeScore;
    @ColumnInfo(name ="away_score")
    private String awayScore;
    @ColumnInfo(name ="homeTeam")
    private String homeTeam;
    @ColumnInfo(name ="awayTeam")
    private String awayTeam;
    @ColumnInfo(name ="duration")
    private String duration;
    @ColumnInfo(name = "notify")
    private boolean notify;

    public Result() {
    }

    public Result(Match match){
        this.setId(match.getId());
        this.setAwayTeam(match.getAwayTeam().getName());
        this.setCompetition(match.getCompetition().getName());
        this.setGroup(match.getGroup());
        this.setHomeTeam(match.getHomeTeam().getName());
        this.setLastUpdated(match.getLastUpdated());
        this.setMatchDay(match.getMatchday());
        this.setStage(match.getStage());
        this.setStatus(match.getStatus());
        this.setUtcDate(match.getUtcDate());
        this.setNotify(false);
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(Integer matchDay) {
        this.matchDay = matchDay;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
