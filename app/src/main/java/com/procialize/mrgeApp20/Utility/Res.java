package com.procialize.mrgeApp20.Utility;

import android.content.res.Resources;
import android.os.Build;

import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;

/**
 * Created by Rahul on 03-11-2018.
 */

public class Res extends Resources {


    public Res(Resources original) {
        super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
    }

    @Override
    public int getColor(int id) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "activetab":
                // You can change the return value to an instance field that loads from SharedPreferences.
                return MrgeHomeActivity.activetab; // used as an example. Change as needed.
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id);
                }
                return super.getColor(id);
        }
    }

/*
    @Override public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }*/

/*
    @Override public int getColor(int id, Theme theme) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "activetab":
                // You can change the return value to an instance field that loads from SharedPreferences.
                return MrgeHomeActivity.activetab; // used as an example. Change as needed.
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id, theme);
                }
                return super.getColor(id);
        }
    }
*/
}
