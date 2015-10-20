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
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.util.CircleTransform;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User item = mUsers.get(position);
        holder.nameView.setText(item.getName());
        holder.updateView.setText(item.getUpdateAt());
        Picasso.with(mContext).load(item.getAvatarUrl()).transform(new CircleTransform()).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView updateView;
        ImageView imageView;
    }
}
