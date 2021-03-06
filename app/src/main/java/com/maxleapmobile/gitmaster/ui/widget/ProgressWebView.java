/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maxleap.external.volley.RequestQueue;
import com.maxleap.external.volley.Response;
import com.maxleap.external.volley.VolleyError;
import com.maxleap.external.volley.toolbox.StringRequest;
import com.maxleap.external.volley.toolbox.Volley;
import com.maxleapmobile.gitmaster.GithubApplication;

public class ProgressWebView extends WebView {

    private static final String READ_ME_SUFFIX = "/blob/master/README.md";

    private WebViewProgressBar progressBar;
    private Handler handler;
    private WebView _this;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new WebViewProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setVisibility(GONE);
        addView(progressBar);
        handler = new Handler();
        _this = this;
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebClient());

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setProgress(100);
                handler.postDelayed(runnable, 200);
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            if (newProgress < 5) {
                newProgress = 5;
            }
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            _this.loadUrl(url);
            return true;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };

    public void loadUrl(final String url, boolean isRepo) {
        final String readMeUrl = url + READ_ME_SUFFIX;
        if (isRepo) {
            RequestQueue mQueue = Volley.newRequestQueue(GithubApplication.getInstance());
            mQueue.add(new StringRequest(readMeUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    loadUrl(readMeUrl);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    loadUrl(url);
                }
            }));
        } else {
            loadUrl(readMeUrl);
        }
    }
}