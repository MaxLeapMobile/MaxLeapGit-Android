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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.ui.activity.UserInfoActivity;
import com.maxleapmobile.gitmaster.util.CircleTransform;
import com.maxleapmobile.gitmaster.util.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> mUsers;

    public UserAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.user_name);
            holder.updateView = (TextView) convertView.findViewById(R.id.user_update);
            holder.imageView = (ImageView) convertView.findViewById(R.id.user_photo);
            holder.content = (RelativeLayout) convertView.findViewById(R.id.item_user_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User item = mUsers.get(position);
        holder.nameView.setText(item.getName());
        holder.updateView.setText(item.getUpdateAt());
        Picasso.with(mContext).load(item.getAvatarUrl()).transform(new CircleTransform()).into(holder.imageView);

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(Const.USERNAME, item.getName());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView updateView;
        ImageView imageView;
        RelativeLayout content;
    }
}
