package com.procialize.frontier.ApiConstant;

public class ApiConstant {

    public static final String BROADCAST_ACTION_FOR_SPOT_LIVE_POLL = "com.procialize.eventsapp.BROADCAST.spot_live_poll";
    public static final String BROADCAST_ACTION_FOR_SPOT_Quiz = "com.procialize.eventsapp.BROADCAST.spot_Quiz";
    public static final String BROADCAST_ACTION_FOR_SPOT_ChatBuddy = "com.procialize.eventsapp.BROADCAST.spot_Chat";
    public static final String BROADCAST_ACTION_FOR_POST_NEWS_FEED = "com.procialize.eventsapp.BROADCAST.NEWSFEED";

    public static final String BROADCAST_ACTION_FOR_NOTIFICATION_COUNT = "com.procialize.eventsapp.BROADCAST.notification_count";
    public static final String BROADCAST_ACTION = "com.procialize.eventsapp.BROADCAST";
    public static final String BROADCAST_ACTION_BUZZ_FEED = "com.procialize.eventsapp.BROADCAST.BUZZ_FEED";
    public static String folderName = "Frontier2020";

    public static String pageVisited = "1";
    public static String fileViewed = "2";
    public static String fileDownloaded = "3";
    public static String pageSize = "50000";
    //Production URL


    // public static String webUrl = "https://www.procialize.live/stage/mrge/";
    public static String webUrl = "https://www.procialize.live/frontier/";
    public static String baseUrl = webUrl + "API/event_api_call/";

    //buddy url
    public static String buddybaseUrl = webUrl + "API/buddy/";

    //buddy url
    public static String gallerycontestUrl = webUrl + "API/contest_api_call/";



    //imag&Video prod
    public static String imgURL = webUrl;//"https://www.procialize.live/stage/baseapp/";


    public static String tenorUrl = "https://api.tenor.com/v1/";
    public static String quizlist = "QuizFetch";
    public static String Spotquizlist = "SpotQuizFetch";

    public static String quizsubmit = "QuizSubmit";
    public static String Spotquizsubmit = "SpotQuizSubmit";

    public static String exhiilogo = imgURL + "uploads/exhibitor_image/";
    //    public static String exhiilogo = "https://www.procialize.info/" + "uploads/exhibitor_image/";
    public static String doc = "https://www.procialize.info/";
    //    public static String doc = "https://www.procialize.live/stage/baseapp/";
    public static String Leaderboard_IMAGE = imgURL + "uploads/attendee/";
    //public static String profilepic = imgURL + "uploads/attendee/";
   // public static String eventpic = imgURL + "uploads/app_logo/";
    public static String newsfeedwall = imgURL + "uploads/news_feed_media_file/";
   // public static String speaker = imgURL + "uploads/speaker/";
    //public static String galleryimage = imgURL + "uploads/gallery/";
    //public static String folderimage = imgURL + "uploads/folder_video/";
    //public static String selfieimage = imgURL + "uploads/selfie/";
    //public static String selfievideo = imgURL + "uploads/video_contest/";
    //public static String INDEPENDENT_AGENDA = imgURL + "uploads/AgendaVacationSeprateFetch";
    //public static String GALLERY_IMAGE = imgURL + "uploads/session_vacation_media/";
    public static String dateformat = "dd-MM-yyyy HH:mm:ss";
    public static String dateformat1 = "yyyy-MM-dd HH:mm:ss";
    public static String dateformat3 = "dd-MM-yyyy";
    public static String dateformat2 = "dd-MM-yyyy";
    public static String dateformat4 = "yyyy-dd-MM";
    public static String FIREBASE_TOKEN = "";
}
