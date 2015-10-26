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

import com.maxleap.MaxLeap;
import com.maxleapmobile.gitmaster.database.DBHelper;
import com.maxleapmobile.gitmaster.util.Logger;

public class GithubApplication extends Application {

    private static GithubApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Logger.setLogOpen(true);

        MaxLeap.initialize(this, "560894ff60b2c290e2ce88eb", "OW16QzA4VTRjcGtpRXgyeGxoVVl4QQ");
        DBHelper.getInstance(this).delExpiredRepo();
    }

    public static GithubApplication getInstance() {
        return mInstance;
    }
}