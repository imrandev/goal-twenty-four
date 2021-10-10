package com.techdev.goalbuzz.core.util;

import androidx.annotation.DrawableRes;

import com.techdev.goalbuzz.R;

public class ResourceUtils {

    private static ResourceUtils instance;

    @DrawableRes
    private final int[] icons = {
            R.drawable.ic_epl, R.drawable.ic_laliga, R.drawable.ic_uefa,
            R.drawable.ic_serie, R.drawable.ic_bundesliga, R.drawable.ic_ligue_1_logo
    };

    private final String[] names = {
            "English Premier League", "La Liga", "UEFA Champions League",
            "Serie A", "Bundesliga", "Ligue 1"
    };

    private final String[] ids = {
            "PL", "PD", "CL", "SA", "BL1", "FL1"
    };

    public static ResourceUtils getInstance(){
        if (instance == null){
            instance = new ResourceUtils();
        }
        return instance;
    }

    @DrawableRes
    public int[] getLeagueIcons(){
        return icons;
    }

    public String[] getNameOfLeagues(){
        return names;
    }

    public String[] getLeagueIds(){
        return ids;
    }

    public String getLeagueName(String id){
        return getNameOfLeagues()[getPositionById(id)];
    }

    @DrawableRes
    public int getLeagueIcon(String id) {
        return getLeagueIcons()[getPositionById(id)];
    }

    public int getPositionById(String id){
        int position = 0;
        for (String i: ids) {
            if (i.equals(id)){
                return position;
            }
            position ++;
        }
        return -1;
    }
}
