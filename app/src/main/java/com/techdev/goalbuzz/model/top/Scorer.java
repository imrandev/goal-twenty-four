
package com.techdev.goalbuzz.model.top;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scorer {

    @SerializedName("player")
    @Expose
    private Player player;
    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("numberOfGoals")
    @Expose
    private int numberOfGoals;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getNumberOfGoals() {
        return numberOfGoals;
    }

    public void setNumberOfGoals(int numberOfGoals) {
        this.numberOfGoals = numberOfGoals;
    }

}
