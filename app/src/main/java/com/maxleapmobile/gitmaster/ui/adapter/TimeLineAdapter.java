/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;

import java.util.List;

public class TimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private List<TimeLineEvent> mEvents;

    public TimeLineAdapter(Context context, List<TimeLineEvent> events) {
        this.mContext = context;
        this.mEvents = events;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_timeline, parent, false);
            holder = new ViewHolder();
            holder.userIcon = (ImageView) convertView.findViewById(R.id.timeline_user_icon);
            holder.content = (TextView) convertView.findViewById(R.id.timeline_content);
            holder.time = (TextView) convertView.findViewById(R.id.timeline_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView userIcon;
        TextView content;
        TextView time;
    }

}
