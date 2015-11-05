/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maxleapmobile.gitmaster.ApiKey;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.api.GithubApi;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.AccessToken;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.Logger;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends BaseActivity {

    private static final String AUTH_URL = "https://github.com/login/oauth/authorize?";

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String FROM_PERMISSION_ACTIVITY = "from_permission_activity";

    private StringBuilder sb;
    private Context mContext;
    private ProgressWebView mWebView;
    private boolean isFromPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        initToolBar();
        initViews();
    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_login_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        isFromPermission = getIntent().getBooleanExtra(FROM_PERMISSION_ACTIVITY, false);
        mWebView = (ProgressWebView) findViewById(R.id.login_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new OauthWebViewClient());

        sb = new StringBuilder(AUTH_URL);
        sb.append("client_id=" + ApiKey.GITHUB_ID);
        sb.append("&redirect_uri=" + Const.CALLBACK_URL);
        sb.append("&scope=user,repo");
        mWebView.loadUrl(sb.toString());
    }

    private void requestAccessToken(String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://github.com/")
                .build();
        GithubApi githubApi = retrofit.create(GithubApi.class);
        githubApi.getAccessToken(ApiKey.GITHUB_ID, ApiKey.GITHUB_SECRET, Const.CALLBACK_URL,
                code).enqueue(new ApiCallback<AccessToken>() {

            @Override
            public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    AccessToken accessToken = response.body();
                    Logger.d(TAG, accessToken.getAccessToken() + " " + accessToken.getTokenType());
                    PreferenceUtil.putString(mContext, Const.ACCESS_TOKEN_KEY,
                            accessToken.getAccessToken());
                    getGithubUserInfo();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private class OauthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.d(TAG, "Redirecting url: " + url);

            if (url.startsWith(Const.CALLBACK_URL + "?code")) {
                String urls[] = url.split("=");
                String code = urls[1];
                requestAccessToken(code);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private void getGithubUserInfo() {
        ApiManager.getInstance().getCurrentUser(new ApiCallback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    User user = response.body();
                    Logger.d(TAG, user.getEmail() + " " + user.getName());
                    PreferenceUtil.putString(mContext, Const.USERNAME, user.getLogin());
                    if (isFromPermission) {
                        toMainActivity();
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.toast(mContext, R.string.toast_login_failed);
                mWebView.loadUrl(sb.toString());
            }

        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}