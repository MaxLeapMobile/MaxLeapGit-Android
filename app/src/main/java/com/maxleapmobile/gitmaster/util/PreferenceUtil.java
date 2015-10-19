/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Set;

public class PreferenceUtil {

    private PreferenceUtil() {}

    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPreferences(context).getLong(key, defaultValue);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPreferences(context).getFloat(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void putString(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }
    @TargetApi(11)
    public static void putStringSet(Context context, String key, Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getEditor(context).putStringSet(key, value).apply();
        }
    }

    public static void putInt(Context context, String key, int value) {
        getEditor(context).putInt(key, value).apply();
    }

    public static void putLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).apply();
    }

    public static void putFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).apply();
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key).apply();
    }

    public static void clear(Context context) {
        getEditor(context).clear().apply();
    }
}