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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.ui.activity.GeneActivity;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.ui.widget.CustomClickableSpan;
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
    private List<Repo> repos;
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
        TextView notice2 = (TextView) view.findViewById(R.id.recommend_notice2);
        SpannableString notice2SS = new SpannableString(mContext.getString(R.string.recommend_notice2_part1)
                + mContext.getString(R.string.recommend_notice2_part2));
        notice2SS.setSpan(new CustomClickableSpan(new CustomClickableSpan.TextClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(mContext, GeneActivity.class);
                startActivity(intent);
            }
        }), 0, mContext.getString(R.string.recommend_notice2_part1).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice2.setText(notice2SS.toString());
        TextView notice3 = (TextView) view.findViewById(R.id.recommend_notice3);
        SpannableString notice3SS = new SpannableString(mContext.getString(R.string.recommend_notice3_part1)
                + mContext.getString(R.string.recommend_notice3_part2));
        notice3SS.setSpan(new CustomClickableSpan(new CustomClickableSpan.TextClickListener() {
            @Override
            public void onClick() {
                mEmptyView.setVisibility(View.GONE);
                mWebView.loadUrl(repos.get(0).getHtmlUrl());
            }
        }), mContext.getString(R.string.recommend_notice3_part1).length(), notice3SS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice3.setText(notice3SS.toString());

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
                                    jsonObject.put("skill", genes.get(i).getSkill());
                                    jsonArray.put(i, jsonObject);
                                } catch (Exception jsonException) {

                                }
                            }
                            mParmasMap.put("genes", jsonArray);
                            mParmasMap.put("page", 1);
                            mParmasMap.put("per_page", 10);
                            mParmasMap.put("type", "search");
                            MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<List<HashMap<String, Object>>>() {
                                @Override
                                public void done(List<HashMap<String, Object>> list, MLException e) {
                                    if (e == null) {
                                        if (repos == null) {
                                            repos = new ArrayList<>();
                                        }
                                        int length = list.size();
                                        for (int i = 0; i < length; i++) {
                                            try {
                                                repos.add(from(list.get(i)));
                                            } catch (Exception jsonException) {
                                                continue;
                                            }
                                        }
                                        mEmptyView.setVisibility(View.GONE);
                                        mWebView.loadUrl(repos.get(0).getHtmlUrl());
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

    public static Repo from(HashMap<String, Object> object) throws Exception {
        Repo repo = new Repo();

        repo.setDescription(object.get("description").toString());
        repo.setForksCount(Long.valueOf(object.get("forksCount").toString()));
        repo.setHtmlUrl(object.get("htmlUrl").toString());
        repo.setId(Long.valueOf(object.get("id").toString()));
        repo.setPrivateRepo(Boolean.valueOf(object.get("isPrivate").toString()));
        repo.setLanguage(object.get("language").toString());
        repo.setName(object.get("name").toString());
        Owner owner = new Owner();
        owner.setAvatarUrl(object.get("ownerAvatarUrl").toString());
        owner.setFollowersUrl(object.get("ownerFollowersUrl").toString());
        owner.setFollowingUrl(object.get("ownerFollowingUrl").toString());
        owner.setHtmlUrl(object.get("ownerHtmlUrl").toString());
        owner.setId(Integer.valueOf(object.get("ownerId").toString()));
        owner.setLogin(object.get("ownerLogin").toString());
        owner.setReposUrl(object.get("ownerReposUrl").toString());
        repo.setOwner(owner);
        repo.setStargazersCount(Long.valueOf(object.get("stargazersCount").toString()));

        return repo;
    }

}
