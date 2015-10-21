/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

public class PermissionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        init();
    }

    private void init() {
        if (!TextUtils.isEmpty(PreferenceUtil.getString(this, Const.ACCESS_TOKEN_KEY, null))
                && !TextUtils.isEmpty(PreferenceUtil.getString(this, Const.USERNAME, null))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void toLoginActivity(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.FROM_PERMISSION_ACTIVITY, true);
        startActivity(intent);
        finish();
    }
}
