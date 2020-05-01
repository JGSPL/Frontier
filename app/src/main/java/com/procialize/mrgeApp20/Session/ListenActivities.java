package com.procialize.mrgeApp20.Session;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class ListenActivities extends Thread{
    boolean exit = false;
    ActivityManager am = null;
    Context context = null;
    SessionManager session;
    public ListenActivities(Context con){
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        session = new SessionManager(con);
    }

    public void run(){

        Looper.prepare();

        while(!exit){

            // get the info from the currently running task
            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(MAX_PRIORITY);

            String activityName = taskInfo.get(0).topActivity.getClassName();


            Log.d("topActivity", "CURRENT Activity ::"
                    + activityName);

            if (activityName.equals("com.procialize.eventsapp.UninstallerActivity")) {

                session.logoutUser();
                SharedPreferences.Editor pref = context.getSharedPreferences("PROFILE_PICTURE", context.MODE_PRIVATE).edit();
                pref.clear();
                // User has clicked on the Uninstall button under the Manage Apps settings

                //do whatever pre-uninstallation task you want to perform here
                // show dialogue or start another activity or database operations etc..etc..

                // context.startActivity(new Intent(context, MyPreUninstallationMsgActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                exit = true;
                Toast.makeText(context, "Done with preuninstallation tasks... Exiting Now", Toast.LENGTH_SHORT).show();
            } else if(activityName.equals("com.procialize.eventsapp.MyApplications")) {
                // back button was pressed and the user has been taken back to Manage Applications window
                // we should close the activity monitoring now
                exit=true;
            }
        }
        Looper.loop();
    }
}
