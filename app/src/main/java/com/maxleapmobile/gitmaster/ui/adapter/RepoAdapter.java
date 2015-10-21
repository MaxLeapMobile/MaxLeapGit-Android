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
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.ui.activity.RepoActivity;
import com.maxleapmobile.gitmaster.util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RepoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Repo> mRepos;

    public RepoAdapter(Context context, ArrayList<Repo> repos) {
        mContext = context;
        mRepos = repos;

    }

    @Override
    public int getCount() {
        return mRepos.size();
    }

    @Override
    public Object getItem(int position) {
        return mRepos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_repo, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.repo_name);
            holder.infoView = (TextView) convertView.findViewById(R.id.repo_info);
            holder.ownerView = (TextView) convertView.findViewById(R.id.repo_owner);
            holder.imageView = (ImageView) convertView.findViewById(R.id.repo_photo);
            holder.repoLayout = (RelativeLayout) convertView.findViewById(R.id.item_repo_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Repo item = mRepos.get(position);
        holder.titleView.setText(item.getName());
        holder.infoView.setText(item.getDescription());
        holder.ownerView.setText(String.format(mContext.getString(R.string.frag_repo_owner), item.getOwner().getLogin()));
        Picasso.with(mContext).load(item.getOwner().getAvatarUrl()).transform(new CircleTransform()).into(holder.imageView);

        holder.repoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RepoActivity.class);
                intent.putExtra(RepoActivity.REPONAME, item.getName());
                intent.putExtra(RepoActivity.OWNER, item.getOwner().getLogin());
                intent.putExtra(RepoActivity.REPOURL, item.getHtmlUrl());
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        TextView infoView;
        TextView ownerView;
        ImageView imageView;
        RelativeLayout repoLayout;
    }
}
