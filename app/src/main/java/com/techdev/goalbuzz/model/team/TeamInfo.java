
package com.techdev.goalbuzz.model.team;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamInfo {

    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
