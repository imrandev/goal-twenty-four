
package com.techdev.goalbuzz.model.fixture;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techdev.goalbuzz.core.adapter.BaseItemType;

public class Fixtures implements BaseItemType {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("filters")
    @Expose
    private Filters filters;
    @SerializedName("competition")
    @Expose
    private Competition competition;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public int getType() {
        return 0;
    }
}
