package com.procialize.frontier.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.procialize.frontier.GetterSetter.EventMenuSettingList;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.GetterSetter.UserEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SessionManager {
    // User name (make variable public to access from outside)
    public static final String MY_PREFS_NAME = "ProcializeInfo";
    public static final String KEY_USER_ID = "attendee_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LNAME = "lname";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    // Designation  (make variable public to access from outside)
    public static final String KEY_DESIGNATION = "designation";
    public static final String KEY_LASTNAME = "lastname";
    // Company  (make variable public to access from outside)
    public static final String KEY_COMPANY = "company";
    // mobile (make variable public to access from outside)
    public static final String KEY_MOBILE = "mobile";
    // TOKEN (make variable public to access from outside)
    public static final String KEY_TOKEN = "api_access_token";
    public static final String EVENT_ID = "event_id";

    // Email address (make variable public to access from outside)
//    public static final String KEY_EMAILID = "emailid";
    // description (make variable public to access from outside)
    public static final String KEY_DESCRIPTION = "description";
    // description (make variable public to access from outside)
    public static final String KEY_CITY = "city";
    public static final String KEY_GCM_ID = "gcm_id";
    // country (make variable public to access from outside)
    public static final String KEY_COUNTRY = "country";
    // PIC (make variable public to access from outside)
    public static final String KEY_PIC = "profile_pic";
    public static final String KEY_ID = "id";
    public static final String KEY_SKIP_FLAG = "skip_flag";
    public static final String ATTENDEE_STATUS = "attendee_status";
    public static final String EXHIBITOR_STATUS = "exhibitor_status";
    public static final String EXHIBITOR_ID = "exhibitor_id";
    // Sharedpref file name
    private static final String PREF_NAME = "Pref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    static SharedPreferences pref;
    // Editor for Shared preferences
    static SharedPreferences.Editor editor;
    //    public static final String KEY_mobno= "profile_pic";
//    public static final String KEY_email= "profile_pic";
//    public static final String KEY_country= "profile_pic";
//    public static final String KEY_description= "profile_pic";
//    public static final String KEY_city= "profile_pic";
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        try {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSharedPreferencesEventList(List<EventSettingList> callLog) {
        Gson gson = new Gson();
        String json = gson.toJson(callLog);
        editor.putString("eventlist", json);
        editor.commit();
    }

    public static void saveSharedPreferencesMenuEventList(List<EventMenuSettingList> callLog) {

        Gson gson = new Gson();
        String json = gson.toJson(callLog);
        editor.putString("menueventlist", json);
        editor.commit();
    }

    public static List<EventSettingList> loadEventList() {
        List<EventSettingList> callLog = new ArrayList<EventSettingList>();
        Gson gson = new Gson();
        String json = pref.getString("eventlist", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<EventSettingList>();
        } else {
            Type type = new TypeToken<List<EventSettingList>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public static List<EventMenuSettingList> loadMenuEventList() {
        List<EventMenuSettingList> callLog = new ArrayList<EventMenuSettingList>();
        Gson gson = new Gson();
        String json = pref.getString("menueventlist", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<EventMenuSettingList>();
        } else {
            Type type = new TypeToken<List<EventMenuSettingList>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    /**
     * Create login session
     */
    public void createLoginSession(String fstname, String lstname, String email, String mobile, String company,
                                   String designation, String token, String desc, String city, String country, String pic, String id, String emailid,
                                   String password, String skip_flag, String attendee_status, String exhibitor_id, String exhibitor_status) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, fstname);
        editor.putString(KEY_LNAME, lstname);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing mobile in pref
        editor.putString(KEY_MOBILE, mobile);

        // Storing company in pref
        editor.putString(KEY_COMPANY, company);


        // Storing designation in pref
        editor.putString(KEY_DESIGNATION, designation);

        // Storing token in pref
        editor.putString(KEY_TOKEN, token);

        // Storing description in pref
        editor.putString(KEY_DESCRIPTION, desc);

        // Storing city in pref
        editor.putString(KEY_CITY, city);

        // Storing country in pref
        editor.putString(KEY_COUNTRY, country);

        // Storing pic in pref
        editor.putString(KEY_PIC, pic);
        editor.putString(KEY_PIC, pic);
        editor.putString(KEY_SKIP_FLAG, skip_flag);

        // Storing pic in pref
        editor.putString(KEY_ID, id);

        // Storing email in pref
        editor.putString(KEY_EMAIL, emailid);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(ATTENDEE_STATUS, attendee_status);
        editor.putString(EXHIBITOR_STATUS, exhibitor_status);
        editor.putString(EXHIBITOR_ID, exhibitor_id);

        // commit changes
        editor.commit();
    }

    public String getSkipFlag() {

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        String skip_flag = pref.getString(KEY_SKIP_FLAG, null);

        return skip_flag;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
           /* Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);*/
        } else {
           /* Intent i = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);*/
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_LNAME, pref.getString(KEY_LNAME, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user mobile
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));

        // user designation
        user.put(KEY_DESIGNATION, pref.getString(KEY_DESIGNATION, null));

        // user company
        user.put(KEY_COMPANY, pref.getString(KEY_COMPANY, null));


        // user token
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));


        // user desc
        user.put(KEY_DESCRIPTION, pref.getString(KEY_DESCRIPTION, null));


        // user city
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));


        // user country
        user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));

        // user pic
        user.put(KEY_PIC, pref.getString(KEY_PIC, null));

        //user Id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(ATTENDEE_STATUS, pref.getString(ATTENDEE_STATUS, null));
        user.put(EXHIBITOR_ID, pref.getString(EXHIBITOR_ID, null));
        user.put(EXHIBITOR_STATUS, pref.getString(EXHIBITOR_STATUS, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createProfileSession(String userId,String name, String company, String designation, String pic, String lastname,
                                     String city, String description, String country, String email, String mobno, String attendee_type, String exhibitor_id,
                                     String exhibitor_status) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, userId);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing company in pref
        editor.putString(KEY_COMPANY, company);

        // Storing designation in pref
        editor.putString(KEY_DESIGNATION, designation);
        editor.putString(KEY_LNAME, lastname);

        // Storing pic in pref
        editor.putString(KEY_PIC, pic);

        editor.putString(KEY_CITY, city);
        editor.putString(KEY_DESCRIPTION, description);
        editor.putString(KEY_COUNTRY, country);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobno);
        editor.putString(ATTENDEE_STATUS, attendee_type);
        editor.putString(EXHIBITOR_ID, exhibitor_id);
        editor.putString(EXHIBITOR_STATUS, exhibitor_status);

        // commit changes
        editor.commit();
    }

    public String getGcmID() {

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        String gcmRegID = pref.getString(KEY_GCM_ID, "");

        return gcmRegID;
    }

    public void storeGcmID(String gcmRegID) {
        // Storing eventId in pref
        editor.putString(KEY_GCM_ID, gcmRegID);
        // commit changes
        editor.commit();
    }

    public UserEvent getUserEvent() {
        SharedPreferences prefs = _context.getSharedPreferences(SessionManager.MY_PREFS_NAME, MODE_PRIVATE);
        return new UserEvent(
                prefs.getString(EVENT_ID, "1"),
                prefs.getString("eventnamestr", ""),
                prefs.getString("logoImg", ""),
                prefs.getString("eventback", ""),
                prefs.getString("colorActive", "")
        );
    }


    public void saveCurrentEvent(UserEvent userEvent) {
        SharedPreferences prefs = _context.getSharedPreferences(SessionManager.MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EVENT_ID, userEvent.getEventId()).commit();
        editor.putString("eventnamestr", userEvent.getName()).commit();
        editor.putString("logoImg", userEvent.getLogo()).commit();
        editor.putString("eventback", userEvent.getBackgroundImage()).commit();
        editor.putString("colorActive", userEvent.getColorActive()).commit();
        editor.apply();
    }

}
