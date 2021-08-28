package com.techdev.goalbuzz.util;

public class Constant {
    public static final String BASE_URL = "http://api.football-data.org";
    public static final String BASE_DB_URL = "https://www.thesportsdb.com";
    public static final String BASE_BAT_URL = "https://www.scorebat.com";
    public static final String PREF_BASE_NAME = "goalKicker";
    public static final String API_TOKEN = "af50ecedd7404682b31476c555b92ec9";

    public static final String UPCOMING_MATCH = "SCHEDULED";
    public static final String LIVE_MATCH = "IN_PLAY";
    public static final String FINISHED_MATCH = "FINISHED";
    public static final String LEAGUE_ID = "league-id";
    public static final String LEAGUE_NAME = "league-name";
    public static final String TEAM = "team";

    public static final long CLICK_TIME_INTERVAL = 300;

    public static final String BROADCAST_SERIALIZABLE_BUNDLE_EXTRA = "broadcast-serializable-data-model";
    public static final String CHANNEL_ID = "GTf-notification-channel";
    public static final String NOTIFICATION_ID = "upcoming-notification";

    public static final int APP_UPDATE_REQUEST_CODE = 1001;
    public static final float MILLISECONDS_PER_INCH = 50f;

    public static final int SPLASH_DISPLAY_LENGTH = 1000;
    public static final int AD_INTERVAL = 3;

    public static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;

    public static final String SCORE_BAT_HTML = "<iframe src=\"https://www.scorebat.com/embed/livescore/\" \n" +
            "\tframeborder=\"0\" \n" +
            "\twidth=\"600\" \n" +
            "\theight=\"760\" allowfullscreen allow='autoplay; fullscreen'\n" +
            "\tstyle=\"width:600px; height:760px; overflow:hidden; display:block;\" \n" +
            "\tclass=\"_scorebatEmbeddedPlayer_\">\n" +
            "</iframe>\n" +
            "<script>\n" +
            "\t(function(d, s, id) { \n" +
            "\t\tvar js, fjs = d.getElementsByTagName(s)[0]; \n" +
            "\t\tif (d.getElementById(id)) \n" +
            "\t\t\treturn; \n" +
            "\t\tjs = d.createElement(s); \n" +
            "\t\tjs.id = id; \n" +
            "\t\tjs.src = 'https://www.scorebat.com/embed/embed.js?v=arrv'; \n" +
            "\t\tfjs.parentNode.insertBefore(js, fjs); \n" +
            "\t}(document, 'script', 'scorebat-jssdk'));\n" +
            "</script>";
}
