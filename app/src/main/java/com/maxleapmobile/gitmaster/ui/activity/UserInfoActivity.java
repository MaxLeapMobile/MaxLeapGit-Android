/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.databinding.ActivityUserinfoBinding;
import com.maxleapmobile.gitmaster.ui.fragment.MineFragment;
import com.maxleapmobile.gitmaster.util.Const;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mFollow;
    private ActivityUserinfoBinding mBinding;
    private Boolean mFollowStatus = false;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo);
        initFragment();
        initView();
        followStatus();
        mBinding.setStatus(mFollowStatus);
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
            public void success(Object o, Response response) {
                if (response.getStatus() == 204) {
                    mFollowStatus = true;
                    mBinding.setStatus(mFollowStatus);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                if (error.getResponse().getStatus() == 404) {
                    mFollowStatus = false;
                    mBinding.setStatus(mFollowStatus);
                }
            }
        });

        mFollow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mFollowStatus) {
            ApiManager.getInstance().unfollow(mUsername, new ApiCallback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    mFollowStatus = false;
                    mBinding.setStatus(mFollowStatus);
                }

                @Override
                public void failure(RetrofitError error) {
                    super.failure(error);
                    mFollowStatus = true;
                    mBinding.setStatus(mFollowStatus);
                }
            });
        } else {
            ApiManager.getInstance().follow(mUsername, new ApiCallback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    mFollowStatus = true;
                    mBinding.setStatus(mFollowStatus);
                }

                @Override
                public void failure(RetrofitError error) {
                    super.failure(error);
                    mFollowStatus = false;
                    mBinding.setStatus(mFollowStatus);
                }
            });
        }
    }
}