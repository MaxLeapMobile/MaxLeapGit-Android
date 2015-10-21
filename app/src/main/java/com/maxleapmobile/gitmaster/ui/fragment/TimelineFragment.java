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
import android.widget.ListView;

import com.maxleap.MLUser;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.ui.adapter.TimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class TimelineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = TimelineFragment.class.getSimpleName();
    private static final int perPageCount = 30;

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listview;
    private TimeLineAdapter mAdapter;
    private Handler mHandler;
    private List<TimeLineEvent> mEvents;
    private int initPageCount = 0;

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mHandler = new Handler();
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
            mHandler.postDelayed(mProgressRunnable, 100);
        }
        fetchEvents();
    }

    private void initData() {
        mAdapter = new TimeLineAdapter(mContext, mEvents);
        listview.setAdapter(mAdapter);
        mHandler.removeCallbacks(mProgressRunnable);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchEvents();
    }

    private void fetchEvents() {
        if (UserManager.getInstance().getCurrentUser() == null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        ApiManager.getInstance().getReceivedEvents(MLUser.getCurrentUser().getUserName(),
                initPageCount, perPageCount, new ApiCallback<List<TimeLineEvent>>() {
                    @Override
                    public void success(List<TimeLineEvent> timeLineEvents, Response response) {
                        mEvents = timeLineEvents;
//                        for (int i = 0; i < mEvents.size(); ) {
//                            if (mEvents.get(i).getType() == ActionType.ForkEvent ||
//                                    mEvents.get(i).getType() == ActionType.WatchEvent) {
//                                i++;
//                            } else {
//                                mEvents.remove(i);
//                            }
//                        }
                        initData();
                    }
                });
    }


}
