package com.procialize.mrgeApp20.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CommonFunction {

    public static void crashlytics(String page,String userId) {
        Crashlytics.setString("Screen", page);
        Crashlytics.log(page);
        Crashlytics.setUserIdentifier(userId);
    }

    public static void firbaseAnalytics(Context context, String page, String userId) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("Screen",page);// foods[1]);
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(10000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(1800000);
        //Sets the user ID property.
        firebaseAnalytics.setUserId(userId);
        firebaseAnalytics.setCurrentScreen(( Activity ) context, page, null /* class override */);
    }

}
