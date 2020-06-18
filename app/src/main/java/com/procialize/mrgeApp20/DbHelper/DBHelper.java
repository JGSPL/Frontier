package com.procialize.mrgeApp20.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.BuddyList.DataModel.Buddy;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list_db;
import com.procialize.mrgeApp20.GetterSetter.AgendaList;
import com.procialize.mrgeApp20.GetterSetter.AgendaMediaList;
import com.procialize.mrgeApp20.GetterSetter.AgendaVacationList;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventList;
import com.procialize.mrgeApp20.GetterSetter.ExhiCatDetailList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorAttendeeList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList;
import com.procialize.mrgeApp20.GetterSetter.Exhibitor_Meeting_Request_List;
import com.procialize.mrgeApp20.GetterSetter.Exhibitor_Notification_List;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedPostMultimedia;
import com.procialize.mrgeApp20.GetterSetter.NotificationList;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.SponsorsList;
import com.procialize.mrgeApp20.GetterSetter.UserData;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.Speaker.Models.SpeakerList;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "ProcializeEventsDB.db";
    //Attendee Table
    public static final String ATTENDEES_TABLE_NAME = "ATTENDEES_TABLE_NAME";
    public static final String SPEAKER_TABLE_NAME = "SPEAKER_TABLE_NAME";
    //User Table
    public static final String USER_TABLE_NAME = "USER_TABLE_NAME";
    public static final String ATTENDEE_ID = "ATTENDEE_ID";
    public static final String ATTENDEE_API_ACCESS_TOKEN = "ATTENDEE_API_ACCESS_TOKEN";
    public static final String ATTENDEE_FIRST_NAME = "ATTENDEE_FIRST_NAME";
    public static final String ATTENDEE_LAST_NAME = "ATTENDEE_LAST_NAME";
    public static final String ATTENDEE_DESCRIPTION = "ATTENDEE_DESCRIPTION";
    public static final String ATTENDEE_CITY = "ATTENDEE_CITY";
    public static final String ATTENDEE_COUNTRY = "ATTENDEE_COUNTRY";
    public static final String ATTENDEE_PROFILE_PIC = "ATTENDEE_PROFILE_PIC";// PROFILE PIC
    public static final String ATTENDEE_MOBILE = "ATTENDEE_MOBILE";
    public static final String ATTENDEE_EMAIL = "ATTENDEE_EMAIL";
    public static final String ATTENDEE_COMPANY_NAME = "ATTENDEE_COMPANY_NAME";
    public static final String ATTENDEE_DESIGNATION = "ATTENDEE_DESIGNATION";
    public static final String ATTENDEE_TYPE = "ATTENDEE_TYPE";
    public static final String BUDDY_STATUS = "BUDDY_STATUS";
    public static final String CHAT_DATA_COUNT = "CHAT_DATA_COUNT";

    public static final String ATTENDEE_TOTAL_RATING = "ATTENDEE_TOTAL_RATING";
    public static final String ATTENDEE_AVG_RATING = "ATTENDEE_AVG_RATING";
    public static final String ATTENDEE_STAR = "ATTENDEE_STAR";
    public static final String ATTENDEE_STATUS = "ATTENDEE_STATUS";
    public static final String ATTENDEE_MENTION_NAME = "ATTENDEE_MENTION_NAME";
    //Agenda Table
    public static final String AGENDA_TABLE_NAME = "AGENDA_TABLE_NAME";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String SESSION_NAME = "SESSION_NAME";
    public static final String SESSION_DESCRIPTION = "SESSION_DESCRIPTION";
    public static final String SESSION_START_TIME = "SESSION_START_TIME";
    public static final String SESSION_END_TIME = "SESSION_END_TIME";
    public static final String SESSION_DATE = "SESSION_DATE";
    public static final String SESSION_EVENT_ID = "SESSION_EVENT_ID";
    public static final String SESSION_STAR = "SESSION_STAR";
    public static final String SESSION_TOTAL_FEEDBACK = "SESSION_TOTAL_FEEDBACK";
    public static final String SESSION_FEEDBACK_COMMENT = "SESSION_FEEDBACK_COMMENT";
    public static final String SESSION_RATED = "SESSION_RATED";
    public static final String AGENDA_VACATION_TABLE_NAME = "AGENDA_VACATION_TABLE_NAME";
    public static final String SESSION_VACATION_ID = "SESSION_VACATION_ID";
    public static final String SESSION_VACATION_NAME = "SESSION_VACATION_NAME";
    public static final String SESSION_VACATION_DESCRIPTION = "SESSION_VACATION_DESCRIPTION";
    public static final String FOLDER_NAME = "FOLDER_NAME";
    public static final String SESSION_EVENT_VACATION_ID = "SESSION_EVENT_VACATION_ID";
    public static final String AGENDA_VACATION_MEDIA_TABLE = "AGENDA_VACATION_MEDIA_TABLE";
    public static final String SESSION_MEDIA_VACATION_ID = "SESSION_MEDIA_VACATION_ID";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";
    public static final String MEDIA_NAME = "MEDIA_NAME";
    public static final String MEDIA_THUMBNAIL = "MEDIA_THUMBNAIL";
    // News Feed Table
    public static final String NEWSFEED_TABLE_NAME = "NEWSFEED_TABLE_NAME";
    public static final String NEWSFEED_ID = "NEWSFEED_ID";
    public static final String NEWSFEED_TYPE = "NEWSFEED_TYPE";
    public static final String NEWSFEED_MEDIAFILE = "NEWSFEED_MEDIAFILE";
    public static final String NEWSFEED_POST_STATUS = "NEWSFEED_POST_STATUS";
    public static final String NEWSFEED_THUMB_IMAGE = "NEWSFEED_THUMB_IMAGE";
    public static final String NEWSFEED_EVENTID = "NEWSFEED_EVENTID";
    public static final String NEWSFEED_POST_DATE = "NEWSFEED_POST_DATE";
    public static final String NEWSFEED_FIRST_NAME = "NEWSFEED_FIRST_NAME";
    public static final String NEWSFEED_LAST_NAME = "NEWSFEED_LAST_NAME";
    public static final String NEWSFEED_COMP_NAME = "NEWSFEED_COMP_NAME";
    public static final String NEWSFEED_DESIGNATION = "NEWSFEED_DESIGNATION";
    public static final String NEWSFEED_PROFILE_PIC = "NEWSFEED_PROFILE_PIC";
    public static final String NEWSFEED_ATTENDEE_ID = "NEWSFEED_ATTENDEE_ID";
    public static final String NEWSFEED_WIDTH = "NEWSFEED_WIDTH";
    public static final String NEWSFEED_HEIGHT = "NEWSFEED_HEIGHT";
    public static final String NEWSFEED_LIKE_FLAG = "NEWSFEED_LIKE_FLAG";
    public static final String NEWSFEED_TOTAL_LIKES = "NEWSFEED_TOTAL_LIKES";
    public static final String NEWSFEED_TOTAL_COMMENTS = "NEWSFEED_TOTAL_COMMENTS";
    public static final String NEWSFEED_ATTENDEE_TYPE = "NEWSFEED_ATTENDEE_TYPE";
    public static final String NEWSFEED_CITY = "NEWSFEED_CITY";

    public static final String QUIZ_TABLE = "QUIZ_TABLE";

    public static final String QUIZ_ID = "QUIZ_ID";
    public static final String QUESTION = "NEWSFEED_TYPE";
    public static final String CORRECTANSWERID = "CORRECTANSWERID";
    public static final String FOLDERID = "FOLDERID";
    public static final String FOLDERNAME = "FOLDERNAME";
    public static final String REPLIED = "REPLIED";
    public static final String SELECTED_OPTION = "SELECTED_OPTION";

    public static final String EXHIBITOR_CATEGORY_MASTER_LIST = "EXHIBITOR_CATEGORY_MASTER_LIST";

    public static final String EXHIBITOR_CATEGORY_ID = "EXHIBITOR_CATEGORY_ID";
    public static final String EXHIBITOR_NAME = "EXHIBITOR_NAME";
    public static final String TOTAL_EXHIBITOR_COUNT = "TOTAL_EXHIBITOR_COUNT";

    public static final String EXHIBITOR_CATEGORY_LIST = "EXHIBITOR_CATEGORY_LIST";

    public static final String EXHIBITOR_CATEGORY_ID_EX = "EXHIBITOR_CATEGORY_ID_EX";
    public static final String EXHIBITOR_NAME_EX = "EXHIBITOR_NAME_EX";
    public static final String EXHIBITOR_ID = "EXHIBITOR_ID";

    public static final String EXHIBITOR_ATTENDEE_LIST = "EXHIBITOR_ATTENDEE_LIST";

    public static final String ATTENDEE_EXHIBITOR_ID = "ATTENDEE_EXHIBITOR_ID";
    public static final String EX_ATTENDEE_ID = "EX_ATTENDEE_ID";
    public static final String EX_FIRST_NAME = "EX_FIRST_NAME";

    public static final String EX_LAST_NAME = "EX_LAST_NAME";

    public static final String EX_PROFILE_PIC = "EX_PROFILE_PIC";
    public static final String EX_CITY = "EX_CITY";

    public static final String EXHIBITOR_BROCHURE_LIST = "EXHIBITOR_BROCHURE_LIST";

    public static final String BROCHURE_ID = "BROCHURE_ID";
    public static final String B_ID = "B_ID";
    public static final String BROCHER_EXHIBITOR_ID = "BROCHER_EXHIBITOR_ID";

    public static final String B_EVENT_ID = "B_EVENT_ID";

    public static final String BROCHURE_TITLE = "BROCHURE_TITLE";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String FILE_TYPE = "FILE_TYPE";

    public static final String B_CREATED = "B_CREATED";

    public static final String B_MODIFIED = "B_MODIFIED";

    public static final String NOTIFICATION_LIST = "NOTIFICATION_LIST";

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static final String SUBJECT_ID = "SUBJECT_ID";

    public static final String SUBJECT_TYPE = "SUBJECT_TYPE";

    public static final String OBJECT_ID = "OBJECT_ID";
    public static final String OBJECT_TYPE = "OBJECT_TYPE";

    public static final String READ = "READ";

    public static final String NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String EVENT_ID = "EVENT_ID";

    public static final String NOTIFICATION_DATE = "NOTIFICATION_DATE";

    public static final String NOTIATTENDEE_ID = "NOTIATTENDEE_ID";
    public static final String NOTIATTENDEE_FIRST_NAME = "NOTIATTENDEE_FIRST_NAME";
    public static final String NOTIATTENDEE_LAST_NAME = "NOTIATTENDEE_LAST_NAME";

    public static final String COMPANY_NAME = "COMPANY_NAME";

    public static final String DESIGNATION = "DESIGNATION";
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String NOTIFICATION_POST_ID = "NOTIFICATION_POST_ID";

    public static final String EX_NOTIFICATION_LIST = "EX_NOTIFICATION_LIST";

    public static final String EX_NOTIFICATION_ID = "EX_NOTIFICATION_ID";
    public static final String EX_NOTIFICATION_TYPE = "EX_NOTIFICATION_TYPE";
    public static final String EX_SUBJECT_ID = "EX_SUBJECT_ID";

    public static final String EX_SUBJECT_TYPE = "EX_SUBJECT_TYPE";

    public static final String EX_OBJECT_ID = "EX_OBJECT_ID";
    public static final String EX_OBJECT_TYPE = "EX_OBJECT_TYPE";

    public static final String EX_READ = "EX_READ";

    public static final String EX_NOTIFICATION_CONTENT = "EX_NOTIFICATION_CONTENT";
    public static final String EX_MESSAGE_ID = "EX_MESSAGE_ID";
    public static final String EX_EVENT_ID = "EX_EVENT_ID";

    public static final String EX_NOTIFICATION_DATE = "EX_NOTIFICATION_DATE";

    public static final String EX_NOTIATTENDEE_ID = "EX_NOTIATTENDEE_ID";
    public static final String EX_NOTIATTENDEE_FIRST_NAME = "EX_NOTIATTENDEE_FIRST_NAME";
    public static final String EX_NOTIATTENDEE_LAST_NAME = "EX_NOTIATTENDEE_LAST_NAME";

    public static final String EX_COMPANY_NAME = "EX_COMPANY_NAME";

    public static final String EX_DESIGNATION = "EX_DESIGNATION";
    public static final String EXI_PROFILE_PIC = "EX_PROFILE_PIC";
    public static final String EX_EVENT_NAME = "EX_EVENT_NAME";
    public static final String EX_NOTIFICATION_POST_ID = "EX_NOTIFICATION_POST_ID";


    public static final String EX_MEETING_TABLE = "EX_MEETING_TABLE";

    public static final String EX_MEETING_ID = "EX_MEETING_ID";
    public static final String MEETING_EXHIBITOR_ID = "MEETING_EXHIBITOR_ID";
    public static final String MEETING_ATTENDEE_ID = "MEETING_ATTENDEE_ID";

    public static final String MEETING_EVENT_ID = "MEETING_EVENT_ID";

    public static final String MEETING_DATE_TIME = "MEETING_DATE_TIME";
    public static final String MEETING_STATUS = "MEETING_STATUS";

    public static final String MEETING_DESCRIPTION = "MEETING_DESCRIPTION";

    public static final String MEETING_CREATED = "MEETING_CREATED";
    public static final String MEETING_MODIFIED = "MEETING_MODIFIED";
    public static final String MEETING_FIRST_NAME = "MEETING_FIRST_NAME";

    public static final String MEETING_LAST_NAME = "MEETING_LAST_NAME";

    public static final String MEETING_PROFILE_PIC = "MEETING_PROFILE_PIC";


    public static final String EVENTINFO_TABLE = "EVENTINFO_TABLE";
    public static final String SPONSOR_TABLE = "SPONSOR_TABLE";

    public static final String EVENTINFO_ID = "EVENT_ID";
    public static final String EVENTINFO_NAME = "EVENTINFO_NAME";
    public static final String EVENT_DESCRIPTION = "EVENT_DESCRIPTION";

    public static final String SPONSOR_ID = "SPONSOR_ID";
    public static final String SPONSOR_NAME = "SPONSOR_NAME";
    public static final String SPONSOR_LOGO = "SPONSOR_LOGO";

    public static final String EVENT_START = "EVENT_START";

    public static final String EVENT_END = "EVENT_END";
    public static final String EVENT_LOCATION = "EVENT_LOCATION";

    public static final String EVENT_CITY = "EVENT_CITY";

    public static final String EVENT_COUNTRY = "EVENT_COUNTRY";
    public static final String EVENT_LATITUDE = "EVENT_LATITUDE";
    public static final String EVENT_LONGITUDE = "EVENT_LONGITUDE";


    public static final String UPLOAD_MULTIMEDIA_TABLE = "TABLE_UPLOAD_MULTIMEDIA";
    public static final String MULTIMEDIA_ID = "MULTIMEDIA_ID";
    public static final String MULTIMEDIA_FILE = "MULTIMEDIA_FILE";
    public static final String MULTIMEDIA_COMPRESSED_FILE = "MULTIMEDIA_COMPRESSED_FILE";
    public static final String MULTIMEDIA_THUMB = "MULTIMEDIA_THUMB";
    public static final String NEWS_FEED_ID = "NEWS_FEED_ID";
    public static final String IS_UPLOADED = "IS_UPLOADED";
    public static final String MULTIMEDIA_TYPE = "MULTIMEDIA_TYPE";
    public static final String FOLDER_UNIQUE_ID = "FOLDER_UNIQUE_ID";


    public static final String BUZZ_MEDIA_TABLE_NAME = "BUZZ_MEDIA_TABLE_NAME";
    public static final String BUZZ_FEED_ID = "BUZZ_FEED_ID";
    public static final String BUZZ_MEDIA_TYPE = "BUZZ_MEDIA_TYPE";
    public static final String BUZZ_MEDIA_FILE = "BUZZ_MEDIA_FILE";
    public static final String BUZZ_THUMB_IMAGE = "BUZZ_THUMB_IMAGE";
    public static final String BUZZ_WIDTH = "BUZZ_WIDTH";
    public static final String BUZZ_HEIGHT = "BUZZ_HEIGHT";
    public static final String BUZZ_MEDIA_IMAGE = "BUZZ_MEDIA_IMAGE";
    public static final String BUZZ_MEDIA_ID = "BUZZ_MEDIA_ID";

    public static final String LOGO = "LOGO";

    //Buddy Chat --------------------------------------------------------------
    public static final String BUDDY_CHAT_TABLE_NAME = "BUDDY_CHAT_TABLE_NAME";
     public static final String BUDDY_CHAT_ID = "BUDDY_CHAT_ID";
    public static final String BUDDY_CHAT_SENDER_ID = "BUDDY_CHAT_SENDER_ID";
    public static final String BUDDY_CHAT_RECEIVER_ID = "BUDDY_CHAT_RECEIVER_ID";
    public static final String BUDDY_CHAT_MESSAGE = "BUDDY_CHAT_MESSAGE";
    public static final String BUDDY_CHAT_TIMESTAMP = "BUDDY_CHAT_TIMESTAMP";
    public static final String BUDDY_CHAT_STATUS = "BUDDY_CHAT_STATUS";

    //Buddy Chat Count--------------------------------------------------------------
    public static final String BUDDY_CHAT_COUNT_TABLE_NAME = "BUDDY_CHAT_COUNT_TABLE_NAME";
   // public static final String BUDDY_CHAT_ID = "BUDDY_CHAT_ID";
    public static final String BUDDY_CHAT_COUNT_SENDER_ID = "BUDDY_CHAT_COUNT_SENDER_ID";
    //public static final String BUDDY_CHAT_RECEIVER_ID = "BUDDY_CHAT_RECEIVER_ID";
    public static final String BUDDY_CHAT_COUNT_MESSAGE = "BUDDY_CHAT_COUNT_MESSAGE";
    /*public static final String BUDDY_CHAT_TIMESTAMP = "BUDDY_CHAT_TIMESTAMP";
    public static final String BUDDY_CHAT_STATUS = "BUDDY_CHAT_STATUS";*/
    public static final String BUDDY_CHAT_COUNT_IS_READ = "BUDDY_CHAT_COUNT_IS_READ";

    //Attendee Chat --------------------------------------------------------------
    public static final String ATTENDEE_CHAT_TABLE_NAME = "ATTENDEE_CHAT_TABLE_NAME";
    public static final String ATTENDEE_CHAT_ID = "ATTENDEE_CHAT_ID";
    public static final String ATTENDEE_CHAT_SENDER_ID = "ATTENDEE_CHAT_SENDER_ID";
    public static final String ATTENDEE_CHAT_RECEIVER_ID = "ATTENDEE_CHAT_RECEIVER_ID";
    public static final String ATTENDEE_CHAT_MESSAGE = "ATTENDEE_CHAT_MESSAGE";
    public static final String ATTENDEE_CHAT_TIMESTAMP = "ATTENDEE_CHAT_TIMESTAMP";
    public static final String ATTENDEE_CHAT_STATUS = "ATTENDEE_CHAT_STATUS";

    //Attendee Chat Count--------------------------------------------------------------
    public static final String ATTENDEE_CHAT_COUNT_TABLE_NAME = "ATTENDEE_CHAT_COUNT_TABLE_NAME";
    //public static final String ATTENDEE_CHAT_ID = "ATTENDEE_CHAT_ID";
    public static final String ATTENDEE_CHAT_COUNT_SENDER_ID = "ATTENDEE_CHAT_COUNT_SENDER_ID";
    //public static final String ATTENDEE_CHAT_RECEIVER_ID = "ATTENDEE_CHAT_RECEIVER_ID";
    public static final String ATTENDEE_CHAT_COUNT_MESSAGE = "ATTENDEE_CHAT_COUNT_MESSAGE";
    /*public static final String ATTENDEE_CHAT_TIMESTAMP = "ATTENDEE_CHAT_TIMESTAMP";
    public static final String ATTENDEE_CHAT_STATUS = "ATTENDEE_CHAT_STATUS";*/
    public static final String ATTENDEE_CHAT_COUNT_IS_READ = "ATTENDEE_CHAT_COUNT_IS_READ";

    //Buddy List
    public static final String BUDDY_TABLE_NAME = "BUDDY_TABLE_NAME";
    public static final String BUDDY_FRIEND_ID = "BUDDY_FRIEND_ID";
    public static final String BUDDY_REQUEST_TYPE = "BUDDY_REQUEST_TYPE";
    public static final String BUDDY_FNAME = "BUDDY_FNAME";
    public static final String BUDDY_LNAME = "BUDDY_LNAME";
    public static final String BUDDY_DESIGNATION = "BUDDY_DESIGNATION";
    public static final String BUDDY_CITY = "BUDDY_CITY";
    public static final String BUDDY_PROFILE_PIC = "BUDDY_PROFILE_PIC";


    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sInstance;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB", "DB Created");
    }

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating Attendees table
        db.execSQL("create table " + ATTENDEES_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_API_ACCESS_TOKEN + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME
                + " text, " + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE
                + " text, " + ATTENDEE_EMAIL + " text, "
                + ATTENDEE_COMPANY_NAME + " text, " + ATTENDEE_DESIGNATION
                + " text, " + ATTENDEE_TYPE + " text, "
                + BUDDY_STATUS + " text, "
                + CHAT_DATA_COUNT + " text, "
                + ATTENDEE_MENTION_NAME + " text)");

        // Creating User data table
        db.execSQL("create table " + USER_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME + " text, " + ATTENDEE_API_ACCESS_TOKEN
                + " text, " + ATTENDEE_EMAIL + " text, " + ATTENDEE_COMPANY_NAME
                + " text, " + ATTENDEE_DESIGNATION + " text, "
                + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE + " text," + ATTENDEE_STATUS + "text)");

        // Creating Agenda table
        db.execSQL("create table " + AGENDA_TABLE_NAME + "(" + SESSION_ID
                + " text, " + SESSION_NAME + " text, " + SESSION_DESCRIPTION + " text, " + SESSION_START_TIME
                + " text, " + SESSION_END_TIME + " text, " + SESSION_DATE
                + " text, " + SESSION_EVENT_ID + " text, "
                + SESSION_STAR + " text, " + SESSION_TOTAL_FEEDBACK
                + " text, " + SESSION_FEEDBACK_COMMENT + " text, "
                + SESSION_RATED + " text)");

        //Creating Agenda Vacation table
        db.execSQL("create table " + AGENDA_VACATION_TABLE_NAME + "(" + SESSION_VACATION_ID
                + " text, " + SESSION_VACATION_NAME + " text, " + SESSION_VACATION_DESCRIPTION + " text, " + FOLDER_NAME
                + " text, " + SESSION_EVENT_VACATION_ID + " text)");

        //create Agenda Media table
        db.execSQL("create table " + AGENDA_VACATION_MEDIA_TABLE + "(" + SESSION_MEDIA_VACATION_ID
                + " text, " + MEDIA_NAME + " text, " + MEDIA_TYPE + " text, " + MEDIA_THUMBNAIL
                + " text)");

        // Creating Speaker table
        db.execSQL("create table " + SPEAKER_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_API_ACCESS_TOKEN + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME
                + " text, " + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE
                + " text, " + ATTENDEE_EMAIL + " text, "
                + ATTENDEE_COMPANY_NAME + " text, " + ATTENDEE_DESIGNATION
                + " text, "
                + ATTENDEE_TYPE + " text, "
                + ATTENDEE_TOTAL_RATING + " text, "
                + ATTENDEE_AVG_RATING + " text, "
                + ATTENDEE_STAR + " text)");

        //Creating News Feed table

        db.execSQL("create table " + NEWSFEED_TABLE_NAME + "(" + NEWSFEED_ID
                + " text, " + NEWSFEED_TYPE + " text, " + NEWSFEED_MEDIAFILE + " text, " + NEWSFEED_POST_STATUS
                + " text, " + NEWSFEED_THUMB_IMAGE + " text, " + NEWSFEED_EVENTID
                + " text, " + NEWSFEED_POST_DATE + " text, "
                + NEWSFEED_FIRST_NAME + " text, " + NEWSFEED_LAST_NAME
                + " text, " + NEWSFEED_COMP_NAME + " text, "
                + NEWSFEED_DESIGNATION + " text, " + NEWSFEED_PROFILE_PIC
                + " text, "
                + NEWSFEED_ATTENDEE_ID + " text, "
                + NEWSFEED_WIDTH + " text, "
                + NEWSFEED_HEIGHT + " text, "
                + NEWSFEED_LIKE_FLAG + " text, "
                + NEWSFEED_TOTAL_LIKES + " text, "
                + NEWSFEED_TOTAL_COMMENTS + " text,"
                + NEWSFEED_ATTENDEE_TYPE + " text,"
                + NEWSFEED_CITY + " text)");

        db.execSQL("create table " + QUIZ_TABLE + "(" + QUIZ_ID
                + " text, " + QUESTION + " text, " + CORRECTANSWERID + " text, " + FOLDERNAME + " text, " + REPLIED + " text, " + SELECTED_OPTION
                + " text)");


        db.execSQL("create table " + EXHIBITOR_CATEGORY_MASTER_LIST + "(" + EXHIBITOR_CATEGORY_ID
                + " text, " + EXHIBITOR_NAME + " text, " + TOTAL_EXHIBITOR_COUNT + " text)");

        db.execSQL("create table " + EXHIBITOR_CATEGORY_LIST + "(" + EXHIBITOR_CATEGORY_ID_EX
                + " text, " + EXHIBITOR_NAME_EX + " text, " + EXHIBITOR_ID + " text)");

        db.execSQL("create table " + EXHIBITOR_ATTENDEE_LIST + "(" + ATTENDEE_EXHIBITOR_ID
                + " text, " + EX_ATTENDEE_ID + " text, " + EX_FIRST_NAME + " text, " + EX_LAST_NAME + " text, "
                + EX_PROFILE_PIC + " text, " + EX_CITY + " text)");


        db.execSQL("create table " + EXHIBITOR_BROCHURE_LIST + "(" + BROCHURE_ID
                + " text, " + B_ID + " text, " + BROCHER_EXHIBITOR_ID + " text, " + B_EVENT_ID + " text, "
                + BROCHURE_TITLE + " text, " + FILE_NAME + " text, " + FILE_TYPE + " text, "
                + B_CREATED + " text, " + B_MODIFIED + " text)");


        db.execSQL("create table " + NOTIFICATION_LIST + "(" + NOTIFICATION_ID
                + " text, " + NOTIFICATION_TYPE + " text, " + SUBJECT_ID + " text, " + SUBJECT_TYPE + " text, "
                + OBJECT_ID + " text, " + OBJECT_TYPE + " text, " + READ + " text, "
                + NOTIFICATION_CONTENT + " text, " + MESSAGE_ID + " text, " + EVENT_ID + " text, " + NOTIFICATION_DATE + " text, " + NOTIATTENDEE_ID + " text,"
                + NOTIATTENDEE_FIRST_NAME + " text, " + NOTIATTENDEE_LAST_NAME + " text, " + COMPANY_NAME + " text, " + DESIGNATION + " text, " + PROFILE_PIC + " text,"
                + EVENT_NAME + " text, " + NOTIFICATION_POST_ID + " text)");

        db.execSQL("create table " + EX_NOTIFICATION_LIST + "(" + EX_NOTIFICATION_ID
                + " text, " + EX_NOTIFICATION_TYPE + " text, " + EX_SUBJECT_ID + " text, " + EX_SUBJECT_TYPE + " text, "
                + EX_OBJECT_ID + " text, " + EX_OBJECT_TYPE + " text, " + EX_READ + " text, "
                + EX_NOTIFICATION_CONTENT + " text, " + EX_MESSAGE_ID + " text, " + EX_EVENT_ID + " text, " + EX_NOTIFICATION_DATE + " text, " + EX_NOTIATTENDEE_ID + " text,"
                + EX_NOTIATTENDEE_FIRST_NAME + " text, " + EX_NOTIATTENDEE_LAST_NAME + " text, " + EX_COMPANY_NAME + " text, " + EX_DESIGNATION + " text, " + EXI_PROFILE_PIC + " text,"
                + EX_EVENT_NAME + " text, " + EX_NOTIFICATION_POST_ID + " text)");


        db.execSQL("create table " + EX_MEETING_TABLE + "(" + EX_MEETING_ID
                + " text, " + MEETING_EXHIBITOR_ID + " text, " + MEETING_ATTENDEE_ID + " text, " + MEETING_EVENT_ID + " text, "
                + MEETING_DATE_TIME + " text, " + MEETING_STATUS + " text, " + MEETING_DESCRIPTION + " text, "
                + MEETING_CREATED + " text, " + MEETING_MODIFIED + " text, " + MEETING_FIRST_NAME + " text, " + MEETING_LAST_NAME + " text, " + MEETING_PROFILE_PIC + " text)");


        db.execSQL("create table " + EVENTINFO_TABLE + "(" + EVENTINFO_ID
                + " text, " + EVENTINFO_NAME + " text, " + EVENT_DESCRIPTION + " text, " + EVENT_START + " text, "
                + EVENT_END + " text, " + EVENT_LOCATION + " text, " + EVENT_CITY + " text, "
                + EVENT_COUNTRY + " text, " + EVENT_LATITUDE + " text, " + EVENT_LONGITUDE + " text, " + LOGO + " text)");

        db.execSQL("create table " + SPONSOR_TABLE + "(" + SPONSOR_ID
                + " text, " + SPONSOR_NAME + " text, " + SPONSOR_LOGO + " text)");


        try {
            // UPLOAD Multimedia Table
            db.execSQL("create table " + UPLOAD_MULTIMEDIA_TABLE + "(" +
                    MULTIMEDIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MULTIMEDIA_FILE + " text, " +
                    MULTIMEDIA_THUMB + " text, " +
                    MULTIMEDIA_COMPRESSED_FILE + " text, " +
                    MULTIMEDIA_TYPE + " text, " +
                    NEWS_FEED_ID + " text, " +
                    FOLDER_UNIQUE_ID + " text, " +
                    IS_UPLOADED + " text)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.execSQL("create table " + BUZZ_MEDIA_TABLE_NAME + "(" + BUZZ_FEED_ID
                + " text, " + BUZZ_MEDIA_TYPE + " text, "
                + BUZZ_MEDIA_FILE + " text, "
                + BUZZ_THUMB_IMAGE + " text, "
                + BUZZ_WIDTH + " text, "
                + BUZZ_MEDIA_IMAGE + " blob, "
                + BUZZ_HEIGHT + " text, "
                + BUZZ_MEDIA_ID + " text)");

        db.execSQL("create table " + BUDDY_CHAT_TABLE_NAME + "("
                + BUDDY_CHAT_ID + " text, "
                + BUDDY_CHAT_SENDER_ID + " text, "
                + BUDDY_CHAT_RECEIVER_ID + " text, "
                + BUDDY_CHAT_MESSAGE + " text, "
                + BUDDY_CHAT_TIMESTAMP + " text, "
                + BUDDY_CHAT_STATUS + " text)");

        db.execSQL("create table " + ATTENDEE_CHAT_TABLE_NAME + "("
                + ATTENDEE_CHAT_ID + " text, "
                + ATTENDEE_CHAT_SENDER_ID + " text, "
                + ATTENDEE_CHAT_RECEIVER_ID + " text, "
                + ATTENDEE_CHAT_MESSAGE + " text, "
                + ATTENDEE_CHAT_TIMESTAMP + " text, "
                + ATTENDEE_CHAT_STATUS + " text)");

db.execSQL("create table " + BUDDY_CHAT_COUNT_TABLE_NAME + "("
                //+ BUDDY_CHAT_ID + " text, "
                + BUDDY_CHAT_COUNT_SENDER_ID + " text, "
                //+ BUDDY_CHAT_RECEIVER_ID + " text, "
                + BUDDY_CHAT_COUNT_MESSAGE + " text, "
               // + BUDDY_CHAT_TIMESTAMP + " text, "
                //+ BUDDY_CHAT_STATUS + " text, "
                + BUDDY_CHAT_COUNT_IS_READ + " text)");

        db.execSQL("create table " + ATTENDEE_CHAT_COUNT_TABLE_NAME + "("
                //+ ATTENDEE_CHAT_ID + " text, "
                + ATTENDEE_CHAT_COUNT_SENDER_ID + " text, "
                //+ ATTENDEE_CHAT_RECEIVER_ID + " text, "
                + ATTENDEE_CHAT_COUNT_MESSAGE + " text, "
               /* + ATTENDEE_CHAT_TIMESTAMP + " text, "
                + ATTENDEE_CHAT_STATUS + " text, "*/
                + ATTENDEE_CHAT_COUNT_IS_READ + " text)");

        db.execSQL("create table " + BUDDY_TABLE_NAME + "("
                + BUDDY_FRIEND_ID + " text, "
                + BUDDY_REQUEST_TYPE + " text, "
                + BUDDY_FNAME + " text, "
                + BUDDY_LNAME + " text, "
                + BUDDY_DESIGNATION + " text, "
                + BUDDY_CITY + " text, "
                + BUDDY_PROFILE_PIC + " text)");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DELETE FROM " + ATTENDEES_TABLE_NAME);
            db.execSQL("DELETE FROM " + SPEAKER_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_VACATION_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_VACATION_MEDIA_TABLE);
            db.execSQL("DELETE FROM " + NEWSFEED_TABLE_NAME);
            db.execSQL("DELETE FROM " + QUIZ_TABLE);
            db.execSQL("DELETE FROM " + EXHIBITOR_CATEGORY_MASTER_LIST);
            db.execSQL("DELETE FROM " + EXHIBITOR_CATEGORY_LIST);
            db.execSQL("DELETE FROM " + EXHIBITOR_ATTENDEE_LIST);
            db.execSQL("DELETE FROM " + EXHIBITOR_BROCHURE_LIST);
            db.execSQL("DELETE FROM " + NOTIFICATION_LIST);
            db.execSQL("DELETE FROM " + USER_TABLE_NAME);
            db.execSQL("DELETE FROM " + EVENTINFO_TABLE);
            db.execSQL("DELETE FROM " + SPONSOR_TABLE);
            db.execSQL("DELETE FROM " + UPLOAD_MULTIMEDIA_TABLE);
            db.execSQL("DELETE FROM " + BUDDY_TABLE_NAME);


            db.execSQL("DROP TABLE IF EXISTS " + ATTENDEES_TABLE_NAME);

            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //Insert value in User Table
    public void insertUserDataInfo(List<UserData> UsersList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < UsersList.size(); i++) {
                contentValues = new ContentValues();

                String attendee_id = UsersList.get(i).getAttendeeId();
                if (attendee_id != null && attendee_id.length() > 0) {
                    contentValues.put(ATTENDEE_ID, attendee_id);
                }

                String api_access_token = UsersList.get(i).getApiAccessToken();
                if (api_access_token != null && api_access_token.length() > 0) {
                    contentValues.put(ATTENDEE_FIRST_NAME, api_access_token);
                }

                String first_name = UsersList.get(i).getFirstName();
                if (first_name != null && first_name.length() > 0) {
                    contentValues.put(ATTENDEE_LAST_NAME, first_name);
                }

                String last_name = UsersList.get(i).getLastName();
                if (last_name != null && last_name.length() > 0) {
                    contentValues.put(ATTENDEE_API_ACCESS_TOKEN, last_name);
                }

                String attendee_description = UsersList.get(i).getDescription();
                if (attendee_description != null
                        && attendee_description.length() > 0) {
                    contentValues.put(ATTENDEE_EMAIL,
                            attendee_description);
                }

                String attendee_city = UsersList.get(i).getCity();
                if (attendee_city != null && attendee_city.length() > 0) {
                    contentValues.put(ATTENDEE_COMPANY_NAME, attendee_city);
                }

                String attendee_country = UsersList.get(i).getCountry();
                if (attendee_country != null
                        && attendee_country.length() > 0) {
                    contentValues.put(ATTENDEE_DESIGNATION, attendee_country);
                }

                String profile_pic = UsersList.get(i).getProfilePic();
                if (profile_pic != null && profile_pic.length() > 0) {
                    contentValues.put(ATTENDEE_DESCRIPTION, profile_pic);
                }

                String mobile = UsersList.get(i).getMobile();
                if (mobile != null && mobile.length() > 0) {
                    contentValues.put(ATTENDEE_CITY, mobile);
                }

                String email = UsersList.get(i).getEmail();
                if (email != null && email.length() > 0) {
                    contentValues.put(ATTENDEE_COUNTRY, email);
                }

                String attendee_company = UsersList.get(i).getCompanyName();
                if (attendee_company != null
                        && attendee_company.length() > 0) {
                    contentValues.put(ATTENDEE_PROFILE_PIC,
                            attendee_company);
                }

                String attendee_designation = UsersList.get(i)
                        .getDesignation();
                if (attendee_designation != null
                        && attendee_designation.length() > 0) {
                    contentValues.put(ATTENDEE_MOBILE,
                            attendee_designation);
                }

                String attendee_status = UsersList.get(i)
                        .getAttendee_status();
                if (attendee_status != null
                        && attendee_status.length() > 0) {
                    contentValues.put(ATTENDEE_STATUS,
                            attendee_status);
                }
                db.insert(USER_TABLE_NAME, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    //Insert Buddy chat into table for unreadcount
    public void insertBuddyChat(List<chat_list> UsersList,
                                     SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < UsersList.size(); i++) {
            contentValues = new ContentValues();

            String strId = UsersList.get(i).getId();
            if (strId != null && strId.length() > 0) {
                contentValues.put(BUDDY_CHAT_ID, strId);
            }
            String strSender_id = UsersList.get(i).getSender_id();
            if (strSender_id != null && strSender_id.length() > 0) {
                contentValues.put(BUDDY_CHAT_SENDER_ID, strSender_id);
            }
            String strReceiver_id = UsersList.get(i).getReceiver_id();
            if (strReceiver_id != null && strReceiver_id.length() > 0) {
                contentValues.put(BUDDY_CHAT_RECEIVER_ID, strReceiver_id);
            }
            String strMessage = UsersList.get(i).getMessage();
            if (strMessage != null && strMessage.length() > 0) {
                contentValues.put(BUDDY_CHAT_MESSAGE, strMessage);
            }
            String strTimestamp = UsersList.get(i).getTimestamp();
            if (strTimestamp != null && strTimestamp.length() > 0) {
                contentValues.put(BUDDY_CHAT_TIMESTAMP, strTimestamp);
            }
            String strStatus = UsersList.get(i).getStatus();
            if (strStatus != null && strStatus.length() > 0) {
                contentValues.put(BUDDY_CHAT_STATUS, strStatus);
            }
            db.insert(BUDDY_CHAT_TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    //Insert Buddy chat into table for unreadcount
    public void insertAttendeeChat(List<chat_list> UsersList,
                                     SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < UsersList.size(); i++) {
            contentValues = new ContentValues();

            String strId = UsersList.get(i).getId();
            if (strId != null && strId.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_ID, strId);
            }
            String strSender_id = UsersList.get(i).getSender_id();
            if (strSender_id != null && strSender_id.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_SENDER_ID, strSender_id);
            }
            String strReceiver_id = UsersList.get(i).getReceiver_id();
            if (strReceiver_id != null && strReceiver_id.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_RECEIVER_ID, strReceiver_id);
            }
            String strMessage = UsersList.get(i).getMessage();
            if (strMessage != null && strMessage.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_MESSAGE, strMessage);
            }
            String strTimestamp = UsersList.get(i).getTimestamp();
            if (strTimestamp != null && strTimestamp.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_TIMESTAMP, strTimestamp);
            }
            String strStatus = UsersList.get(i).getStatus();
            if (strStatus != null && strStatus.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_STATUS, strStatus);
            }
            db.insert(ATTENDEE_CHAT_TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public boolean deleteBuddyChat(String receiver_id,String sender_id)
    {
        String deleteSql = "DELETE FROM "+BUDDY_CHAT_TABLE_NAME+" where ("+BUDDY_CHAT_RECEIVER_ID +"='"+receiver_id
                +"' and "+BUDDY_CHAT_SENDER_ID+"='"+sender_id+"') OR ("+BUDDY_CHAT_RECEIVER_ID +"='"+sender_id
                +"' and "+BUDDY_CHAT_SENDER_ID+"='"+receiver_id+"')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteSql);
        return true;
    }

    public boolean deleteAttendeeChat(String receiver_id,String sender_id)
    {
        String deleteSql = "DELETE FROM "+ATTENDEE_CHAT_TABLE_NAME+" where ("+ATTENDEE_CHAT_RECEIVER_ID +"='"+receiver_id
                +"' and "+ATTENDEE_CHAT_SENDER_ID+"='"+sender_id+"') OR ("+ATTENDEE_CHAT_RECEIVER_ID +"='"+sender_id
                +"' and "+ATTENDEE_CHAT_SENDER_ID+"='"+receiver_id+"')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteSql);
        return true;
    }

    public List<chat_list> getBuddyChat(String receiver_id,String sender_id) {
        String selectQuery = "select * from " + BUDDY_CHAT_TABLE_NAME + " where ("+BUDDY_CHAT_RECEIVER_ID +"='"+receiver_id
                +"' and "+BUDDY_CHAT_SENDER_ID+"='"+sender_id+"') OR ("+BUDDY_CHAT_RECEIVER_ID +"='"+sender_id
                        +"' and "+BUDDY_CHAT_SENDER_ID+"='"+receiver_id+"')";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<chat_list> questionList = new ArrayList<chat_list>();

        if (cursor.moveToFirst()) {
            do {

                chat_list chatList = new chat_list();
               chatList.setId(cursor.getString(0));
               chatList.setSender_id(cursor.getString(1));
               chatList.setReceiver_id(cursor.getString(2));
               chatList.setMessage(cursor.getString(3));
               chatList.setTimestamp(cursor.getString(4));
               chatList.setStatus(cursor.getString(5));

                questionList.add(chatList);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public List<chat_list> getAttendeeChat(String receiver_id,String sender_id) {
        String selectQuery = "select * from " + ATTENDEE_CHAT_TABLE_NAME + " where ("+ATTENDEE_CHAT_RECEIVER_ID +"='"+receiver_id
                +"' and "+ATTENDEE_CHAT_SENDER_ID+"='"+sender_id+"') OR ("+ATTENDEE_CHAT_RECEIVER_ID +"='"+sender_id
                        +"' and "+ATTENDEE_CHAT_SENDER_ID+"='"+receiver_id+"')";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<chat_list> questionList = new ArrayList<chat_list>();

        if (cursor.moveToFirst()) {
            do {

                chat_list chatList = new chat_list();
               chatList.setId(cursor.getString(0));
               chatList.setSender_id(cursor.getString(1));
               chatList.setReceiver_id(cursor.getString(2));
               chatList.setMessage(cursor.getString(3));
               chatList.setTimestamp(cursor.getString(4));
               chatList.setStatus(cursor.getString(5));

                questionList.add(chatList);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    //Insert Buddy chat count into table for unreadcount
    public void insertBuddyChatCount(chat_list_db UsersList,
                                     SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            //for (int i = 0; i < UsersList.size(); i++) {
            contentValues = new ContentValues();

            /*String strId = UsersList.getId();
            if (strId != null && strId.length() > 0) {
                contentValues.put(BUDDY_CHAT_ID, strId);
            }*/
            String strSender_id = UsersList.getSender_id();
            if (strSender_id != null && strSender_id.length() > 0) {
                contentValues.put(BUDDY_CHAT_COUNT_SENDER_ID, strSender_id);
            }
            /*String strReceiver_id = UsersList.getReceiver_id();
            if (strReceiver_id != null && strReceiver_id.length() > 0) {
                contentValues.put(BUDDY_CHAT_RECEIVER_ID, strReceiver_id);
            }*/
            String strMessage = UsersList.getMessage();
            if (strMessage != null && strMessage.length() > 0) {
                contentValues.put(BUDDY_CHAT_COUNT_MESSAGE, strMessage);
            }
            /*String strTimestamp = UsersList.getTimestamp();
            if (strTimestamp != null && strTimestamp.length() > 0) {
                contentValues.put(BUDDY_CHAT_TIMESTAMP, strTimestamp);
            }
            String strStatus = UsersList.getStatus();
            if (strStatus != null && strStatus.length() > 0) {
                contentValues.put(BUDDY_CHAT_STATUS, strStatus);
            }*/
            String strIsRead = "0";
            if (strIsRead != null && strIsRead.length() > 0) {
                contentValues.put(BUDDY_CHAT_COUNT_IS_READ, strIsRead);
            }
            db.insert(BUDDY_CHAT_COUNT_TABLE_NAME, null, contentValues);

            //}
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    //Insert ATTENDEE chat into table for unreadcount
    public void insertAttendeeChat(chat_list_db UsersList,
                                SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            //for (int i = 0; i < UsersList.size(); i++) {
            contentValues = new ContentValues();

            /*String strId = UsersList.getId();
            if (strId != null && strId.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_ID, strId);
            }*/
            String strSender_id = UsersList.getSender_id();
            if (strSender_id != null && strSender_id.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_COUNT_SENDER_ID, strSender_id);
            }
            /*String strReceiver_id = UsersList.getReceiver_id();
            if (strReceiver_id != null && strReceiver_id.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_RECEIVER_ID, strReceiver_id);
            }*/
            String strMessage = UsersList.getMessage();
            if (strMessage != null && strMessage.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_COUNT_MESSAGE, strMessage);
            }
            /*String strTimestamp = UsersList.getTimestamp();
            if (strTimestamp != null && strTimestamp.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_TIMESTAMP, strTimestamp);
            }
            String strStatus = UsersList.getStatus();
            if (strStatus != null && strStatus.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_STATUS, strStatus);
            }*/
            String strIsRead = "0";
            if (strIsRead != null && strIsRead.length() > 0) {
                contentValues.put(ATTENDEE_CHAT_COUNT_IS_READ, strIsRead);
            }
            db.insert(ATTENDEE_CHAT_COUNT_TABLE_NAME, null, contentValues);

            //}
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }



    //Insert bUDDY into table for unreadcount
    public void insertBuddyInfo(List<Buddy> attendeesList,
                                    SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < attendeesList.size(); i++) {
                contentValues = new ContentValues();

                String strId = attendeesList.get(i).getFriend_id();
                if (strId != null && strId.length() > 0) {
                    contentValues.put(BUDDY_FRIEND_ID, strId);
                }
                String strMessage = attendeesList.get(i).getRequest_type();
                if (strMessage != null && strMessage.length() > 0) {
                    contentValues.put(BUDDY_REQUEST_TYPE, strMessage);
                }
                String strSender_id = attendeesList.get(i).getFirstName();
                if (strSender_id != null && strSender_id.length() > 0) {
                    contentValues.put(BUDDY_FNAME, strSender_id);
                }
                String strReceiver_id = attendeesList.get(i).getLastName();
                if (strReceiver_id != null && strReceiver_id.length() > 0) {
                    contentValues.put(BUDDY_LNAME, strReceiver_id);
                }

                String strTimestamp = attendeesList.get(i).getDesignation();
                if (strTimestamp != null && strTimestamp.length() > 0) {
                    contentValues.put(BUDDY_DESIGNATION, strTimestamp);
                }
                String strStatus = attendeesList.get(i).getCity();
                if (strStatus != null && strStatus.length() > 0) {
                    contentValues.put(BUDDY_CITY, strStatus);
                }
                String strIsRead = attendeesList.get(i).getProfilePic();
                if (strIsRead != null && strIsRead.length() > 0) {
                    contentValues.put(BUDDY_PROFILE_PIC, strIsRead);
                }

                db.insert(BUDDY_TABLE_NAME, null, contentValues);

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }



    // Insert Values in Attendee Table
    public void insertAttendeesInfo(List<AttendeeList> attendeesList,
                                    SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < attendeesList.size(); i++) {
                contentValues = new ContentValues();


                String attendee_id = attendeesList.get(i).getAttendeeId();
                if (attendee_id != null && attendee_id.length() > 0) {
                    contentValues.put(ATTENDEE_ID, attendee_id);
                }

                String api_access_token = attendeesList.get(i).getApiAccessToken();
                if (api_access_token != null && api_access_token.length() > 0) {
                    contentValues.put(ATTENDEE_API_ACCESS_TOKEN, api_access_token);
                }

                String first_name = attendeesList.get(i).getFirstName();
                if (first_name != null && first_name.length() > 0) {
                    contentValues.put(ATTENDEE_FIRST_NAME, first_name);
                }

                String last_name = attendeesList.get(i).getLastName();
                if (last_name != null && last_name.length() > 0) {
                    contentValues.put(ATTENDEE_LAST_NAME, last_name);
                }

                String attendee_description = attendeesList.get(i).getDescription();
                if (attendee_description != null
                        && attendee_description.length() > 0) {
                    contentValues.put(ATTENDEE_DESCRIPTION,
                            attendee_description);
                }

                String attendee_city = attendeesList.get(i).getCity();
                if (attendee_city != null && attendee_city.length() > 0) {
                    contentValues.put(ATTENDEE_CITY, attendee_city);
                }

                String attendee_country = attendeesList.get(i).getCountry();
                if (attendee_country != null
                        && attendee_country.length() > 0) {
                    contentValues.put(ATTENDEE_COUNTRY, attendee_country);
                }

                String profile_pic = attendeesList.get(i).getProfilePic();
                if (profile_pic != null && profile_pic.length() > 0) {
                    contentValues.put(ATTENDEE_PROFILE_PIC, profile_pic);
                }

                String mobile = attendeesList.get(i).getMobile();
                if (mobile != null && mobile.length() > 0) {
                    contentValues.put(ATTENDEE_MOBILE, mobile);
                }

                String email = attendeesList.get(i).getEmail();
                if (email != null && email.length() > 0) {
                    contentValues.put(ATTENDEE_EMAIL, email);
                }

                String attendee_company = attendeesList.get(i).getCompanyName();
                if (attendee_company != null
                        && attendee_company.length() > 0) {
                    contentValues.put(ATTENDEE_COMPANY_NAME,
                            attendee_company);
                }

                String attendee_designation = attendeesList.get(i)
                        .getDesignation();
                if (attendee_designation != null
                        && attendee_designation.length() > 0) {
                    contentValues.put(ATTENDEE_DESIGNATION,
                            attendee_designation);
                }

                String attendee_type = attendeesList.get(i).getAttendeeType();
                if (attendee_type != null && attendee_type.length() > 0) {
                    contentValues.put(ATTENDEE_TYPE, attendee_type);
                }
                String buddy_status = attendeesList.get(i).getBuddy_status();
                if (buddy_status != null && buddy_status.length() > 0) {
                    contentValues.put(BUDDY_STATUS, buddy_status);
                }
                String chat_count = attendeesList.get(i).getChat_data_count();
                if (chat_count != null && chat_count.length() > 0) {
                    contentValues.put(CHAT_DATA_COUNT, chat_count);
                }


                String mention_name;
                if (attendeesList.get(i).getLastName() != null) {
                    mention_name = "<" + attendeesList.get(i).getAttendeeId() + "^" + attendeesList.get(i).getFirstName() + " " + attendeesList.get(i).getLastName() + ">";
                } else {
                    mention_name = "<" + attendeesList.get(i).getAttendeeId() + "^" + attendeesList.get(i).getFirstName() + ">";
                }
                if (mention_name != null && mention_name.length() > 0) {
                    contentValues.put(ATTENDEE_MENTION_NAME, mention_name);
                }

                db.insert(ATTENDEES_TABLE_NAME, null, contentValues);

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    //Insert value in Speaker Table
    public void insertSpeakersInfo(List<SpeakerList> speakersList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < speakersList.size(); i++) {
                contentValues = new ContentValues();

                if (speakersList.get(i).getAttendeeType()
                        .equalsIgnoreCase("S")) {

                    String attendee_id = speakersList.get(i).getAttendeeId();
                    if (attendee_id != null && attendee_id.length() > 0) {
                        contentValues.put(ATTENDEE_ID, attendee_id);
                    }

                    String api_access_token = speakersList.get(i).getApiAccessToken();
                    if (api_access_token != null && api_access_token.length() > 0) {
                        contentValues.put(ATTENDEE_API_ACCESS_TOKEN, api_access_token);
                    }

                    String first_name = speakersList.get(i).getFirstName();
                    if (first_name != null && first_name.length() > 0) {
                        contentValues.put(ATTENDEE_FIRST_NAME, first_name);
                    }

                    String last_name = speakersList.get(i).getLastName();
                    if (last_name != null && last_name.length() > 0) {
                        contentValues.put(ATTENDEE_LAST_NAME, last_name);
                    }

                    String attendee_description = speakersList.get(i).getDescription();
                    if (attendee_description != null
                            && attendee_description.length() > 0) {
                        contentValues.put(ATTENDEE_DESCRIPTION,
                                attendee_description);
                    }

                    String attendee_city = speakersList.get(i).getCity();
                    if (attendee_city != null && attendee_city.length() > 0) {
                        contentValues.put(ATTENDEE_CITY, attendee_city);
                    }

                    String attendee_country = speakersList.get(i).getCountry();
                    if (attendee_country != null
                            && attendee_country.length() > 0) {
                        contentValues.put(ATTENDEE_COUNTRY, attendee_country);
                    }

                    String profile_pic = speakersList.get(i).getProfilePic();
                    if (profile_pic != null && profile_pic.length() > 0) {
                        contentValues.put(ATTENDEE_PROFILE_PIC, profile_pic);
                    }

                    String mobile = speakersList.get(i).getMobileNumber();
                    if (mobile != null && mobile.length() > 0) {
                        contentValues.put(ATTENDEE_MOBILE, mobile);
                    }

                    String email = speakersList.get(i).getEmail();
                    if (email != null && email.length() > 0) {
                        contentValues.put(ATTENDEE_EMAIL, email);
                    }

                    String attendee_company = speakersList.get(i).getCompany();
                    if (attendee_company != null
                            && attendee_company.length() > 0) {
                        contentValues.put(ATTENDEE_COMPANY_NAME,
                                attendee_company);
                    }

                    String attendee_designation = speakersList.get(i)
                            .getDesignation();
                    if (attendee_designation != null
                            && attendee_designation.length() > 0) {
                        contentValues.put(ATTENDEE_DESIGNATION,
                                attendee_designation);
                    }

                    String attendee_type = speakersList.get(i).getAttendeeType();
                    if (attendee_type != null && attendee_type.length() > 0) {
                        contentValues.put(ATTENDEE_TYPE, attendee_type);

                    }
                    String attendee_total_rating = speakersList.get(i).getTotalRating();
                    if (attendee_total_rating != null && attendee_total_rating.length() > 0) {
                        contentValues.put(ATTENDEE_TOTAL_RATING, attendee_total_rating);
                    }

                    String attendee_avg_rating = speakersList.get(i).getAvgRating();
                    if (attendee_avg_rating != null && attendee_avg_rating.length() > 0) {
                        contentValues.put(ATTENDEE_AVG_RATING, attendee_avg_rating);
                    }

                    String attendee_star = speakersList.get(i).getStar();
                    if (attendee_star != null && attendee_star.length() > 0) {
                        contentValues.put(ATTENDEE_STAR, attendee_star);
                    }


                    db.insert(SPEAKER_TABLE_NAME, null, contentValues);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    //Insert Newsfeed data in table
    public void insertNEwsFeedInfo(List<NewsFeedList> newsfeedsList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < newsfeedsList.size(); i++) {
                contentValues = new ContentValues();

                String newsFeedId = newsfeedsList.get(i).getNewsFeedId();
                if (newsFeedId != null && newsFeedId.length() > 0) {
                    contentValues.put(NEWSFEED_ID, newsFeedId);
                }

                String type = newsfeedsList.get(i).getType();
                if (type != null && type.length() > 0) {
                    contentValues.put(NEWSFEED_TYPE, type);
                }

                String mediaFile = newsfeedsList.get(i).getMediaFile();
                if (mediaFile != null && mediaFile.length() > 0) {
                    contentValues.put(NEWSFEED_MEDIAFILE, mediaFile);
                }

                String postStatus = newsfeedsList.get(i).getPostStatus();
                if (postStatus != null && postStatus.length() > 0) {
                    contentValues.put(NEWSFEED_POST_STATUS, postStatus);
                }

                String thumbImage = newsfeedsList.get(i).getThumbImage();
                if (thumbImage != null
                        && thumbImage.length() > 0) {
                    contentValues.put(NEWSFEED_THUMB_IMAGE,
                            thumbImage);
                }

                String eventId = newsfeedsList.get(i).getEventId();
                if (eventId != null
                        && eventId.length() > 0) {
                    contentValues.put(NEWSFEED_EVENTID, eventId);
                }
                String postDate = newsfeedsList.get(i).getPostDate();
                if (postDate != null && postDate.length() > 0) {
                    contentValues.put(NEWSFEED_POST_DATE, postDate);
                }

                String firstName = newsfeedsList.get(i).getFirstName();
                if (firstName != null && firstName.length() > 0) {
                    contentValues.put(NEWSFEED_FIRST_NAME, firstName);
                }

                String lastName = newsfeedsList.get(i).getLastName();
                if (lastName != null && lastName.length() > 0) {
                    contentValues.put(NEWSFEED_LAST_NAME, lastName);
                }

                String companyName = newsfeedsList.get(i).getCompanyName();
                if (companyName != null && companyName.length() > 0) {
                    contentValues.put(NEWSFEED_COMP_NAME, companyName);
                }

                String designation = newsfeedsList.get(i).getDesignation();
                if (designation != null
                        && designation.length() > 0) {
                    contentValues.put(NEWSFEED_DESIGNATION,
                            designation);
                }
                String profilePic = newsfeedsList.get(i).getProfilePic();
                if (profilePic != null && profilePic.length() > 0) {
                    contentValues.put(NEWSFEED_PROFILE_PIC, profilePic);

                }
                String attendeeId = newsfeedsList.get(i)
                        .getAttendeeId();
                if (attendeeId != null
                        && attendeeId.length() > 0) {
                    contentValues.put(NEWSFEED_ATTENDEE_ID,
                            attendeeId);
                }

                String width = newsfeedsList.get(i).getWidth();
                if (width != null && width.length() > 0) {
                    contentValues.put(NEWSFEED_WIDTH, width);
                }

                String height = newsfeedsList.get(i).getHeight();
                if (height != null && height.length() > 0) {
                    contentValues.put(NEWSFEED_HEIGHT, height);
                }

                String likeFlag = newsfeedsList.get(i).getLikeFlag();
                if (likeFlag != null && likeFlag.length() > 0) {
                    contentValues.put(NEWSFEED_LIKE_FLAG, likeFlag);
                }
                String totalLikes = newsfeedsList.get(i).getTotalLikes();
                if (totalLikes != null && totalLikes.length() > 0) {
                    contentValues.put(NEWSFEED_TOTAL_LIKES, totalLikes);
                }
                String totalComments = newsfeedsList.get(i).getTotalComments();
                if (totalComments != null && totalComments.length() > 0) {
                    contentValues.put(NEWSFEED_TOTAL_COMMENTS, totalComments);
                }
                String attendee_type = newsfeedsList.get(i).getAttendee_type();
                if (attendee_type != null && attendee_type.length() > 0) {
                    contentValues.put(NEWSFEED_ATTENDEE_TYPE, attendee_type);
                }
                String city = newsfeedsList.get(i).getCity();
                if (city != null && city.length() > 0) {
                    contentValues.put(NEWSFEED_CITY, city);
                }

                db.insert(NEWSFEED_TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertQuizTable(List<Quiz> agendasList,
                                SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(QUIZ_ID, session_id);
                }

                String session_name = agendasList.get(i).getQuestion();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(QUESTION, session_name);
                }

                String session_description = agendasList.get(i).getCorrect_answer();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(CORRECTANSWERID, session_description);
                }

                String folder_name = agendasList.get(i).getFolder_name();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(FOLDERNAME, folder_name);
                }

                String replied = agendasList.get(i).getReplied();
                if (replied != null && replied.length() > 0) {
                    contentValues.put(REPLIED, replied);
                }

                String selected_option = agendasList.get(i).getSelected_option();
                if (selected_option != null && selected_option.length() > 0) {
                    contentValues.put(SELECTED_OPTION, selected_option);
                }


                db.insert(QUIZ_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertExebitorTable(List<ExhibitorCatList> agendasList,
                                    SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getExhibitor_category_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(EXHIBITOR_CATEGORY_ID, session_id);
                }

                String session_name = agendasList.get(i).getName();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(EXHIBITOR_NAME, session_name);
                }

                String session_description = agendasList.get(i).getTotal_exhibitor_count();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(TOTAL_EXHIBITOR_COUNT, session_description);
                }


                db.insert(EXHIBITOR_CATEGORY_MASTER_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertExebitorCatTable(List<ExhiCatDetailList> agendasList,
                                       SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getExhibitor_category_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(EXHIBITOR_CATEGORY_ID_EX, session_id);
                }

                String session_name = agendasList.get(i).getName();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(EXHIBITOR_NAME_EX, session_name);
                }

                String session_description = agendasList.get(i).getExhibitor_id();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(EXHIBITOR_ID, session_description);
                }


                db.insert(EXHIBITOR_CATEGORY_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    // Insert Values in Agenda Table
    public void insertAgendaInfo(List<AgendaList> agendasList,
                                 SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSessionId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_ID, session_id);
                }

                String session_name = agendasList.get(i).getSessionName();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(SESSION_NAME, session_name);
                }

                String session_description = agendasList.get(i).getSessionDescription();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(SESSION_DESCRIPTION, session_description);
                }

                String session_start_time = agendasList.get(i).getSessionStartTime();
                if (session_start_time != null && session_start_time.length() > 0) {
                    contentValues.put(SESSION_START_TIME, session_start_time);
                }

                String session_end_time = agendasList.get(i).getSessionEndTime();
                if (session_end_time != null
                        && session_end_time.length() > 0) {
                    contentValues.put(SESSION_END_TIME,
                            session_end_time);
                }

                String session_date = agendasList.get(i).getSessionDate();
                if (session_date != null && session_date.length() > 0) {
                    contentValues.put(SESSION_DATE, session_date);
                }

                String event_id = agendasList.get(i).getEventId();
                if (event_id != null
                        && event_id.length() > 0) {
                    contentValues.put(SESSION_EVENT_ID, event_id);
                }

                String star = agendasList.get(i).getStar();
                if (star != null) {
                    contentValues.put(SESSION_STAR, star);
                }

                String totalFeedback = agendasList.get(i).getTotalFeedback();
                if (totalFeedback != null && totalFeedback.length() > 0) {
                    contentValues.put(SESSION_TOTAL_FEEDBACK, totalFeedback);
                }

                String feedbackComment = agendasList.get(i).getFeedbackComment();
                if (feedbackComment != null && feedbackComment.length() > 0) {
                    contentValues.put(SESSION_FEEDBACK_COMMENT, feedbackComment);
                }

                db.insert(AGENDA_TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    public void insertAgendaVacationInfo(List<AgendaVacationList> agendasList,
                                         SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSessionId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_VACATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getSession_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(SESSION_VACATION_NAME, session_name);
                }

                String session_description = agendasList.get(i).getSession_description();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(SESSION_VACATION_DESCRIPTION, session_description);
                }

                String folder_name = agendasList.get(i).getFolder_name();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(FOLDER_NAME, folder_name);
                }


                String event_id = agendasList.get(i).getEvent_id();
                if (event_id != null
                        && event_id.length() > 0) {
                    contentValues.put(SESSION_EVENT_VACATION_ID, event_id);
                }


                db.insert(AGENDA_VACATION_TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertEventInfo(List<EventList> agendasList,
                                SQLiteDatabase db) {


        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getEventId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(EVENTINFO_ID, session_id);
                }

                String session_name = agendasList.get(i).getEventName();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(EVENTINFO_NAME, session_name);
                }

                String session_description = agendasList.get(i).getEventDescription();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(EVENT_DESCRIPTION, session_description);
                }

                String folder_name = agendasList.get(i).getEventStart();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(EVENT_START, folder_name);
                }


                String event_id = agendasList.get(i).getEventEnd();
                if (event_id != null
                        && event_id.length() > 0) {
                    contentValues.put(EVENT_END, event_id);
                }
                String event_location = agendasList.get(i).getEventLocation();
                if (event_location != null
                        && event_location.length() > 0) {
                    contentValues.put(EVENT_LOCATION, event_location);
                }
                String event_city = agendasList.get(i).getEventCity();
                if (event_city != null
                        && event_city.length() > 0) {
                    contentValues.put(EVENT_CITY, event_city);
                }
                String event_country = agendasList.get(i).getEventCountry();
                if (event_country != null
                        && event_country.length() > 0) {
                    contentValues.put(EVENT_COUNTRY, event_country);
                }
                String event_latitude = agendasList.get(i).getEventLatitude();
                if (event_latitude != null
                        && event_latitude.length() > 0) {
                    contentValues.put(EVENT_LATITUDE, event_latitude);
                }

                String event_longitude = agendasList.get(i).getEventLongitude();
                if (event_longitude != null
                        && event_longitude.length() > 0) {
                    contentValues.put(EVENT_LONGITUDE, event_longitude);
                }

                String event_logo = agendasList.get(i).getLogo();
                if (event_logo != null
                        && event_logo.length() > 0) {
                    contentValues.put(LOGO, event_logo);
                }


                db.insert(EVENTINFO_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertSponsorInfo(List<SponsorsList> sponsorList,
                                  SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < sponsorList.size(); i++) {
                contentValues = new ContentValues();

                String sponsor_id = sponsorList.get(i).getId();
                if (sponsor_id != null && sponsor_id.length() > 0) {
                    contentValues.put(SPONSOR_ID, sponsor_id);
                }

                String sponsor_name = sponsorList.get(i).getLogo();
                if (sponsor_name != null && sponsor_name.length() > 0) {
                    contentValues.put(SPONSOR_LOGO, sponsor_name);
                }

                String sponsor_description = sponsorList.get(i).getName();
                if (sponsor_description != null && sponsor_description.length() > 0) {
                    contentValues.put(SPONSOR_NAME, sponsor_description);
                }

                db.insert(SPONSOR_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    //get Agenda Media Info
    public void insertAgendaMediaInfo(List<AgendaMediaList> agendasList,
                                      SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSession_vacation_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_MEDIA_VACATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getMedia_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(MEDIA_NAME, session_name);
                }

                String session_description = agendasList.get(i).getMedia_type();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(MEDIA_TYPE, session_description);
                }

                String folder_name = agendasList.get(i).getMedia_thumbnail();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(MEDIA_THUMBNAIL, folder_name);
                }


                db.insert(AGENDA_VACATION_MEDIA_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertExAttendeeList(List<ExhibitorAttendeeList> agendasList,
                                     SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getExhibitor_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(ATTENDEE_EXHIBITOR_ID, session_id);
                }

                String session_name = agendasList.get(i).getAttendee_id();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(EX_ATTENDEE_ID, session_name);
                }

                String session_description = agendasList.get(i).getFirst_name();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(EX_FIRST_NAME, session_description);
                }

                String folder_name = agendasList.get(i).getLast_name();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(EX_LAST_NAME, folder_name);
                }
                String prof = agendasList.get(i).getProfile_pic();
                if (prof != null && prof.length() > 0) {
                    contentValues.put(EX_PROFILE_PIC, prof);
                }
                String city = agendasList.get(i).getCity();
                if (city != null && city.length() > 0) {
                    contentValues.put(EX_CITY, city);
                }


                db.insert(EXHIBITOR_ATTENDEE_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertNotificationList(List<NotificationList> agendasList,
                                       SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getNotificationId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(NOTIFICATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getNotificationType();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(NOTIFICATION_TYPE, session_name);
                }

                String session_description = agendasList.get(i).getSubjectId();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(SUBJECT_ID, session_description);
                }

                String folder_name = agendasList.get(i).getSubjectType();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(SUBJECT_TYPE, folder_name);
                }
                String prof = agendasList.get(i).getObjectId();
                if (prof != null && prof.length() > 0) {
                    contentValues.put(OBJECT_ID, prof);
                }
                String city = agendasList.get(i).getObjectType();
                if (city != null && city.length() > 0) {
                    contentValues.put(OBJECT_TYPE, city);
                }

                String read = agendasList.get(i).getRead();
                if (read != null && read.length() > 0) {
                    contentValues.put(READ, read);
                }

                String noticontent = agendasList.get(i).getNotificationContent();
                if (noticontent != null && noticontent.length() > 0) {
                    contentValues.put(NOTIFICATION_CONTENT, noticontent);
                }

                String msgid = agendasList.get(i).getMessageId();
                if (msgid != null) {
                    contentValues.put(MESSAGE_ID, msgid);
                }

                String eventid = agendasList.get(i).getEventId();
                if (eventid != null && eventid.length() > 0) {
                    contentValues.put(EVENT_ID, eventid);
                }
                String notidate = agendasList.get(i).getNotificationDate();
                if (notidate != null && notidate.length() > 0) {
                    contentValues.put(NOTIFICATION_DATE, notidate);
                }
                String notiid = agendasList.get(i).getAttendeeId();
                if (notiid != null && notiid.length() > 0) {
                    contentValues.put(NOTIATTENDEE_ID, notiid);
                }
                String fname = agendasList.get(i).getAttendeeFirstName();
                if (fname != null && fname.length() > 0) {
                    contentValues.put(NOTIATTENDEE_FIRST_NAME, fname);
                }
                String lname = agendasList.get(i).getAttendeeLastName();
                if (lname != null && city.length() > 0) {
                    contentValues.put(NOTIATTENDEE_LAST_NAME, lname);
                }
                String cname = agendasList.get(i).getCompanyName();
                if (cname != null && cname.length() > 0) {
                    contentValues.put(COMPANY_NAME, cname);
                }
                String des = agendasList.get(i).getDesignation();
                if (des != null && des.length() > 0) {
                    contentValues.put(DESIGNATION, des);
                }
                String prpic = agendasList.get(i).getProfilePic();
                if (prpic != null && prpic.length() > 0) {
                    contentValues.put(PROFILE_PIC, prpic);
                }
                String evname = agendasList.get(i).getEventName();
                if (evname != null && evname.length() > 0) {
                    contentValues.put(EVENT_NAME, evname);
                }
                String postid = agendasList.get(i).getNotificationPostId();
                if (postid != null && postid.length() > 0) {
                    contentValues.put(NOTIFICATION_POST_ID, postid);
                }


                db.insert(NOTIFICATION_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertExNotificationList(List<Exhibitor_Notification_List> agendasList,
                                         SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getNotificationId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(EX_NOTIFICATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getNotificationType();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(EX_NOTIFICATION_TYPE, session_name);
                }

                String session_description = agendasList.get(i).getSubjectId();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(EX_SUBJECT_ID, session_description);
                }

                String folder_name = agendasList.get(i).getSubjectType();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(EX_SUBJECT_TYPE, folder_name);
                }
                String prof = agendasList.get(i).getObjectId();
                if (prof != null && prof.length() > 0) {
                    contentValues.put(EX_OBJECT_ID, prof);
                }
                String city = agendasList.get(i).getObjectType();
                if (city != null && city.length() > 0) {
                    contentValues.put(EX_OBJECT_TYPE, city);
                }

                String read = agendasList.get(i).getRead();
                if (read != null && read.length() > 0) {
                    contentValues.put(EX_READ, read);
                }

                String noticontent = agendasList.get(i).getNotificationContent();
                if (noticontent != null && noticontent.length() > 0) {
                    contentValues.put(EX_NOTIFICATION_CONTENT, noticontent);
                }

                String msgid = agendasList.get(i).getMessageId();
                if (msgid != null) {
                    contentValues.put(EX_MESSAGE_ID, msgid);
                }

                String eventid = agendasList.get(i).getEventId();
                if (eventid != null && eventid.length() > 0) {
                    contentValues.put(EX_EVENT_ID, eventid);
                }
                String notidate = agendasList.get(i).getNotificationDate();
                if (notidate != null && notidate.length() > 0) {
                    contentValues.put(EX_NOTIFICATION_DATE, notidate);
                }
                String notiid = agendasList.get(i).getNotificationId();
                if (notiid != null && notiid.length() > 0) {
                    contentValues.put(EX_NOTIATTENDEE_ID, notiid);
                }
                String fname = agendasList.get(i).getAttendeeFirstName();
                if (fname != null && fname.length() > 0) {
                    contentValues.put(EX_NOTIATTENDEE_FIRST_NAME, fname);
                }
                String lname = agendasList.get(i).getAttendeeLastName();
                if (lname != null && city.length() > 0) {
                    contentValues.put(EX_NOTIATTENDEE_LAST_NAME, lname);
                }
                String cname = agendasList.get(i).getCompanyName();
                if (cname != null && cname.length() > 0) {
                    contentValues.put(EX_COMPANY_NAME, cname);
                }
                String des = agendasList.get(i).getDesignation();
                if (des != null && des.length() > 0) {
                    contentValues.put(EX_DESIGNATION, des);
                }
                String prpic = agendasList.get(i).getProfilePic();
                if (prpic != null && prpic.length() > 0) {
                    contentValues.put(EX_PROFILE_PIC, prpic);
                }
                String evname = agendasList.get(i).getEventName();
                if (evname != null && evname.length() > 0) {
                    contentValues.put(EX_EVENT_NAME, evname);
                }
                String postid = agendasList.get(i).getNotificationPostId();
                if (postid != null && postid.length() > 0) {
                    contentValues.put(EX_NOTIFICATION_POST_ID, postid);
                }


                db.insert(EX_NOTIFICATION_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    public void insertBuzzMediaInfo(List<news_feed_media> agendasList,
                                    SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getNews_feed_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(BUZZ_FEED_ID, session_id);
                }

                String session_name = agendasList.get(i).getMedia_type();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(BUZZ_MEDIA_TYPE, session_name);
                }

                String session_description = agendasList.get(i).getMediaFile();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(BUZZ_MEDIA_FILE, session_description);
                }

                String session_start_time = agendasList.get(i).getThumb_image();
                if (session_start_time != null && session_start_time.length() > 0) {
                    contentValues.put(BUZZ_THUMB_IMAGE, session_start_time);
                }

                String session_end_time = agendasList.get(i).getWidth();
                if (session_end_time != null
                        && session_end_time.length() > 0) {
                    contentValues.put(BUZZ_WIDTH,
                            session_end_time);
                }

                String session_date = agendasList.get(i).getHeight();
                if (session_date != null && session_date.length() > 0) {
                    contentValues.put(BUZZ_HEIGHT, session_date);
                }

                String media_id = agendasList.get(i).getMedia_id();
                if (media_id != null && media_id.length() > 0) {
                    contentValues.put(BUZZ_MEDIA_ID, media_id);
                }

                //if (agendasList.get(i).getMedia_type().equalsIgnoreCase("image")) {
                if (agendasList.get(i).getMedia_image() != null) {
                    contentValues.put(BUZZ_MEDIA_IMAGE, agendasList.get(i).getMedia_image());
                }
                //}

                db.insert(BUZZ_MEDIA_TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertMeetingList(List<Exhibitor_Meeting_Request_List> agendasList,
                                  SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();

        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(EX_MEETING_ID, session_id);
                }

                String session_name = agendasList.get(i).getExhibitor_id();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(MEETING_EXHIBITOR_ID, session_name);
                }

                String session_description = agendasList.get(i).getAttendee_id();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(MEETING_ATTENDEE_ID, session_description);
                }

                String folder_name = agendasList.get(i).getEvent_id();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(MEETING_EVENT_ID, folder_name);
                }
                String prof = agendasList.get(i).getMeeting_date_time();
                if (prof != null && prof.length() > 0) {
                    contentValues.put(MEETING_DATE_TIME, prof);
                }
                String city = agendasList.get(i).getStatus();
                if (city != null && city.length() > 0) {
                    contentValues.put(MEETING_STATUS, city);
                }

                String read = agendasList.get(i).getDescription();
                if (read != null && read.length() > 0) {
                    contentValues.put(MEETING_DESCRIPTION, read);
                }

                String noticontent = agendasList.get(i).getCreated();
                if (noticontent != null && noticontent.length() > 0) {
                    contentValues.put(MEETING_CREATED, noticontent);
                }

                String msgid = agendasList.get(i).getModified();
                if (msgid != null) {
                    contentValues.put(MEETING_MODIFIED, msgid);
                }

                String eventid = agendasList.get(i).getFirst_name();
                if (eventid != null && eventid.length() > 0) {
                    contentValues.put(MEETING_FIRST_NAME, eventid);
                }
                String lname = agendasList.get(i).getLast_name();
                if (lname != null && lname.length() > 0) {
                    contentValues.put(MEETING_LAST_NAME, lname);
                }

                String notiid = agendasList.get(i).getProfile_pic();
                if (notiid != null && notiid.length() > 0) {
                    contentValues.put(MEETING_PROFILE_PIC, notiid);
                }

                db.insert(EX_MEETING_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }
