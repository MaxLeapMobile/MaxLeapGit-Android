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
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;

public class RepoActivity extends BaseActivity {

    public static final String OWNER = "owner";
    public static final String REPONAME = "reponame";
    public static final String REPOURL = "url";

    private Toolbar mToolbar;
    private TextView mTitle;
    private ProgressWebView mWebView;
    private TextView mStar;
    private TextView mFork;

    private String mRepoOwner;
    private String mRepoName;
    private String mRepoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        mRepoOwner = getIntent().getStringExtra(OWNER);
        mRepoName = getIntent().getStringExtra(REPONAME);
        mRepoUrl = getIntent().getStringExtra(REPOURL);
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
        mWebView.loadUrl(mRepoUrl);
        mStar = (TextView) findViewById(R.id.repo_star);
        mFork = (TextView) findViewById(R.id.repo_fork);
    }
}