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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.util.TimeUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private List<TimeLineEvent> mEvents;
    private long now;

    public TimeLineAdapter(Context context, List<TimeLineEvent> events) {
        this.mContext = context;
        this.mEvents = events;
        now = System.currentTimeMillis();
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

        TimeLineEvent event = mEvents.get(position);
        SpannableString ss = new SpannableString(event.getActor().getLogin() +
                " starred " + event.getRepo().getName());
        ss.setSpan(new URLSpan(event.getActor().getUrl()), 0, event.getActor().getLogin().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new URLSpan(event.getRepo().getUrl()), event.getActor().getLogin().length() + 9, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(ss);
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.time.setText(getTime(event.getCreatedAt()));
        Picasso.with(mContext).load(event.getActor().getAvatarUrl()).into(holder.userIcon);

        return convertView;
    }

    static class ViewHolder {
        ImageView userIcon;
        TextView content;
        TextView time;
    }

    private String getTime(String formatedTime) {
        long time = TimeUtil.getDateFromString(formatedTime).getTime();
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;
        return DateUtils.getRelativeTimeSpanString(time * 1000, now, DateUtils.MINUTE_IN_MILLIS, flags).toString();
    }

}
