package com.techdev.goalbuzz.network.client;

import com.techdev.goalbuzz.model.fixture.Fixtures;
import com.techdev.goalbuzz.model.league.League;
import com.techdev.goalbuzz.model.live.Live;
import com.techdev.goalbuzz.model.market.Amazon;
import com.techdev.goalbuzz.model.point.PointTable;
import com.techdev.goalbuzz.model.team.TeamInfo;
import com.techdev.goalbuzz.model.top.TopScorer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRepository {

    @GET("/v2/matches")
    Call<Live> getMatches();

    @GET("/v2/competitions/{leagueId}/matches")
    Call<Fixtures> getFixturesByLeagueId(@Path("leagueId") String leagueId, @Query("status") String status);

    @GET("/v2/competitions/{leagueId}/standings")
    Call<PointTable> getStandingsByLeague(@Path("leagueId") String leagueId);

    @GET("/v2/competitions/{leagueId}")
    Call<League> getLeagueInfoById(@Path("leagueId") String leagueId);

    @GET("/v2/competitions/{leagueId}/scorers")
    Call<TopScorer> getTopScorerInfoById(@Path("leagueId") String leagueId);

    @GET("/api/v1/json/1/searchteams.php")
    Call<TeamInfo> getSingleTeamInfo(@Query("t") String team);

    @GET("/api/amazon.json")
    Call<Amazon> findAmazonProducts();
}
