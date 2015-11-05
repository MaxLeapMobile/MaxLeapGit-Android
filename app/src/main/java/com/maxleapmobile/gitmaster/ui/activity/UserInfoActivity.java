/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.ui.fragment.MineFragment;
import com.maxleapmobile.gitmaster.util.Const;

import retrofit.Response;
import retrofit.Retrofit;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mFollow;
    private Boolean mFollowStatus = false;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initFragment();
        initView();
        followStatus();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.userinfo_title);
        mTitle.setText(mUsername);

        mFollow = (TextView) findViewById(R.id.userinfo_follow);

        mToolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment() {
        Bundle bundle = getIntent().getExtras();
        mUsername = bundle.getString(Const.USERNAME);
        MineFragment mineFragment = new MineFragment();
        mineFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.userinfo_content_main, mineFragment);
        transaction.commit();
    }

    private void followStatus() {
        ApiManager.getInstance().followStatus(mUsername, new ApiCallback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 204) {
                        mFollowStatus = true;
                        mFollow.setText(getString(R.string.activity_userinfo_unfollow));
                    } else if (response.code() == 404) {
                        mFollowStatus = false;
                        mFollow.setText(getString(R.string.activity_userinfo_follow));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mFollowStatus = false;
                mFollow.setText(getString(R.string.activity_userinfo_follow));
            }
        });

        mFollow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mFollowStatus) {
            ApiManager.getInstance().unfollow(mUsername, new ApiCallback<Object>() {
                @Override
                public void onResponse(Response<Object> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.code() == 204) {
                            mFollowStatus = false;
                            mFollow.setText(getString(R.string.activity_userinfo_follow));
                        } else if (response.code() == 404) {
                            mFollowStatus = true;
                            mFollow.setText(getString(R.string.activity_userinfo_unfollow));
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mFollowStatus = true;
                    mFollow.setText(getString(R.string.activity_userinfo_unfollow));
                }
            });
        } else {
            ApiManager.getInstance().follow(mUsername, new ApiCallback<Object>() {
                @Override
                public void onResponse(Response<Object> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.code() == 204) {
                            mFollowStatus = true;
                            mFollow.setText(getString(R.string.activity_userinfo_unfollow));
                        } else if (response.code() == 404) {
                            mFollowStatus = false;
                            mFollow.setText(getString(R.string.activity_userinfo_follow));
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mFollowStatus = false;
                    mFollow.setText(getString(R.string.activity_userinfo_follow));
                }
            });
        }
    }
}