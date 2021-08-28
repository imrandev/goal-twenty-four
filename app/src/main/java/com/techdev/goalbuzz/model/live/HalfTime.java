
package com.techdev.goalbuzz.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HalfTime implements Serializable {

    private static final long serialVersionUID = -4279148836586850993L;
    @SerializedName("homeTeam")
    @Expose
    private Integer homeTeam;
    @SerializedName("awayTeam")
    @Expose
    private Integer awayTeam;

    public Integer getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Integer homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Integer getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Integer awayTeam) {
        this.awayTeam = awayTeam;
    }

}
