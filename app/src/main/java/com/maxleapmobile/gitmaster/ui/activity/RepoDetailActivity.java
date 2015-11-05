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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.ForkRepo;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.util.Logger;

import retrofit.Response;
import retrofit.Retrofit;

public class RepoDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String OWNER = "owner";
    public static final String REPONAME = "reponame";

    private Toolbar mToolbar;
    private TextView mTitle;
    private ProgressWebView mWebView;
    private TextView mStar;
    private TextView mFork;
    private ProgressBar mProgressBar;

    private String mRepoOwner;
    private String mRepoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        mRepoOwner = getIntent().getStringExtra(OWNER);
        mRepoName = getIntent().getStringExtra(REPONAME);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.repo_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(mRepoName);
        mWebView = (ProgressWebView) findViewById(R.id.repo_webview);
        mStar = (TextView) findViewById(R.id.repo_star);
        mFork = (TextView) findViewById(R.id.repo_fork);
        mProgressBar = (ProgressBar) findViewById(R.id.repo_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        ApiManager.getInstance().getRepo(mRepoOwner, mRepoName, new ApiCallback<Repo>() {
            @Override
            public void onResponse(Response<Repo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Repo repo = response.body();
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.loadUrl(repo.getHtmlUrl(), true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

        ApiManager.getInstance().isStarred(mRepoOwner, mRepoName, new ApiCallback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 204) {
                        mStar.setText(R.string.bottom_label_unstar);
                    } else if (response.code() == 404) {
                        mStar.setText(R.string.bottom_label_star);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        mStar.setOnClickListener(this);
        mFork.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repo_star:
                star();
                break;
            case R.id.repo_fork:
                ApiManager.getInstance().fork(mRepoOwner, mRepoName, new ApiCallback<ForkRepo>() {
                    @Override
                    public void onResponse(Response<ForkRepo> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            ForkRepo forkRepo = response.body();
                            Logger.toast(getApplicationContext(),
                                    getString(R.string.toast_fork_success) + forkRepo.getFullName());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Logger.toast(getApplicationContext(), getString(R.string.toast_fork_failed));
                    }
                });
                break;
        }
    }

    private void star() {
        if (mStar.getText().equals(getString(R.string.bottom_label_star))) {
            ApiManager.getInstance().star(mRepoOwner, mRepoName, new ApiCallback<Object>() {
                @Override
                public void onResponse(Response<Object> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.code() == 204) {
                            Logger.toast(getApplicationContext(),
                                    getString(R.string.toast_star_success));
                            mStar.setText(R.string.bottom_label_unstar);
                        } else if (response.code() == 404) {
                            Logger.toast(getApplicationContext(),
                                    getString(R.string.toast_star_failed));
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Logger.toast(getApplicationContext(), getString(R.string.toast_star_failed));
                }
            });
        } else {
            ApiManager.getInstance().unstart(mRepoOwner, mRepoName, new ApiCallback<Object>() {
                @Override
                public void onResponse(Response<Object> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.code() == 204) {
                            Logger.toast(getApplicationContext(),
                                    getString(R.string.toast_unstar_success));
                            mStar.setText(R.string.bottom_label_star);
                        } else if (response.code() == 404) {
                            Logger.toast(getApplicationContext(), getString(R.string.toast_unstar_failed));
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Logger.toast(getApplicationContext(), getString(R.string.toast_unstar_failed));
                }
            });
        }
    }
}