//    public void insertMtingList(List<String> agendasList,
//                                  SQLiteDatabase db) {
//
//        db = this.getWritableDatabase();
//        ContentValues contentValues;
//        db.beginTransaction();
//
//        try {
//            for (int i = 0; i < agendasList.size(); i++) {
//                contentValues = new ContentValues();
//
//                String session_id = agendasList.get(i);
//                if (session_id != null && session_id.length() > 0) {
//                    contentValues.put(E_MEETING_ID, session_id);
//                }
//
//
//
//                db.insert(E_MEETING_TABLE, null, contentValues);
//            }
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//        return;
//    }

    public void insertUploadMultimediaInfo(List<NewsFeedPostMultimedia> newsFeedPostMultimedia, String news_feed_id,
                                           SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < newsFeedPostMultimedia.size(); i++) {
                contentValues = new ContentValues();
                contentValues.put(MULTIMEDIA_FILE, newsFeedPostMultimedia.get(i).getMedia_file());
                contentValues.put(MULTIMEDIA_THUMB, newsFeedPostMultimedia.get(i).getMedia_file_thumb());
                contentValues.put(NEWS_FEED_ID, news_feed_id);
                contentValues.put(IS_UPLOADED, "false");
                contentValues.put(MULTIMEDIA_COMPRESSED_FILE, newsFeedPostMultimedia.get(i).getCompressedPath());
                contentValues.put(MULTIMEDIA_TYPE, newsFeedPostMultimedia.get(i).getMedia_type());
                contentValues.put(FOLDER_UNIQUE_ID, newsFeedPostMultimedia.get(i).getFolderUniqueId());
                db.insert(UPLOAD_MULTIMEDIA_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertExBrocherList(List<ExhibitorBrochureList> agendasList,
                                    SQLiteDatabase db) {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();

        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getBrochure_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(BROCHURE_ID, session_id);
                }

                String session_name = agendasList.get(i).getId();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(B_ID, session_name);
                }

                String session_description = agendasList.get(i).getExhibitor_id();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(BROCHER_EXHIBITOR_ID, session_description);
                }

                String folder_name = agendasList.get(i).getEvent_id();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(B_EVENT_ID, folder_name);
                }
                String prof = agendasList.get(i).getBrochure_title();
                if (prof != null && prof.length() > 0) {
                    contentValues.put(BROCHURE_TITLE, prof);
                }
                String city = agendasList.get(i).getFile_name();
                if (city != null && city.length() > 0) {
                    contentValues.put(FILE_NAME, city);
                }
                String ftype = agendasList.get(i).getFile_type();
                if (ftype != null && ftype.length() > 0) {
                    contentValues.put(FILE_TYPE, ftype);
                }
                String created = agendasList.get(i).getCreated();
                if (created != null && created.length() > 0) {
                    contentValues.put(B_CREATED, created);
                }
                String modified = agendasList.get(i).getModified();
                if (modified != null && modified.length() > 0) {
                    contentValues.put(B_MODIFIED, modified);
                }


                db.insert(EXHIBITOR_BROCHURE_LIST, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }
    //get Agenda vacation detail

    public List<AgendaVacationList> getAgendaFolderList() {
        String selectQuery = "select * from " + AGENDA_VACATION_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaVacationList> questionList = new ArrayList<AgendaVacationList>();

        if (cursor.moveToFirst()) {
            do {

                AgendaVacationList agendaQuestions = new AgendaVacationList();
                agendaQuestions.setSessionId(cursor.getString(0));
                agendaQuestions.setSession_name(cursor.getString(1));
                agendaQuestions.setSession_description(cursor.getString(2));
                agendaQuestions.setFolder_name(cursor.getString(3));
                agendaQuestions.setEvent_id(cursor.getString(4));


                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public String getBuddyChatForAttendeeId(String id) {
        String count = "0";
        String selectQuery = "select count(*) as count from " + BUDDY_CHAT_COUNT_TABLE_NAME + " where " + BUDDY_CHAT_COUNT_IS_READ + " = '0' " +
                "and " + BUDDY_CHAT_COUNT_SENDER_ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getString(0);
            }
            while (cursor.moveToNext());
            db.close();
        }
        return count;
    }



    public boolean setBuddyChatUnreadMessageCountToZero(String id) {
        String updateTable = "UPDATE " + BUDDY_CHAT_COUNT_TABLE_NAME +
                " SET " + BUDDY_CHAT_COUNT_IS_READ + " = '1' WHERE " + BUDDY_CHAT_COUNT_SENDER_ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateTable);
        return true;
    }

    public String getAttendeeChatForAttendeeId(String id) {
        String count = "0";
        String selectQuery = "select count(*) as count from " + ATTENDEE_CHAT_COUNT_TABLE_NAME + " where " + ATTENDEE_CHAT_COUNT_IS_READ + " = '0' " +
                "and " + ATTENDEE_CHAT_COUNT_SENDER_ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getString(0);
            }
            while (cursor.moveToNext());
            db.close();
        }
        return count;
    }
    public boolean setAttendeeChatUnreadMessageCountToZero(String id) {
        String updateTable = "UPDATE " + ATTENDEE_CHAT_COUNT_TABLE_NAME +
                " SET " + ATTENDEE_CHAT_COUNT_IS_READ + " = '1' WHERE " + ATTENDEE_CHAT_COUNT_SENDER_ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL(updateTable);
        return true;
    }

    public List<ExhibitorBrochureList> getBrochureList(String id) {
        String selectQuery = "select * from " + EXHIBITOR_BROCHURE_LIST + " where " + BROCHER_EXHIBITOR_ID + " LIKE \'%" + id + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<ExhibitorBrochureList> questionList = new ArrayList<ExhibitorBrochureList>();

        if (cursor.moveToFirst()) {
            do {
                ExhibitorBrochureList agendaQuestions = new ExhibitorBrochureList();
                agendaQuestions.setBrochure_id(cursor.getString(0));
                agendaQuestions.setId(cursor.getString(1));
                agendaQuestions.setExhibitor_id(cursor.getString(2));
                agendaQuestions.setEvent_id(cursor.getString(3));
                agendaQuestions.setBrochure_title(cursor.getString(4));
                agendaQuestions.setFile_name(cursor.getString(5));
                agendaQuestions.setFile_type(cursor.getString(6));
                agendaQuestions.setCreated(cursor.getString(7));
                agendaQuestions.setModified(cursor.getString(8));


                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public List<ExhibitorAttendeeList> getExAttendeeList() {
        String selectQuery = "select * from " + EXHIBITOR_ATTENDEE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<ExhibitorAttendeeList> questionList = new ArrayList<ExhibitorAttendeeList>();

        if (cursor.moveToFirst()) {
            do {

                ExhibitorAttendeeList agendaQuestions = new ExhibitorAttendeeList();
                agendaQuestions.setExhibitor_id(cursor.getString(0));
                agendaQuestions.setAttendee_id(cursor.getString(1));
                agendaQuestions.setFirst_name(cursor.getString(2));
                agendaQuestions.setLast_name(cursor.getString(3));
                agendaQuestions.setProfile_pic(cursor.getString(4));
                agendaQuestions.setCity(cursor.getString(5));


                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public List<ExhibitorAttendeeList> getExAttendeeList(String id) {
        String selectQuery = "select * from " + EXHIBITOR_ATTENDEE_LIST + " where " + EX_ATTENDEE_ID + " LIKE \'%" + id + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<ExhibitorAttendeeList> questionList = new ArrayList<ExhibitorAttendeeList>();

        if (cursor.moveToFirst()) {
            do {

                ExhibitorAttendeeList agendaQuestions = new ExhibitorAttendeeList();
                agendaQuestions.setExhibitor_id(cursor.getString(0));
                agendaQuestions.setAttendee_id(cursor.getString(1));
                agendaQuestions.setFirst_name(cursor.getString(2));
                agendaQuestions.setLast_name(cursor.getString(3));
                agendaQuestions.setProfile_pic(cursor.getString(4));
                agendaQuestions.setCity(cursor.getString(5));


                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public String getMeentionNameFromAttendeeId(String attendeeId, SQLiteDatabase db) {
        String mentionName = "";

        db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "select " + ATTENDEE_MENTION_NAME + " from " + ATTENDEES_TABLE_NAME + " where " + ATTENDEE_ID + "='" + attendeeId + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    mentionName = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return mentionName;
    }


    //get Media detail

    public List<AgendaMediaList> getAgendaMediaList() {
        String selectQuery = "select * from " + AGENDA_VACATION_MEDIA_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaMediaList> questionList = new ArrayList<AgendaMediaList>();

        if (cursor.moveToFirst()) {
            do {

                AgendaMediaList agendaQuestions = new AgendaMediaList();
                agendaQuestions.setSession_vacation_id(cursor.getString(0));
                agendaQuestions.setMedia_name(cursor.getString(1));
                agendaQuestions.setMedia_type(cursor.getString(2));
                agendaQuestions.setMedia_thumbnail(cursor.getString(3));

                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    // Get Attendee List/ Details
    public List<AttendeeList> getAttendeeDetails() {
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));
                attendeesList.setBuddy_status(cursor.getString(13));
                attendeesList.setChat_data_count(cursor.getString(14));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    public List<AttendeeList> getAttendeeDetailsFromAttendeeId(String attendeeId) {
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME+" where "+ATTENDEE_ID+" = '"+attendeeId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));
                attendeesList.setBuddy_status(cursor.getString(13));
                attendeesList.setChat_data_count(cursor.getString(14));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    // Get Buddy List/ Details
    public List<Buddy> getBuddyDetail() {
        String selectQuery = "select * from " + BUDDY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Buddy> attendeeList = new ArrayList<Buddy>();
        if (cursor.moveToFirst()) {

            do {
                Buddy attendeesList = new Buddy();
                attendeesList.setFriend_id(cursor.getString(0));
                attendeesList.setRequest_type(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDesignation(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setProfilePic(cursor.getString(6));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    public List<EventList> getEventListDetail() {
        String selectQuery = "select * from " + EVENTINFO_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<EventList> eventList = new ArrayList<EventList>();
        if (cursor.moveToFirst()) {

            do {
                EventList attendeesList = new EventList();
                attendeesList.setEventId(cursor.getString(0));
                attendeesList.setEventName(cursor.getString(1));
                attendeesList.setEventDescription(cursor.getString(2));
                attendeesList.setEventStart(cursor.getString(3));
                attendeesList.setEventEnd(cursor.getString(4));
                attendeesList.setEventLocation(cursor.getString(5));
                attendeesList.setEventCity(cursor.getString(6));
                attendeesList.setEventCountry(cursor.getString(7));
                attendeesList.setEventLatitude(cursor.getString(8));
                attendeesList.setEventLongitude(cursor.getString(9));
                attendeesList.setLogo(cursor.getString(10));


                eventList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return eventList;
    }

    public List<SponsorsList> getSponsorList() {
        String selectQuery = "select * from " + SPONSOR_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<SponsorsList> sponsorsLists = new ArrayList<SponsorsList>();
        if (cursor.moveToFirst()) {

            do {
                SponsorsList sponsorList = new SponsorsList();
                sponsorList.setId(cursor.getString(0));
                sponsorList.setName(cursor.getString(1));
                sponsorList.setLogo(cursor.getString(2));

                sponsorsLists.add(sponsorList);
            } while (cursor.moveToNext());
        }
        db.close();
        return sponsorsLists;
    }

    public List<AttendeeList> getAttendeeList() {
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));
                attendeesList.setBuddy_status(cursor.getString(13));
                attendeesList.setChat_data_count(cursor.getString(14));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }

    public List<Buddy> getBuddyDetailbyId(String buddyId) {
        //String selectQuery = "select * from " + BUDDY_TABLE_NAME;
        String selectQuery = "select * from " + BUDDY_TABLE_NAME + " where " + BUDDY_FRIEND_ID + " = '" + buddyId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Buddy> attendeeList = new ArrayList<Buddy>();
        if (cursor.moveToFirst()) {

            do {
                Buddy attendeesList = new Buddy();
                attendeesList.setFriend_id(cursor.getString(0));
                attendeesList.setRequest_type(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDesignation(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setProfilePic(cursor.getString(6));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    public List<AttendeeList> getAttendeeDetailsId(String att_id) {
/*
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'A\'";
*/

        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME + " where " + ATTENDEE_ID + " = '" + att_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));
                attendeesList.setBuddy_status(cursor.getString(13));
                attendeesList.setChat_data_count(cursor.getString(14));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    //Get User Data
    public UserData getUserDetails() {
        String selectQuery = "select * from " + USER_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserData userdataList = new UserData();
        if (cursor.moveToFirst()) {

            UserData userdatasList = new UserData();
            userdatasList.setAttendeeId(cursor.getString(0));
            userdatasList.setFirstName(cursor.getString(1));
            userdatasList.setLastName(cursor.getString(2));
            userdatasList.setApiAccessToken(cursor.getString(3));
            userdatasList.setEmail(cursor.getString(4));
            userdatasList.setCompanyName(cursor.getString(5));
            userdatasList.setDesignation(cursor.getString(6));
            userdatasList.setDescription(cursor.getString(7));
            userdatasList.setCity(cursor.getString(8));
            userdatasList.setCountry(cursor.getString(9));
            userdatasList.setProfilePic(cursor.getString(10));
            userdatasList.setMobile(cursor.getString(11));
            userdatasList.setAttendee_status(cursor.getString(12));
        }

        db.close();
        return userdataList;
    }


    // Get Speaker List/ Details
    public List<SpeakerList> getSpeakerDetails() {
        String selectQuery = "select * from " + SPEAKER_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'S\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<SpeakerList> speakerList = new ArrayList<SpeakerList>();
        if (cursor.moveToFirst()) {

            do {
                SpeakerList speakersList = new SpeakerList();
                speakersList.setAttendeeId(cursor.getString(0));
                speakersList.setApiAccessToken(cursor.getString(1));
                speakersList.setFirstName(cursor.getString(2));
                speakersList.setLastName(cursor.getString(3));
                speakersList.setDescription(cursor.getString(4));
                speakersList.setCity(cursor.getString(5));
                speakersList.setCountry(cursor.getString(6));
                speakersList.setProfilePic(cursor.getString(7));
                speakersList.setMobileNumber(cursor.getString(8));
                speakersList.setEmail(cursor.getString(9));
                speakersList.setCompany(cursor.getString(10));
                speakersList.setDesignation(cursor.getString(11));
                speakersList.setAttendeeType(cursor.getString(12));
                speakersList.setTotalRating(cursor.getString(13));
                speakersList.setAvgRating(cursor.getString(14));
                speakersList.setStar(cursor.getString(15));


                speakerList.add(speakersList);
            } while (cursor.moveToNext());
        }
        db.close();
        return speakerList;
    }


    // Get Agenda List/ Details
    public List<AgendaList> getAgendaDetails() {
        String selectQuery = "select * from " + AGENDA_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaList> agendaList = new ArrayList<AgendaList>();
        if (cursor.moveToFirst()) {

            do {
                AgendaList agendasList = new AgendaList();
                agendasList.setSessionId(cursor.getString(0));
                agendasList.setSessionName(cursor.getString(1));
                agendasList.setSessionDescription(cursor.getString(2));
                agendasList.setSessionStartTime(cursor.getString(3));
                agendasList.setSessionEndTime(cursor.getString(4));
                agendasList.setSessionDate(cursor.getString(5));
                agendasList.setEventId(cursor.getString(6));
                agendasList.setStar(cursor.getString(7));
                agendasList.setTotalFeedback(cursor.getString(8));
                agendasList.setFeedbackComment(cursor.getString(9));
                agendasList.setRated(cursor.getString(10));

                agendaList.add(agendasList);
            } while (cursor.moveToNext());
        }
        db.close();
        return agendaList;
    }

    //Get NewsFeed List
    public List<NewsFeedList> getNewsFeedDetails() {
        String selectQuery = "select * from " + NEWSFEED_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<NewsFeedList> newsFeedList = new ArrayList<NewsFeedList>();
        if (cursor.moveToFirst()) {

            do {
                NewsFeedList newsfeedsList = new NewsFeedList();
                newsfeedsList.setNewsFeedId(cursor.getString(0));
                newsfeedsList.setType(cursor.getString(1));
                newsfeedsList.setMediaFile(cursor.getString(2));
                newsfeedsList.setPostStatus(cursor.getString(3));
                newsfeedsList.setThumbImage(cursor.getString(4));
                newsfeedsList.setEventId(cursor.getString(5));
                newsfeedsList.setPostDate(cursor.getString(6));
                newsfeedsList.setFirstName(cursor.getString(7));
                newsfeedsList.setLastName(cursor.getString(8));
                newsfeedsList.setCompanyName(cursor.getString(9));
                newsfeedsList.setDesignation(cursor.getString(10));
                newsfeedsList.setProfilePic(cursor.getString(11));
                newsfeedsList.setAttendeeId(cursor.getString(12));
                newsfeedsList.setWidth(cursor.getString(13));
                newsfeedsList.setHeight(cursor.getString(14));
                newsfeedsList.setLikeFlag(cursor.getString(15));
                newsfeedsList.setTotalLikes(cursor.getString(16));
                newsfeedsList.setTotalComments(cursor.getString(17));
                newsfeedsList.setAttendee_type(cursor.getString(18));
                newsfeedsList.setCity(cursor.getString(19));

                String selectQueryMedia = "select BUZZ_FEED_ID,BUZZ_MEDIA_TYPE,BUZZ_MEDIA_FILE,BUZZ_THUMB_IMAGE,BUZZ_MEDIA_ID FROM " +
                        BUZZ_MEDIA_TABLE_NAME + " where " + BUZZ_FEED_ID + " = '" + cursor.getString(0) + "'";
                Cursor cursorMedia = db.rawQuery(selectQueryMedia, null);
                int count = cursorMedia.getCount();
                List<news_feed_media> newsFeedMedia = new ArrayList<news_feed_media>();
                if (cursorMedia.moveToFirst()) {
                    do {
                        news_feed_media newsFeedMediaList = new news_feed_media();
                        newsFeedMediaList.setNews_feed_id(cursorMedia.getString(0));
                        newsFeedMediaList.setMedia_type(cursorMedia.getString(1));
                        newsFeedMediaList.setMediaFile(cursorMedia.getString(2));
                        newsFeedMediaList.setThumb_image(cursorMedia.getString(3));
                        newsFeedMediaList.setMedia_id(cursorMedia.getString(4));
                        // newsFeedMediaList.setWidth(cursorMedia.getString(4));
                       /* if(cursorMedia.getBlob(4)!=null)
                        newsFeedMediaList.setMedia_image(cursorMedia.getBlob(4));
*/
                        //  newsFeedMediaList.setHeight(cursorMedia.getString(6));
                        newsFeedMedia.add(newsFeedMediaList);
                    } while (cursorMedia.moveToNext());
                }
               /* String selectQueryMedia = "select btn.BUZZ_ID,btn.BUZZ_POST_STATUS,btn.BUZZ_EVENTID,btn.BUZZ_POST_DATE," +
                        "btn.BUZZ_FIRST_NAME,btn.BUZZ_LAST_NAME,btn.BUZZ_PROFILE_PIC,btn.BUZZ_ATTENDEE_ID,btn.BUZZ_CITY," +
                        "btn.BUZZ_RELATION,btn.BUZZ_RELATION_WITH,btn.BUZZ_LIKE_FLAG,btn.BUZZ_TOTAL_LIKE,btn.BUZZ_TOTAL_COMMENT," +

                        "btnm.BUZZ_FEED_ID,btnm.BUZZ_MEDIA_TYPE,btnm.BUZZ_MEDIA_FILE,btnm.BUZZ_THUMB_IMAGE,btnm.BUZZ_WIDTH,btnm.BUZZ_HEIGHT,btnm.BUZZ_MEDIA_IMAGE FROM"+
                        " BUZZ_TABLE_NAME as btn LEFT JOIN  BUZZ_MEDIA_TABLE_NAME as btnm on btn.BUZZ_ID = btnm.BUZZ_FEED_ID";*/
                newsfeedsList.setNews_feed_media(newsFeedMedia);
                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public List<NotificationList> getNotificationDetails() {
        String selectQuery = "select * from " + NOTIFICATION_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<NotificationList> newsFeedList = new ArrayList<NotificationList>();
        if (cursor.moveToFirst()) {

            do {

                NotificationList newsfeedsList = new NotificationList();
                newsfeedsList.setNotificationId(cursor.getString(0));
                newsfeedsList.setNotificationType(cursor.getString(1));
                newsfeedsList.setSubjectId(cursor.getString(2));
                newsfeedsList.setSubjectType(cursor.getString(3));
                newsfeedsList.setObjectId(cursor.getString(4));
                newsfeedsList.setObjectType(cursor.getString(5));
                newsfeedsList.setRead(cursor.getString(6));
                newsfeedsList.setNotificationContent(cursor.getString(7));
                newsfeedsList.setMessageId(cursor.getString(8));
                newsfeedsList.setEventId(cursor.getString(9));
                newsfeedsList.setNotificationDate(cursor.getString(10));
                newsfeedsList.setAttendeeId(cursor.getString(11));
                newsfeedsList.setAttendeeFirstName(cursor.getString(12));
                newsfeedsList.setAttendeeLastName(cursor.getString(13));
                newsfeedsList.setCompanyName(cursor.getString(14));
                newsfeedsList.setDesignation(cursor.getString(15));
                newsfeedsList.setProfilePic(cursor.getString(16));
                newsfeedsList.setEventName(cursor.getString(17));
                newsfeedsList.setNotificationPostId(cursor.getString(18));

                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public List<Exhibitor_Notification_List> getExNotificationDetails() {
        String selectQuery = "select * from " + EX_NOTIFICATION_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Exhibitor_Notification_List> newsFeedList = new ArrayList<Exhibitor_Notification_List>();
        if (cursor.moveToFirst()) {

            do {

                Exhibitor_Notification_List newsfeedsList = new Exhibitor_Notification_List();
                newsfeedsList.setNotificationId(cursor.getString(0));
                newsfeedsList.setNotificationType(cursor.getString(1));
                newsfeedsList.setSubjectId(cursor.getString(2));
                newsfeedsList.setSubjectType(cursor.getString(3));
                newsfeedsList.setObjectId(cursor.getString(4));
                newsfeedsList.setObjectType(cursor.getString(5));
                newsfeedsList.setRead(cursor.getString(6));
                newsfeedsList.setNotificationContent(cursor.getString(7));
                newsfeedsList.setMessageId(cursor.getString(8));
                newsfeedsList.setEventId(cursor.getString(9));
                newsfeedsList.setNotificationDate(cursor.getString(10));
                newsfeedsList.setAttendeeId(cursor.getString(11));
                newsfeedsList.setAttendeeFirstName(cursor.getString(12));
                newsfeedsList.setAttendeeLastName(cursor.getString(13));
                newsfeedsList.setCompanyName(cursor.getString(14));
                newsfeedsList.setDesignation(cursor.getString(15));
                newsfeedsList.setProfilePic(cursor.getString(16));
                newsfeedsList.setEventName(cursor.getString(17));
                newsfeedsList.setNotificationPostId(cursor.getString(18));

                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public List<Exhibitor_Meeting_Request_List> getMeetingDetails() {
        String selectQuery = "select * from " + EX_MEETING_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Exhibitor_Meeting_Request_List> newsFeedList = new ArrayList<Exhibitor_Meeting_Request_List>();
        if (cursor.moveToFirst()) {

            do {

                Exhibitor_Meeting_Request_List newsfeedsList = new Exhibitor_Meeting_Request_List();
                newsfeedsList.setId(cursor.getString(0));
                newsfeedsList.setExhibitor_id(cursor.getString(1));
                newsfeedsList.setAttendee_id(cursor.getString(2));
                newsfeedsList.setEvent_id(cursor.getString(3));
                newsfeedsList.setMeeting_date_time(cursor.getString(4));
                newsfeedsList.setStatus(cursor.getString(5));
                newsfeedsList.setDescription(cursor.getString(6));
                newsfeedsList.setCreated(cursor.getString(7));
                newsfeedsList.setModified(cursor.getString(8));
                newsfeedsList.setFirst_name(cursor.getString(9));
                newsfeedsList.setLast_name(cursor.getString(10));
                newsfeedsList.setProfile_pic(cursor.getString(11));


                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public List<NewsFeedList> getNewsFeedLikeandComment(String feedid) {
        String selectQuery = "select * from " + NEWSFEED_TABLE_NAME + " where " + NEWSFEED_ID + " LIKE \'%" + feedid + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<NewsFeedList> newsFeedList = new ArrayList<NewsFeedList>();
        if (cursor.moveToFirst()) {

            do {
                NewsFeedList newsfeedsList = new NewsFeedList();
                newsfeedsList.setNewsFeedId(cursor.getString(0));
                newsfeedsList.setType(cursor.getString(1));
                newsfeedsList.setMediaFile(cursor.getString(2));
                newsfeedsList.setPostStatus(cursor.getString(3));
                newsfeedsList.setThumbImage(cursor.getString(4));
                newsfeedsList.setEventId(cursor.getString(5));
                newsfeedsList.setPostDate(cursor.getString(6));
                newsfeedsList.setFirstName(cursor.getString(7));
                newsfeedsList.setLastName(cursor.getString(8));
                newsfeedsList.setCompanyName(cursor.getString(9));
                newsfeedsList.setDesignation(cursor.getString(10));
                newsfeedsList.setProfilePic(cursor.getString(11));
                newsfeedsList.setAttendeeId(cursor.getString(12));
                newsfeedsList.setWidth(cursor.getString(13));
                newsfeedsList.setHeight(cursor.getString(14));
                newsfeedsList.setLikeFlag(cursor.getString(15));
                newsfeedsList.setTotalLikes(cursor.getString(16));
                newsfeedsList.setTotalComments(cursor.getString(17));
                newsfeedsList.setAttendee_type(cursor.getString(18));
                newsfeedsList.setCity(cursor.getString(19));
                String selectQueryMedia = "select BUZZ_FEED_ID,BUZZ_MEDIA_TYPE,BUZZ_MEDIA_FILE,BUZZ_THUMB_IMAGE FROM " +
                        BUZZ_MEDIA_TABLE_NAME + " where " + BUZZ_FEED_ID + " = '" + cursor.getString(0) + "'";
                Cursor cursorMedia = db.rawQuery(selectQueryMedia, null);
                int count = cursorMedia.getCount();
                List<news_feed_media> newsFeedMedia = new ArrayList<news_feed_media>();
                if (cursorMedia.moveToFirst()) {
                    do {
                        news_feed_media newsFeedMediaList = new news_feed_media();
                        newsFeedMediaList.setNews_feed_id(cursorMedia.getString(0));
                        newsFeedMediaList.setMedia_type(cursorMedia.getString(1));
                        newsFeedMediaList.setMediaFile(cursorMedia.getString(2));
                        newsFeedMediaList.setThumb_image(cursorMedia.getString(3));
                        // newsFeedMediaList.setWidth(cursorMedia.getString(4));
                       /* if(cursorMedia.getBlob(4)!=null)
                        newsFeedMediaList.setMedia_image(cursorMedia.getBlob(4));
*/
                        //  newsFeedMediaList.setHeight(cursorMedia.getString(6));
                        newsFeedMedia.add(newsFeedMediaList);
                    } while (cursorMedia.moveToNext());
                }
               /* String selectQueryMedia = "select btn.BUZZ_ID,btn.BUZZ_POST_STATUS,btn.BUZZ_EVENTID,btn.BUZZ_POST_DATE," +
                        "btn.BUZZ_FIRST_NAME,btn.BUZZ_LAST_NAME,btn.BUZZ_PROFILE_PIC,btn.BUZZ_ATTENDEE_ID,btn.BUZZ_CITY," +
                        "btn.BUZZ_RELATION,btn.BUZZ_RELATION_WITH,btn.BUZZ_LIKE_FLAG,btn.BUZZ_TOTAL_LIKE,btn.BUZZ_TOTAL_COMMENT," +

                        "btnm.BUZZ_FEED_ID,btnm.BUZZ_MEDIA_TYPE,btnm.BUZZ_MEDIA_FILE,btnm.BUZZ_THUMB_IMAGE,btnm.BUZZ_WIDTH,btnm.BUZZ_HEIGHT,btnm.BUZZ_MEDIA_IMAGE FROM"+
                        " BUZZ_TABLE_NAME as btn LEFT JOIN  BUZZ_MEDIA_TABLE_NAME as btnm on btn.BUZZ_ID = btnm.BUZZ_FEED_ID";*/
                newsfeedsList.setNews_feed_media(newsFeedMedia);
                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());

        }
        db.close();
        return newsFeedList;
    }

    public ArrayList<Quiz> getQuizList(String foldername) {
        String selectQuery = "select * from " + QUIZ_TABLE + " where " + FOLDERNAME + " LIKE \'%" + foldername + "%\'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Quiz> newsFeedList = new ArrayList<Quiz>();
        if (cursor.moveToFirst()) {

            do {
                Quiz newsfeedsList = new Quiz();
                newsfeedsList.setId(cursor.getString(0));
                newsfeedsList.setQuestion(cursor.getString(1));
                newsfeedsList.setCorrect_answer(cursor.getString(2));
                newsfeedsList.setFolder_name(cursor.getString(3));
                newsfeedsList.setReplied(cursor.getString(4));
                newsfeedsList.setSelected_option(cursor.getString(5));


                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public void updateNewsFeedId(String news_feed_id, String folderUniqueId, SQLiteDatabase db) {
        if (!news_feed_id.equalsIgnoreCase("null")) {
            db = this.getWritableDatabase();
            db.beginTransaction();
            try {


                String selectQuery = "select " + NEWS_FEED_ID + " from " + UPLOAD_MULTIMEDIA_TABLE + " where " + FOLDER_UNIQUE_ID + "='" + folderUniqueId + "'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                String news_feed_id1 = "";
                if (cursor.moveToFirst()) {
                    do {
                        if (news_feed_id1.isEmpty())
                            news_feed_id1 = cursor.getString(0);
                    } while (cursor.moveToNext());
                }

                if (news_feed_id1.length() == 0 || news_feed_id1.equals("false")) {
                    String sql = "UPDATE " + UPLOAD_MULTIMEDIA_TABLE + " set " + NEWS_FEED_ID + "='" + news_feed_id + "' where " + FOLDER_UNIQUE_ID + "='" + folderUniqueId + "'";
                    db.execSQL(sql);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return;
    }

    public ArrayList<ExhibitorCatList> getCatList(String foldername) {
        String selectQuery = "select * from " + EXHIBITOR_CATEGORY_MASTER_LIST + " where " + EXHIBITOR_CATEGORY_ID + " LIKE \'%" + foldername + "%\'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ExhibitorCatList> newsFeedList = new ArrayList<ExhibitorCatList>();
        if (cursor.moveToFirst()) {

            do {
                ExhibitorCatList newsfeedsList = new ExhibitorCatList();
                newsfeedsList.setExhibitor_category_id(cursor.getString(0));
                newsfeedsList.setName(cursor.getString(1));
                newsfeedsList.setTotal_exhibitor_count(cursor.getString(2));


                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public ArrayList<ExhiCatDetailList> getEXCatList(String foldername) {
        String selectQuery = "select * from " + EXHIBITOR_CATEGORY_LIST + " where " + EXHIBITOR_ID + " LIKE \'%" + foldername + "%\'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ExhiCatDetailList> newsFeedList = new ArrayList<ExhiCatDetailList>();
        if (cursor.moveToFirst()) {

            do {
                ExhiCatDetailList newsfeedsList = new ExhiCatDetailList();
                newsfeedsList.setExhibitor_category_id(cursor.getString(0));
                newsfeedsList.setName(cursor.getString(1));
                newsfeedsList.setExhibitor_id(cursor.getString(2));


                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public ArrayList<NewsFeedPostMultimedia> getNotUploadedMultiMedia() {
        String selectQuery = "select * from " + UPLOAD_MULTIMEDIA_TABLE + " where IS_UPLOADED='false'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<NewsFeedPostMultimedia> newsFeedPostMultimediaList = new ArrayList<NewsFeedPostMultimedia>();

        if (cursor.moveToFirst()) {
            do {

                NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
                newsFeedPostMultimedia.setMedia_file(cursor.getString(1));
                newsFeedPostMultimedia.setMedia_file_thumb(cursor.getString(2));
                newsFeedPostMultimedia.setCompressedPath(cursor.getString(3));
                newsFeedPostMultimedia.setMedia_type(cursor.getString(4));
                newsFeedPostMultimedia.setNews_feed_id(cursor.getString(5));
                newsFeedPostMultimedia.setFolderUniqueId(cursor.getString(6));
                newsFeedPostMultimedia.setIs_uploaded(cursor.getString(7));

                newsFeedPostMultimediaList.add(newsFeedPostMultimedia);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedPostMultimediaList;
    }

    public int getCountOfNotUploadedMultiMedia() {
        String selectQuery = "select * from " + UPLOAD_MULTIMEDIA_TABLE + " where IS_UPLOADED='false'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }


    public void updateMultimediaPath(String strPath, String compressedPath, String
            news_feed_id, SQLiteDatabase db) {

        db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql = "UPDATE TABLE_UPLOAD_MULTIMEDIA set MULTIMEDIA_COMPRESSED_FILE='" + compressedPath + "' " +
                    "where MULTIMEDIA_FILE='" + strPath + "' AND NEWS_FEED_ID='" + news_feed_id + "'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void updateMultimediaInfo(String strPath, String news_feed_id, SQLiteDatabase
            db, String media_file_thumb, String folderUniqueId) {

        db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            String whereCondition;
            if (strPath.contains("mp4")) {
                if (news_feed_id.isEmpty())
                    whereCondition = MULTIMEDIA_COMPRESSED_FILE + " ='" + strPath + "' AND " + FOLDER_UNIQUE_ID + "='" + folderUniqueId + "'";
                else
                    whereCondition = MULTIMEDIA_COMPRESSED_FILE + " ='" + strPath + "' AND " + NEWS_FEED_ID + "='" + news_feed_id + "'";
            } else {
                if (news_feed_id.isEmpty())
                    whereCondition = " MULTIMEDIA_FILE='" + strPath + "' AND FOLDER_UNIQUE_ID='" + folderUniqueId + "'";
                else
                    whereCondition = " MULTIMEDIA_FILE='" + strPath + "' AND NEWS_FEED_ID='" + news_feed_id + "'";
            }
            String sql = "UPDATE TABLE_UPLOAD_MULTIMEDIA set IS_UPLOADED='true' where " + whereCondition;
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public String getNewsFeedIdFromFolderUniqueId(String folderUniqueId) {
        String selectQuery = "select " + NEWS_FEED_ID + " from " + UPLOAD_MULTIMEDIA_TABLE + " where " + FOLDER_UNIQUE_ID + "='" + folderUniqueId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String news_feed_id = "";
        if (cursor.moveToFirst()) {
            do {
                if (news_feed_id.isEmpty())
                    news_feed_id = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        db.close();
        return news_feed_id;
    }

    public int getCountOfUploadedMultiMediaForNewsFeedId(String news_feed_id) {
        String selectQuery = "select * from " + UPLOAD_MULTIMEDIA_TABLE + " where IS_UPLOADED='false' and " + NEWS_FEED_ID + "='" + news_feed_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public int getCountOfUploadedMultiMediaForFolderUniqueId(String folderUniqueId) {
        String selectQuery = "select * from " + UPLOAD_MULTIMEDIA_TABLE + " where IS_UPLOADED='false' and " + FOLDER_UNIQUE_ID + "='" + folderUniqueId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public List<news_feed_media> getBuzzMediaFeedDetails() {
        List<news_feed_media> newsFeedList = new ArrayList<news_feed_media>();
        try {
            String selectQuery = "select * from " + BUZZ_MEDIA_TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            int count = cursor.getCount();
            int count1 = count;
            if (cursor.moveToFirst()) {

                do {
                    news_feed_media newsfeedsList = new news_feed_media();
                    newsfeedsList.setNews_feed_id(cursor.getString(0));
                    newsfeedsList.setMedia_type(cursor.getString(1));
                    newsfeedsList.setMediaFile(cursor.getString(2));
                    newsfeedsList.setThumb_image(cursor.getString(3));
                    newsfeedsList.setWidth(cursor.getString(4));
                    newsfeedsList.setHeight(cursor.getString(6));

                    if (cursor.getBlob(5) != null)
                        newsfeedsList.setMedia_image(cursor.getBlob(5));

                    newsFeedList.add(newsfeedsList);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsFeedList;
    }


    public void DeleteBrochure(String brid) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EXHIBITOR_BROCHURE_LIST + " where " + BROCHURE_ID + " LIKE \'%" + brid + "%\'");
    }

    public void clearQuizTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + QUIZ_TABLE);
    }


    public void clearAttendeesTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + ATTENDEES_TABLE_NAME);
    }

    public void clearBuddyTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + BUDDY_TABLE_NAME);
    }

    public void clearEventListTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EVENTINFO_TABLE);
    }

    public void clearSponsorTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + SPONSOR_TABLE);
    }

    public void clearAgendaTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_TABLE_NAME);
    }

    public void clearAgendaVacationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_VACATION_TABLE_NAME);
    }

    public void clearAgendaFolerTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_VACATION_MEDIA_TABLE);
    }

    public void clearSpeakersTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + SPEAKER_TABLE_NAME);
    }

    public void clearNewsFeedTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + NEWSFEED_TABLE_NAME);
    }

    public void clearUserDataTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + USER_TABLE_NAME);
    }

    public void clearCatagoryTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EXHIBITOR_CATEGORY_MASTER_LIST);
    }

    public void clearEXCatagoryTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EXHIBITOR_CATEGORY_LIST);
    }

    public void clearEXattendeeTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EXHIBITOR_ATTENDEE_LIST);
    }

    public void clearEXbrocherTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EXHIBITOR_BROCHURE_LIST);
    }

    public void clearNotificationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + NOTIFICATION_LIST);
    }

    public void clearExNotificationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EX_NOTIFICATION_LIST);
    }

    public void clearEXMeetingTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + EX_MEETING_TABLE);
    }

    public void deleteData(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void clearBuzzMediaFeedTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + BUZZ_MEDIA_TABLE_NAME);
        db.close();
    }

}