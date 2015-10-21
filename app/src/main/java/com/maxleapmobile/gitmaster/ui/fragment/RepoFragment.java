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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.OrderEnum;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.model.SearchedRepos;
import com.maxleapmobile.gitmaster.model.SortEnumRepo;
import com.maxleapmobile.gitmaster.ui.adapter.RepoAdapter;
import com.maxleapmobile.gitmaster.util.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class RepoFragment extends Fragment implements AbsListView.OnScrollListener {
    private ProgressBar mProgressBar;
    private RepoAdapter mRepoAdapter;
    private Context mContext;
    private ArrayList<Repo> mRepos;
    private static final int PAGE_COUNT = 30;
    private int mPage;
    private String mKeyWord;
    private boolean mIsGettingMore;
    public static final int FLAG_SEARCH = 11;
    public static final int FLAG_USER_REPO = 22;
    public static final int FLAG_USER_STAR = 33;
    private int mFlag;
    private String mUsername;
    private SortEnumRepo mSortEnumRepo;

    public static RepoFragment newInstance(int flag, String username) {
        RepoFragment fragment = new RepoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mFlag = args.getInt("flag");
            mUsername = args.getString("username");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.frag_search, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);

        ListView listView = (ListView) view.findViewById(R.id.search_list);
        if (mRepos == null) {
            mRepos = new ArrayList<>();
        }
        mRepoAdapter = new RepoAdapter(mContext, mRepos);
        listView.setAdapter(mRepoAdapter);
        listView.setEmptyView(view.findViewById(R.id.search_empty));
        listView.setOnScrollListener(this);
        if (mFlag == FLAG_USER_REPO) {
            fetchUserRepoData(1);
        } else if (mFlag == FLAG_USER_STAR) {
            fetchUserStarData(1);
        }
    }

    public void fetchUserRepoData(int page) {
        Logger.d("=======>> call fetchUserRepoData");
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().listReposByPage(mUsername, mPage, PAGE_COUNT, new ApiCallback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                if (!repos.isEmpty()) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mRepos.clear();
                    }
                    mRepos.addAll(repos);
                    mRepoAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void fetchUserStarData(int page) {
        Logger.d("=======>> call fetchUserStarData");
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().listStarRepoByUser(mUsername, mPage, PAGE_COUNT, new ApiCallback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                if (!repos.isEmpty()) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mRepos.clear();
                    }
                    mRepos.addAll(repos);
                    mRepoAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void searchRepoData(String keyWord, int page, SortEnumRepo sortEnumRepo) {
        Logger.d("=======>> call searchRepoData");
        mKeyWord = keyWord;
        mSortEnumRepo = sortEnumRepo;
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().searchRepo(mKeyWord, mSortEnumRepo, OrderEnum.DESC, mPage, PAGE_COUNT, new ApiCallback<SearchedRepos>() {
            @Override
            public void success(SearchedRepos searchedRepos, Response response) {
                if (searchedRepos != null) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mRepos.clear();
                    }
                    List<SearchedRepos.Item> items = searchedRepos.getItems();
                    for (SearchedRepos.Item item : items) {
                        Repo repo = new Repo();
                        repo.setName(item.getName());
                        repo.setDescription(item.getDescription());
                        repo.setHtmlUrl(item.getHtmlUrl());
                        Owner owner = new Owner();
                        owner.setAvatarUrl(item.getOwner().getAvatarUrl());
                        owner.setLogin(item.getOwner().getLogin());
                        repo.setOwner(owner);
                        mRepos.add(repo);
                    }
                    mRepoAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && !mIsGettingMore
                && totalItemCount >= PAGE_COUNT && totalItemCount % PAGE_COUNT == 0) {
            mIsGettingMore = true;
            mPage++;
            if (mFlag == FLAG_SEARCH) {
                searchRepoData(mKeyWord, mPage, mSortEnumRepo);
            } else if (mFlag == FLAG_USER_REPO) {
                fetchUserRepoData(mPage);
            } else if (mFlag == FLAG_USER_STAR) {
                fetchUserStarData(mPage);
            }
        }
    }
}
