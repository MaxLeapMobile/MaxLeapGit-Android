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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.FunctionCallback;
import com.maxleap.MLCloudManager;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLUser;
import com.maxleap.exception.MLException;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.database.DBHelper;
import com.maxleapmobile.gitmaster.database.DBRecRepo;
import com.maxleapmobile.gitmaster.model.ForkRepo;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.ui.activity.AddGeneActivity;
import com.maxleapmobile.gitmaster.ui.widget.CustomClickableSpan;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.Logger;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RecommendFragment extends Fragment implements View.OnClickListener {

    private static final int PER_PAGE = 10;
    private int page = 1;

    private Context mContext;
    private ProgressWebView mWebView;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyView;
    private TextView starText;
    private ProgressBar starProgressBar;
    private TextView notice3;
    private View skipBtn;

    private String username;
    private MLQuery<MLObject> query;
    private List<Repo> repos;
    private ArrayList<Gene> genes;
    private Map<String, Object> mParmasMap;
    private int nowPosition;
    private DBRecRepo dbRecRepo;
    private DBHelper dbHelper;
    private boolean isEnd;
    private boolean isReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        username = PreferenceUtil.getString(mContext, Const.USERNAME, null);
        query = MLQuery.getQuery("Gene");
        query.whereEqualTo("githubName", username);
        dbHelper = DBHelper.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        starProgressBar = (ProgressBar) view.findViewById(R.id.recommend_star_progressbar);
        starText = (TextView) view.findViewById(R.id.recommend_star);
        starText.setOnClickListener(this);
        view.findViewById(R.id.recommend_fork).setOnClickListener(this);
        skipBtn = view.findViewById(R.id.recommend_skip);
        skipBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.repo_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);
        TextView notice2 = (TextView) view.findViewById(R.id.recommend_notice2);
        SpannableString notice2SS = new SpannableString(mContext.getString(R.string.recommend_notice2_part1)
                + " " + mContext.getString(R.string.recommend_notice2_part2));
        notice2SS.setSpan(new CustomClickableSpan(), 0, mContext.getString(R.string.recommend_notice2_part1).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice2.setText(notice2SS);
        notice2.setOnClickListener(this);
        notice3 = (TextView) view.findViewById(R.id.recommend_notice3);
        final SpannableString notice3SS = new SpannableString(mContext.getString(R.string.recommend_notice3_part1)
                + " " + mContext.getString(R.string.recommend_notice3_part2));
        notice3SS.setSpan(new CustomClickableSpan(), mContext.getString(R.string.recommend_notice3_part1).length(), notice3SS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice3.setText(notice3SS);
        notice3.setOnClickListener(this);

        mWebView = (ProgressWebView) view.findViewById(R.id.recommend_webview);

        mEmptyView = (LinearLayout) view.findViewById(R.id.recommend_empty);
        mEmptyView.setVisibility(View.GONE);
        if (mParmasMap == null) {
            mParmasMap = new HashMap();
            mParmasMap.put("userid", username);
            mParmasMap.put("page", page);
            mParmasMap.put("per_page", PER_PAGE);
        }
        if (repos == null) {
            genes = new ArrayList<>();
            repos = new ArrayList<>();
            getGenes();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEnd) {
            mProgressBar.setVisibility(View.VISIBLE);
            compareGenes();
        }
    }

    private void getGenes() {
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                if (e == null) {
                    Logger.d("get genes success");
                    for (MLObject o : list) {
                        genes.add(Gene.from(o));
                    }
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
                    fetchTrendingGeneDate();
                } else {
                    Logger.d("get genes failed");
                    if (e.getCode() == MLException.OBJECT_NOT_FOUND) {
                        isEnd = true;
                        fetchSearchGeneDate();
                    } else {
                        Logger.toast(mContext, R.string.toast_get_recommend_failed);
                    }
                }
            }
        });
    }

    private void compareGenes() {
        Logger.d("compareGenes start");
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                if (e == null) {
                    Logger.d("compareGenes success");
                    boolean needRefresh = false;
                    ArrayList<Gene> newGenes = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Gene gene = Gene.from(list.get(i));
                        if (!needRefresh && !genes.contains(gene)) {
                            genes.add(gene);
                            needRefresh = true;
                        }
                        newGenes.add(gene);
                    }
                    if (needRefresh) {
                        genes = newGenes;
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
                        isEnd = false;
                        isReview = false;
                        repos.clear();
                        fetchTrendingGeneDate();
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.GONE);
                    }
                } else {
                    Logger.d("compareGenes failed");
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void fetchTrendingGeneDate() {
        mParmasMap.put("type", "trending");
        Logger.d("fetchTrendingGeneDate start");
        MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> list, MLException e) {
                if (e == null) {
                    Logger.d("fetchTrendingGeneDate success");
                    int length = list.size();
                    if (length == 0) {
                        fetchSearchGeneDate();
                        return;
                    }
                    for (int i = 0; i < length; i++) {
                        try {
                            repos.add(from(list.get(i)));
                        } catch (Exception jsonException) {
                            continue;
                        }
                    }
                    nowPosition = 0;
                    loadUrl();
                } else {
                    Logger.d("fetchTrendingGeneDate failed");
                    Logger.toast(mContext, R.string.toast_get_recommend_failed);
                }
            }
        });
    }

    private void fetchSearchGeneDate() {
        if (isEnd) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (repos.size() == 0) {
                notice3.setVisibility(View.INVISIBLE);
            } else {
                notice3.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mParmasMap.put("page", page++);
        mParmasMap.put("type", "search");
        Logger.d("fetchSearchGeneDate start");
        MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> list, MLException e) {
                if (e == null) {
                    Logger.d("fetchSearchGeneDate succes");
                    int length = list.size();
                    if (length < 10) {
                        isEnd = true;
                    }
                    for (int i = 0; i < length; i++) {
                        try {
                            repos.add(from(list.get(i)));
                        } catch (Exception jsonException) {
                            continue;
                        }
                    }
                    loadUrl();
                } else {
                    Logger.d("fetchSearchGeneDate failed");
                    Logger.toast(mContext, R.string.toast_get_recommend_failed);
                }
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

    private void loadUrl() {
        Repo repo = repos.get(nowPosition);
        dbRecRepo = dbHelper.getRepoById(repo.getId());
        if (dbRecRepo == null) {
            dbRecRepo = new DBRecRepo();
            dbRecRepo.setRepo_id(repo.getId());
        }
        if (dbRecRepo.isStar() || isReview ? false : dbRecRepo.isSkip() || dbRecRepo.isFork()) {
            nowPosition++;
            loadUrl();
            return;
        }
        mEmptyView.setVisibility(View.GONE);
        mWebView.loadUrl(repo.getHtmlUrl(), true);
        mProgressBar.setVisibility(View.GONE);
        checkIsStar(repo);
    }

    private void checkIsStar(Repo repo) {
        final DBRecRepo checkRepo = dbRecRepo.clone();
        ApiManager.getInstance().isStarred(repo.getOwner().getLogin(),
                repo.getName(), new ApiCallback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        if (response.getStatus() == 204) {
                            checkRepo.setIsStar(true);
                        }
                        dbHelper.updateRepo(checkRepo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null && error.getResponse().getStatus() == 404) {
                            checkRepo.setIsStar(false);
                        } else {
                            super.failure(error);
                            return;
                        }
                        dbHelper.updateRepo(checkRepo);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_notice2:
                Intent intent = new Intent(mContext, AddGeneActivity.class);
                intent.putExtra(AddGeneActivity.INTENT_KEY_TITLE,
                        getString(R.string.activity_add_new_gene));
                intent.putExtra(AddGeneActivity.INTENT_LIST, genes);
                startActivity(intent);
                break;
            case R.id.recommend_notice3:
                mEmptyView.setVisibility(View.GONE);
                isReview = true;
                mWebView.loadUrl(repos.get(0).getHtmlUrl());
                break;
            case R.id.recommend_star:
                starProgressBar.setVisibility(View.VISIBLE);
                ApiManager.getInstance().star(repos.get(nowPosition).getOwner().getLogin(), repos.get(nowPosition).getName(),
                        new ApiCallback<Object>() {
                            @Override
                            public void success(Object o, Response response) {
                                showStarResult(R.string.toast_star_recommend_success);
                                skipBtn.performClick();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                showStarResult(R.string.toast_star_recommend_failed);
                            }
                        });
                break;
            case R.id.recommend_fork:
                ApiManager.getInstance().fork(repos.get(nowPosition).getOwner().getLogin(), repos.get(nowPosition).getName(),
                        new ApiCallback<ForkRepo>() {
                            @Override
                            public void success(ForkRepo forkRepo, Response response) {
                                if (dbRecRepo.getId() != 0) {
                                    dbRecRepo.setIsFork(true);
                                    dbHelper.updateRepo(dbRecRepo);
                                } else {
                                    dbRecRepo.setIsFork(true);
                                    int id = dbHelper.insertRepo(dbRecRepo);
                                    dbRecRepo.setId(id);
                                }
                                Logger.toast(mContext, R.string.toast_fork_recommend_success);
                                skipBtn.performClick();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                super.failure(error);
                            }
                        });
                break;
            case R.id.recommend_skip:
                nowPosition++;
                if (nowPosition == repos.size()) {
                    fetchSearchGeneDate();
                    return;
                }
                dbRecRepo.setIsSkip(true);
                if (dbRecRepo.getId() != 0) {
                    dbHelper.updateRepo(dbRecRepo);
                } else {
                    int id = dbHelper.insertRepo(dbRecRepo);
                    dbRecRepo.setId(id);
                }
                loadUrl();
                break;
            default:
                break;
        }
    }

    private void showStarResult(int res) {
        starProgressBar.setVisibility(View.GONE);
        Logger.toast(mContext, res);
    }

}
