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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.FunctionCallback;
import com.maxleap.MLCloudManager;
import com.maxleap.MLObject;
import com.maxleap.MLQueryManager;
import com.maxleap.MLUser;
import com.maxleap.exception.MLException;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.database.DBHelper;
import com.maxleapmobile.gitmaster.database.DBRecRepo;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.ForkRepo;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.ui.activity.GeneActivity;
import com.maxleapmobile.gitmaster.ui.widget.CustomClickableSpan;
import com.maxleapmobile.gitmaster.ui.widget.ProgressWebView;
import com.maxleapmobile.gitmaster.util.Logger;

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
    private static final String READ_ME_SUFFIX = "/blob/master/README.md";
    private int page = 1;

    private Context mContext;
    private ProgressWebView mWebView;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyView;
    private TextView starText;
    private ProgressBar starProgressBar;

    private List<Repo> repos;
    private List<Gene> genes;
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
        view.findViewById(R.id.recommend_skip).setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.repo_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);
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
                isReview = true;
                mWebView.loadUrl(repos.get(0).getHtmlUrl());
            }
        }), mContext.getString(R.string.recommend_notice3_part1).length(), notice3SS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice3.setText(notice3SS.toString());

        mWebView = (ProgressWebView) view.findViewById(R.id.recommend_webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (view.getUrl().equals(repos.get(nowPosition).getHtmlUrl() + READ_ME_SUFFIX)) {
                    mWebView.loadUrl(repos.get(nowPosition).getHtmlUrl());
                }
            }
        });
        mEmptyView = (LinearLayout) view.findViewById(R.id.recommend_empty);
        mEmptyView.setVisibility(View.GONE);
        if (mParmasMap == null) {
            mParmasMap = new HashMap();
        }
        if (repos == null) {
            getGenes();
        }
        if (dbHelper == null) {
            dbHelper = DBHelper.getInstance(mContext);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEnd && nowPosition == repos.size() - 1) {
            mProgressBar.setVisibility(View.VISIBLE);
            compareGenes();
        }
    }

    private void getGenes() {
        UserManager.getInstance().checkIsLogin(new OperationCallback() {
            @Override
            public void success() {
                MLQueryManager.findAllInBackground(MLUser.getCurrentUser().getRelation("genes").getQuery(), new FindCallback<MLObject>() {
                    @Override
                    public void done(List<MLObject> list, MLException e) {
                        if (e == null) {
                            genes = new ArrayList<>();
                            if (e == null) {
                                for (MLObject o : list) {
                                    genes.add(Gene.from(o));
                                }
                                mParmasMap.put("userid", MLUser.getCurrentUser().getUserName());
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
                                mParmasMap.put("page", page);
                                mParmasMap.put("per_page", PER_PAGE);
                                mParmasMap.put("type", "trending");
                                fetchTrendingGeneDate();
                            } else {
                                Logger.toast(mContext, R.string.toast_get_recommend_failed);
                            }
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

    private void compareGenes() {
        MLQueryManager.findAllInBackground(MLUser.getCurrentUser().getRelation("genes").getQuery(), new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        Gene gene = Gene.from(list.get(i));
                        if (genes.contains(gene)) {
                            if (i == list.size() - 1) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                            continue;
                        } else {
                            isEnd = false;
                            isReview = false;
                            repos.clear();
                            fetchTrendingGeneDate();
                            break;
                        }
                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void fetchTrendingGeneDate() {
        MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> list, MLException e) {
                if (e == null) {
                    if (repos == null) {
                        repos = new ArrayList<>();
                    }
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
                    Logger.toast(mContext, R.string.toast_get_recommend_failed);
                }
            }
        });
    }

    private void fetchSearchGeneDate() {
        if (isEnd) {
            mEmptyView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mParmasMap.put("page", page++);
        mParmasMap.put("type", "search");
        MLCloudManager.callFunctionInBackground("repositories", mParmasMap, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> list, MLException e) {
                if (e == null) {
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
        mWebView.loadUrl(repo.getHtmlUrl() + READ_ME_SUFFIX);
        mProgressBar.setVisibility(View.GONE);
        checkIsStar(repo);
    }

    private void checkIsStar(Repo repo) {
        starProgressBar.setVisibility(View.VISIBLE);
        final DBRecRepo checkRepo = dbRecRepo.clone();
        ApiManager.getInstance().isStarred(repo.getOwner().getLogin(),
                repo.getName(), new ApiCallback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        if (response.getStatus() == 204) {
                            checkRepo.setIsStar(true);
                        }
                        if (checkRepo.getRepo_id() == dbRecRepo.getRepo_id()) {
                            starText.setText(R.string.recommend_bottom_label_unstar);
                        } else {
                            dbHelper.updateRepo(checkRepo);
                        }
                        starProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse().getStatus() == 404) {
                            checkRepo.setIsStar(false);
                        } else {
                            super.failure(error);
                            return;
                        }
                        if (checkRepo.getRepo_id() == dbRecRepo.getRepo_id()) {
                            starText.setText(R.string.recommend_bottom_label_star);
                        } else {
                            dbHelper.updateRepo(checkRepo);
                        }
                        starProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Repo repo = repos.get(nowPosition);
        switch (v.getId()) {
            case R.id.recommend_star:
                starProgressBar.setVisibility(View.VISIBLE);
                if (starText.getText().toString().equals(mContext.getString(R.string.recommend_bottom_label_star))) {
                    ApiManager.getInstance().star(repo.getOwner().getLogin(), repo.getName(),
                            new ApiCallback<Object>() {
                                @Override
                                public void success(Object o, Response response) {
                                    showStarResult(R.string.toast_star_recommend_success);
                                    starText.setText(R.string.recommend_bottom_label_unstar);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    showStarResult(R.string.toast_star_recommend_failed);
                                }
                            });
                } else {
                    ApiManager.getInstance().unstart(repo.getOwner().getLogin(), repo.getName(),
                            new ApiCallback<Object>() {
                                @Override
                                public void success(Object o, Response response) {
                                    showStarResult(R.string.toast_unstar_recommend_success);
                                    starText.setText(R.string.recommend_bottom_label_star);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    showStarResult(R.string.toast_unstar_recommend_failed);
                                }
                            });
                }
                break;
            case R.id.recommend_fork:
                ApiManager.getInstance().fork(repo.getOwner().getLogin(), repo.getName(),
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
        mWebView.reload();
    }

}
