/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.maxleap.MaxLeap;
import com.maxleapmobile.gitmaster.database.DBHelper;

public class GithubApplication extends Application {

    private static GithubApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        MaxLeap.initialize(this, ApiKey.MAXLEAP_APP_ID, ApiKey.MAXLEAP_CLIENT_KEY);
        FlurryAgent.init(this, ApiKey.FLURRY_KEY);
        DBHelper.getInstance(this).delExpiredRepo();
    }

    public static GithubApplication getInstance() {
        return mInstance;
    }
}