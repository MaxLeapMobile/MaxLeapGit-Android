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
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.ActionType;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.ui.adapter.TimeLineAdapter;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimelineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {


    private static final String TAG = TimelineFragment.class.getSimpleName();

    private Context mContext;
    private String username;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listview;
    private TimeLineAdapter mAdapter;
    private Handler mHandler;
    private List<TimeLineEvent> mEvents;
    private int initPageCount = 0;
    private boolean isGettingMore;

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        username = PreferenceUtil.getString(mContext, Const.USERNAME, null);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.timeline_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        listview = (ListView) view.findViewById(R.id.timeline_event_list);
        if (mEvents == null) {
            mEvents = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new TimeLineAdapter(mContext, mEvents);
        }
        listview.setAdapter(mAdapter);
        if (mEvents.size() < Const.PER_PAGE_COUNT) {
            fetchEvents();
        }
    }

    private void initData() {
        if (mEvents.size() < Const.PER_PAGE_COUNT) {
            fetchEvents();
            return;
        }
        mAdapter.notifyDataSetChanged();
        mHandler.removeCallbacks(mProgressRunnable);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchEvents() {
        if (username == null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        } else {
            if (mEvents.size() < Const.PER_PAGE_COUNT) {
                mHandler.postDelayed(mProgressRunnable, 500);
            }
        }
        isGettingMore = true;
        ApiManager.getInstance().getReceivedEvents(username,
                initPageCount, Const.PER_PAGE_COUNT, new ApiCallback<List<TimeLineEvent>>() {
                    @Override
                    public void success(List<TimeLineEvent> timeLineEvents, Response response) {
                        mEvents.addAll(timeLineEvents);
                        for (int i = 0; i < mEvents.size(); ) {
                            if (mEvents.get(i).getType() == ActionType.ForkEvent ||
                                    mEvents.get(i).getType() == ActionType.WatchEvent) {
                                i++;
                            } else {
                                mEvents.remove(i);
                            }
                        }
                        initPageCount++;
                        isGettingMore = false;
                        initData();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        isGettingMore = false;
                    }
                });
    }

    @Override
    public void onRefresh() {
        fetchEvents();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isGettingMore
                && totalItemCount >= Const.PER_PAGE_COUNT && totalItemCount % Const.PER_PAGE_COUNT == 0) {
            fetchEvents();
            mHandler.post(mProgressRunnable);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}
