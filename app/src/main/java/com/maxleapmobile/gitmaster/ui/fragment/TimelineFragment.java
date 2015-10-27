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
import android.widget.TextView;

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

    private static final int REQUEST_PER_PAGE = 50;
    private static final int MAX_PAGE_COUNT = 6;
    private static final int GET_PER_PAGE = 15;

    private Context mContext;
    private String username;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listview;
    private TextView emptyView;
    private TimeLineAdapter mAdapter;
    private Handler mHandler;
    private List<TimeLineEvent> mEvents;
    private int pageCount = 1;
    private boolean isGettingMore;
    private boolean isEnd;
    private int eventTargetSize;

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
        emptyView = (TextView) view.findViewById(R.id.timeline_empty_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.timeline_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        listview = (ListView) view.findViewById(R.id.timeline_event_list);
        listview.setOnScrollListener(this);
        if (mEvents == null) {
            mEvents = new ArrayList<>();
            eventTargetSize = 15;
            fetchEvents();
        }
        if (mAdapter == null) {
            mAdapter = new TimeLineAdapter(mContext, mEvents);
        }
        listview.setAdapter(mAdapter);
    }

    private void initData() {
        if (!isEnd && mEvents.size() < eventTargetSize) {
            fetchEvents();
            return;
        }
        if (isEnd && mEvents.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.time_line_no_date);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged(eventTargetSize);
        mHandler.removeCallbacks(mProgressRunnable);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchEvents() {
        if (username == null || isEnd) {
            mHandler.removeCallbacks(mProgressRunnable);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        } else {
            if (mEvents.size() < GET_PER_PAGE) {
                mHandler.postDelayed(mProgressRunnable, 500);
            }
        }
        isGettingMore = true;
        ApiManager.getInstance().getReceivedEvents(username,
                pageCount, REQUEST_PER_PAGE, new ApiCallback<List<TimeLineEvent>>() {
                    @Override
                    public void success(List<TimeLineEvent> timeLineEvents, Response response) {
                        if (pageCount == MAX_PAGE_COUNT || REQUEST_PER_PAGE > timeLineEvents.size()) {
                            isEnd = true;
                        }
                        for (int i = 0; i < timeLineEvents.size(); ) {
                            if (timeLineEvents.get(i).getType() == ActionType.ForkEvent ||
                                    timeLineEvents.get(i).getType() == ActionType.WatchEvent) {
                                i++;
                            } else {
                                timeLineEvents.remove(i);
                            }
                        }
                        mEvents.addAll(timeLineEvents);

                        pageCount = (pageCount < MAX_PAGE_COUNT) ? pageCount++ : MAX_PAGE_COUNT;
                        isGettingMore = false;
                        initData();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mEvents.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(R.string.time_line_refresh_failed);
                        }
                        isGettingMore = false;
                        mHandler.removeCallbacks(mProgressRunnable);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        isEnd = false;
        pageCount = 0;
        mEvents.clear();
        eventTargetSize = 15;
        fetchEvents();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isGettingMore
                && totalItemCount >= GET_PER_PAGE && !isEnd) {
            eventTargetSize = eventTargetSize + GET_PER_PAGE;
            if (mEvents.size() >= eventTargetSize) {
                mAdapter.notifyDataSetChanged(eventTargetSize);
            } else {
                fetchEvents();
                mHandler.post(mProgressRunnable);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}
