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
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.ActionType;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.ui.activity.RepoDetailActivity;
import com.maxleapmobile.gitmaster.ui.activity.UserInfoActivity;
import com.maxleapmobile.gitmaster.ui.widget.CustomClickableSpan;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.TimeUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.TimeZone;

public class TimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private List<TimeLineEvent> mEvents;
    private long now;

    public TimeLineAdapter(Context context, List<TimeLineEvent> events) {
        this.mContext = context;
        this.mEvents = events;
        now = getUTCNow();
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

        final TimeLineEvent event = mEvents.get(position);

        CustomClickableSpan.TextClickListener nameClickListen = new CustomClickableSpan.TextClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(Const.USERNAME, event.getActor().getLogin());
                mContext.startActivity(intent);
            }
        };
        CustomClickableSpan.TextClickListener repoClickListen = new CustomClickableSpan.TextClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(mContext, RepoDetailActivity.class);
                String fullName = event.getRepo().getName();
                intent.putExtra(RepoDetailActivity.REPONAME, fullName.substring(fullName.lastIndexOf(File.separator) + 1));
                intent.putExtra(RepoDetailActivity.OWNER, fullName.substring(0, fullName.lastIndexOf(File.separator)));
                mContext.startActivity(intent);
            }
        };
        SpannableString ss = null;
        if (event.getType() == ActionType.WatchEvent) {
            ss = new SpannableString(event.getActor().getLogin() +
                    " starred " + event.getRepo().getName());
            ss.setSpan(new CustomClickableSpan(nameClickListen), 0, event.getActor().getLogin().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomClickableSpan(repoClickListen),
                    event.getActor().getLogin().length() + 9, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (event.getType() == ActionType.ForkEvent) {
            ss = new SpannableString(event.getActor().getLogin() +
                    " forked " + event.getRepo().getName());
            ss.setSpan(new CustomClickableSpan(nameClickListen), 0, event.getActor().getLogin().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomClickableSpan(repoClickListen),
                    event.getActor().getLogin().length() + 8, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.content.setText(ss);
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.time.setText(getTime(event.getCreatedAt()));
        Picasso.with(mContext).load(event.getActor().getAvatarUrl()).into(holder.userIcon);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        now = getUTCNow();
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView userIcon;
        TextView content;
        TextView time;
    }

    private long getUTCNow() {
        long time = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getOffset(time);
        return time - offset;
    }

    private String getTime(String formatedTime) {
        long time = TimeUtil.getDateFromString(formatedTime).getTime();
        long duration = (now - time) / 1000;
        if (duration < 60) {
            return mContext.getString(R.string.time_line_less_than_one_minute);
        } else if (duration >= 60 && duration < 120) {
            return mContext.getString(R.string.time_line_minute_ago);
        } else if (duration >= 120 && duration < 3600) {
            return String.format(mContext.getString(R.string.time_line_minutes_ago),
                    duration / 60);
        } else if (duration >= 3600 && duration < 7200) {
            return mContext.getString(R.string.time_line_hour_ago);
        } else if (duration >= 7200 && duration < 86400) {
            return String.format(mContext.getString(R.string.time_line_hours_ago),
                    duration / 3600);
        } else if (duration >= 86400 && duration < 172800) {
            return mContext.getString(R.string.time_line_day_ago);
        } else if (duration >= 172800 && duration < 2592000) {
            return String.format(mContext.getString(R.string.time_line_days_ago),
                    duration / 86400);
        } else if (duration >= 2592000 && duration < 5184000) {
            return mContext.getString(R.string.time_line_month_ago);
        } else if (duration >= 5184000 && duration < 31104000) {
            return String.format(mContext.getString(R.string.time_line_month_ago),
                    duration / 2592000);
        } else if (duration >= 31104000 && duration < 62208000) {
            return mContext.getString(R.string.time_line_years_ago);
        } else {
            return String.format(mContext.getString(R.string.time_line_years_ago),
                    duration / 31104000);
        }
    }

}
