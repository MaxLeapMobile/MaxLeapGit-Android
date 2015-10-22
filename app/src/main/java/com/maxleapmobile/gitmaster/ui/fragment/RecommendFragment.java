/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.maxleap.FindCallback;
import com.maxleap.FunctionCallback;
import com.maxleap.MLCloudManager;
import com.maxleap.MLObject;
import com.maxleap.MLQueryManager;
import com.maxleap.MLUser;
import com.maxleap.exception.MLException;
import com.maxleap.utils.FileHandle;
import com.maxleap.utils.FileHandles;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.model.RecommendRepo;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecommendFragment extends Fragment {

    private static final String SKIPPED_REPO_FILE_NAME = "skipped_repo_file_name";
    private static final String SKIPPED_REPOES = "skipped_repoes";
    private static final String SKIPPED_REPO = "skipped_repo_url";

    private Context mContext;
    private ProgressWebView mWebView;
    private LinearLayout mEmptyView;
    private Set<String> mSkipRepo;
    private FileHandle mFileHandle;
    private JSONObject mJsonObject;
    private List<RecommendRepo> recommendRepos;
    private List<Gene> genes;
    private Map<String, Object> mParmasMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mWebView = (ProgressWebView) view.findViewById(R.id.recommend_webview);
        mEmptyView = (LinearLayout) view.findViewById(R.id.recommend_empty);
        if (mParmasMap == null) {
            mParmasMap = new HashMap();
        }
        if (mSkipRepo == null) {
            mSkipRepo = Collections.synchronizedSet(new LinkedHashSet<String>());
            mFileHandle = FileHandles.files(SKIPPED_REPO_FILE_NAME);
            try {
                mJsonObject = mFileHandle.readJSONObject();
                if (mJsonObject != null) {
                    JSONArray jsonArray = mJsonObject.getJSONArray(SKIPPED_REPOES);
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        mSkipRepo.add(jsonArray.getJSONObject(i).getString(SKIPPED_REPO));
                    }
                }
            } catch (Exception e) {

            }
        }
        fetchGeneDate();
    }

    private void fetchGeneDate() {
        UserManager.getInstance().checkIsLogin(new OperationCallback() {
            @Override
            public void success() {
                MLQueryManager.findAllInBackground(MLUser.getCurrentUser().getRelation("genes").getQuery(), new FindCallback<MLObject>() {
                    @Override
                    public void done(List<MLObject> list, MLException e) {
                        genes = new ArrayList<>();
                        if (e == null) {
                            for (MLObject o : list) {
                                genes.add(Gene.from(o));
                            }
                            mParmasMap.put("userid", "bullda");
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < genes.size(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("language", genes.get(i).getLanguage());
                                    jsonObject.put("keyword", genes.get(i).getSkill());
                                    jsonArray.put(i, jsonObject);
                                } catch (Exception jsonException) {

                                }
                            }
                            mParmasMap.put("genes", jsonArray);
                            mParmasMap.put("page", 1);
                            mParmasMap.put("per_page", 10);
                            mParmasMap.put("type", "search");
                            MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<JSONArray>() {
                                @Override
                                public void done(JSONArray jsonArray, MLException e) {
                                    if (e == null) {
                                        if (recommendRepos == null) {
                                            recommendRepos = new ArrayList<>();
                                        }
                                        int length = jsonArray.length();
                                        for (int i = 0; i < length; i++) {
                                            try {
                                                recommendRepos.add(RecommendRepo.from(jsonArray.getJSONObject(i)));
                                            } catch (Exception jsonException) {
                                                continue;
                                            }
                                        }
                                    } else {
                                        Logger.toast(mContext, R.string.toast_get_recommend_failed);
                                    }
                                }
                            });
                        } else {
                            Logger.toast(mContext, R.string.toast_get_recommend_failed);
                        }
                    }
                });
            }

            @Override
            public void failed(String error) {
                Logger.toast(mContext, R.string.toast_get_recommend_failed);
            }
        });
    }
}
