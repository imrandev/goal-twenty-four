
package com.techdev.goalbuzz.model.league;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("currentMatchday")
    @Expose
    private Object currentMatchday;
    @SerializedName("winner")
    @Expose
    private Winner winner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getCurrentMatchday() {
        return currentMatchday;
    }

    public void setCurrentMatchday(Object currentMatchday) {
        this.currentMatchday = currentMatchday;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

}
