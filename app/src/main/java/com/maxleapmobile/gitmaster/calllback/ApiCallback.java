/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.calllback;

import android.content.Context;

import com.maxleapmobile.gitmaster.GithubApplication;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.GithubError;
import com.maxleapmobile.gitmaster.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;

public abstract class ApiCallback<T> implements Callback<T> {

    private Context mContext;

    public ApiCallback() {
        this.mContext = GithubApplication.getInstance();
    }

    @Override
    public void failure(RetrofitError error) {
        if (error.getKind() == RetrofitError.Kind.NETWORK) {
            Logger.toast(mContext,
                    mContext.getString(R.string.toast_network_error));
        } else {

            if (error.getResponse() == null) {
                Logger.e("----->" +
                        mContext.getString(R.string.toast_request_unexpected_error) + "<-----");
                Logger.toast(mContext,
                        mContext.getString(R.string.toast_request_unexpected_error));
            } else {
                GithubError githubError = (GithubError) error.getBodyAs(GithubError.class);
                Logger.e("------> Error Message: " + githubError.getMessage() +
                        ", Refer to: " + githubError.getDocumentUrl() + " <------");
            }
        }
    }
}