/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logger {
    private final static String LOG_TAG = "Debug_Logger";

    private static boolean logOpen;

    public static void d(String content) {
        if (logOpen)
            Log.d(LOG_TAG, content);
    }

    public static void d(String tag, String content) {
        if (logOpen)
            Log.d(tag, content);
    }

    public static void e(String content) {
        if (logOpen)
            Log.e(LOG_TAG, content);
    }

    public static void e(String tag, String content) {
        if (logOpen)
            Log.e(tag, content);
    }

    public static void i(String content) {
        if (logOpen)
            Log.i(LOG_TAG, content);
    }

    public static void i(String tag, String content) {
        if (logOpen)
            Log.i(tag, content);
    }

    public static void v(String content) {
        if (logOpen)
            Log.v(LOG_TAG, content);
    }

    public static void v(String tag, String content) {
        if (logOpen)
            Log.v(tag, content);
    }

    public static void w(String content) {
        if (logOpen)
            Log.w(LOG_TAG, content);
    }

    public static void w(String tag, String content) {
        if (logOpen)
            Log.w(tag, content);
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void setLogOpen(boolean isopen) {
        logOpen = isopen;
    }

    public static boolean isLogOpen() {
        return logOpen;
    }
}