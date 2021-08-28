
package com.techdev.goalbuzz.model.videos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("competition")
    @Expose
    private String competition;
    @SerializedName("matchviewUrl")
    @Expose
    private String matchviewUrl;
    @SerializedName("competitionUrl")
    @Expose
    private String competitionUrl;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getMatchviewUrl() {
        return matchviewUrl;
    }

    public void setMatchviewUrl(String matchviewUrl) {
        this.matchviewUrl = matchviewUrl;
    }

    public String getCompetitionUrl() {
        return competitionUrl;
    }

    public void setCompetitionUrl(String competitionUrl) {
        this.competitionUrl = competitionUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
