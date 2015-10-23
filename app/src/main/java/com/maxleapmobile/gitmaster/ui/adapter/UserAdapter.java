/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.util.CircleTransform;
import com.maxleapmobile.gitmaster.util.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Owner> mUsers;
    private boolean mFromFollowing;

    public UserAdapter(Context context, ArrayList<Owner> users, boolean fromFollowing) {
        mContext = context;
        mUsers = users;
        mFromFollowing = fromFollowing;
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
            holder.unfollowView = (TextView) convertView.findViewById(R.id.user_unfollow);
            holder.imageView = (ImageView) convertView.findViewById(R.id.user_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Owner item = mUsers.get(position);
        holder.nameView.setText(item.getLogin());
        holder.updateView.setText(item.getHtmlUrl());
        Picasso.with(mContext).load(item.getAvatarUrl()).transform(new CircleTransform())
                .placeholder(R.mipmap.ic_user_portrait_small).into(holder.imageView);
        if (mFromFollowing) {
            holder.unfollowView.setVisibility(View.VISIBLE);
            addListener(holder.unfollowView, item);
        } else {
            holder.unfollowView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void addListener(View view, final Owner item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.dialog_unfollow_title);
                builder.setMessage(String.format(mContext.getString(R.string.dialog_unfollow), item.getLogin()));
                builder.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiManager.getInstance().unfollow(item.getLogin(), new ApiCallback<Object>() {
                            @Override
                            public void success(Object o, Response response) {
                                if (response.getStatus() == 204) {
                                    Logger.toast(mContext, R.string.toast_unfollow_success);
                                    mUsers.remove(item);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                super.failure(error);
                            }
                        });
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
    }

    static class ViewHolder {
        TextView nameView;
        TextView updateView;
        TextView unfollowView;
        ImageView imageView;
    }
}
