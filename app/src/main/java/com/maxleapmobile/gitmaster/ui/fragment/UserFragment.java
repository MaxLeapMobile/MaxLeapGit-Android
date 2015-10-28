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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.OrderEnum;
import com.maxleapmobile.gitmaster.model.Organzation;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.SearchedUsers;
import com.maxleapmobile.gitmaster.model.SortEnumUser;
import com.maxleapmobile.gitmaster.ui.activity.UserInfoActivity;
import com.maxleapmobile.gitmaster.ui.adapter.UserAdapter;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.Logger;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class UserFragment extends Fragment implements AbsListView.OnScrollListener {
    private Context mContext;
    private ProgressBar mProgressBar;
    private ArrayList<Owner> mUsers;
    private UserAdapter mUserAdapter;
    private static final int PAGE_COUNT = 30;
    private int mPage;
    private String mKeyWord;
    private boolean mIsGettingMore;
    public static final int FLAG_SEARCH = 11;
    public static final int FLAG_USER_FOLLOWING = 22;
    public static final int FLAG_USER_FOLLOWER = 33;
    public static final int FLAG_ORG = 44;
    private int mFlag;
    private String mUsername;
    private SortEnumUser mSortEnumUser;

    public static UserFragment newInstance(int flag, String username) {
        UserFragment fragment = new UserFragment();
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
        if (mUsers == null) {
            mUsers = new ArrayList<>();
        }
        if (mFlag == FLAG_USER_FOLLOWING &&
                mUsername.equals(PreferenceUtil.getString(mContext, Const.USERNAME, null))) {
            mUserAdapter = new UserAdapter(mContext, mUsers, true);
        } else {
            mUserAdapter = new UserAdapter(mContext, mUsers, false);
        }
        listView.setOnScrollListener(this);
        listView.setAdapter(mUserAdapter);
        listView.setEmptyView(view.findViewById(R.id.search_empty));

        if (mFlag == FLAG_USER_FOLLOWING) {
            fetchFollowingData(1);
        } else if (mFlag == FLAG_USER_FOLLOWER) {
            fetchFollowerData(1);
        } else if (mFlag == FLAG_ORG) {
            fetchOrgData();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(Const.USERNAME, mUsers.get(position).getLogin());
                mContext.startActivity(intent);
            }
        });
    }

    private void fetchOrgData() {
        Logger.d("=======>> call fetchOrgData");
        mProgressBar.setVisibility(View.VISIBLE);
        ApiCallback<List<Organzation>> apiCallback = new ApiCallback<List<Organzation>>() {
            @Override
            public void success(List<Organzation> organzations, Response response) {
                if (organzations != null) {
                    for (Organzation item : organzations) {
                        Owner user = new Owner();
                        user.setAvatarUrl(item.getAvatarUrl());
                        user.setLogin(item.getLogin());
                        user.setHtmlUrl(item.getReposUrl());
                        mUsers.add(user);
                    }
                    mUserAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        };

        if (mUsername.equals(PreferenceUtil.getString(mContext, Const.USERNAME, null))) {
            ApiManager.getInstance().getOrg(apiCallback);
        } else {
            ApiManager.getInstance().getUserOrgs(mUsername, apiCallback);
        }
    }

    private void fetchFollowerData(int page) {
        Logger.d("=======>> call fetchFollowingData");
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().getFollowersList(mUsername, page, PAGE_COUNT, new ApiCallback<List<Owner>>() {
            @Override
            public void success(List<Owner> owners, Response response) {
                if (!owners.isEmpty()) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mUsers.clear();
                    }
                    mUsers.addAll(owners);
                    mUserAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchFollowingData(int page) {
        Logger.d("=======>> call fetchFollowingData");
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().getFollowingList(mUsername, page, PAGE_COUNT, new ApiCallback<List<Owner>>() {
            @Override
            public void success(List<Owner> owners, Response response) {
                if (!owners.isEmpty()) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mUsers.clear();
                    }
                    mUsers.addAll(owners);
                    mUserAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void searchUserData(String keyWord, int page, SortEnumUser sortEnumUser) {
        Logger.d("=======>> call searchUserData");
        mKeyWord = keyWord;
        mSortEnumUser = sortEnumUser;
        if (page == 1) {
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().searchUser(keyWord, mSortEnumUser, OrderEnum.DESC, page, PAGE_COUNT, new ApiCallback<SearchedUsers>() {
            @Override
            public void success(SearchedUsers searchedUsers, Response response) {
                if (searchedUsers != null) {
                    if (mIsGettingMore) {
                        mIsGettingMore = false;
                    }
                    if (mPage == 1) {
                        mUsers.clear();
                    }
                    List<SearchedUsers.Item> items = searchedUsers.getItems();
                    for (SearchedUsers.Item item : items) {
                        Owner user = new Owner();
                        user.setAvatarUrl(item.getAvatarUrl());
                        user.setLogin(item.getLogin());
                        user.setHtmlUrl(item.getHtmlUrl());
                        mUsers.add(user);
                    }
                    mUserAdapter.notifyDataSetChanged();
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
                searchUserData(mKeyWord, mPage, mSortEnumUser);
            } else if (mFlag == FLAG_USER_FOLLOWER) {
                fetchFollowerData(mPage);
            } else if (mFlag == FLAG_USER_FOLLOWING) {
                fetchFollowingData(mPage);
            }

        }
    }
}
