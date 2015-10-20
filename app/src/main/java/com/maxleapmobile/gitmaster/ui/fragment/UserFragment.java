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
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.SearchedUsers;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.ui.adapter.UserAdapter;
import com.maxleapmobile.gitmaster.util.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class UserFragment extends Fragment implements AbsListView.OnScrollListener{
    private Context mContext;
    private ProgressBar mProgressBar;
    private ArrayList<User> mUsers;
    private UserAdapter mUserAdapter;
    private static final int PAGE_COUNT = 30;
    private int mPage;
    private String mKeyWord;
    private boolean mIsGettingMore;
    public static final int FLAG_SEARCH = 11;
    public static final int FLAG_USER = 22;
    private int mFlag;

    public static UserFragment newInstance(int flag){
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag",flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args= getArguments();
        if(args != null){
            mFlag = args.getInt("flag");
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
        if(mUsers == null){
            mUsers = new ArrayList<>();
        }
        mUserAdapter = new UserAdapter(mContext,mUsers);
        listView.setAdapter(mUserAdapter);
        listView.setEmptyView(view.findViewById(R.id.search_empty));
        if(mFlag == FLAG_SEARCH){
            listView.setOnScrollListener(this);
        }
    }

    public void searchUserData(String keyWord,int page) {
        Logger.d("=======>> call searchUserData");
        mKeyWord = keyWord;
        if(page == 1){
            mPage = page;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().searchUser(keyWord, page,PAGE_COUNT,new ApiCallback<SearchedUsers>() {
            @Override
            public void success(SearchedUsers searchedUsers, Response response) {
                if (searchedUsers != null) {
                    if(mPage == 1){
                        mUsers.clear();
                    }
                    List<SearchedUsers.Item> items = searchedUsers.getItems();
                    for (SearchedUsers.Item item : items) {
                        User user = new User();
                        user.setAvatarUrl(item.getAvatarUrl());
                        user.setName(item.getLogin());
                        user.setUpdateAt(String.valueOf(item.getScore()));
                        mUsers.add(user);
                    }
                    mProgressBar.setVisibility(View.GONE);
                    mUserAdapter.notifyDataSetChanged();
                }
                if(mIsGettingMore){
                    mIsGettingMore = false;
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((firstVisibleItem + visibleItemCount) == totalItemCount && !mIsGettingMore
                && totalItemCount >= PAGE_COUNT && totalItemCount % PAGE_COUNT == 0){
            mIsGettingMore = true;
            mPage++;
            searchUserData(mKeyWord,mPage);
        }
    }
}
