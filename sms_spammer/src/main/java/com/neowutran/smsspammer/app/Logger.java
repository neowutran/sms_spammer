package com.neowutran.smsspammer.app;

import android.util.Log;

/**
 * Created by draragar on 4/12/14.
 */
public class Logger {

    public static void error(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
}
