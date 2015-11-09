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

public class RepoDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String OWNER = "owner";
    public static final String REPONAME = "reponame";

    private Toolbar mToolbar;
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
        mToolbar.setTitle(mRepoName);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView = (ProgressWebView) findViewById(R.id.repo_webview);
        mStar = (TextView) findViewById(R.id.repo_star);
        mFork = (TextView) findViewById(R.id.repo_fork);
        mProgressBar = (ProgressBar) findViewById(R.id.repo_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        ApiManager.getInstance().getRepo(mRepoOwner, mRepoName, new ApiCallback<Repo>() {
            @Override
            public void onSuccess(Repo repo) {
                mProgressBar.setVisibility(View.GONE);
                mWebView.loadUrl(repo.getHtmlUrl(), true);
            }

            @Override
            public void onFail(Throwable throwable) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

        ApiManager.getInstance().isStarred(mRepoOwner, mRepoName, new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object o) {
                mStar.setText(R.string.bottom_label_unstar);
            }

            @Override
            public void onFail(Throwable throwable) {
                mStar.setText(R.string.bottom_label_star);
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
                    public void onSuccess(ForkRepo forkRepo) {
                        Logger.toast(getApplicationContext(),
                                getString(R.string.toast_fork_success) + forkRepo.getFullName());
                    }

                    @Override
                    public void onFail(Throwable throwable) {
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
                public void onSuccess(Object o) {
                    Logger.toast(getApplicationContext(),
                            getString(R.string.toast_star_success));
                    mStar.setText(R.string.bottom_label_unstar);
                }

                @Override
                public void onFail(Throwable throwable) {
                    Logger.toast(getApplicationContext(),
                            getString(R.string.toast_star_failed));
                }
            });
        } else {
            ApiManager.getInstance().unstart(mRepoOwner, mRepoName, new ApiCallback<Object>() {
                @Override
                public void onSuccess(Object o) {
                    Logger.toast(getApplicationContext(),
                            getString(R.string.toast_unstar_success));
                    mStar.setText(R.string.bottom_label_star);
                }

                @Override
                public void onFail(Throwable throwable) {
                    Logger.toast(getApplicationContext(), getString(R.string.toast_unstar_failed));
                }
            });
        }
    }
}