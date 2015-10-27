/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.ui.activity.AddGeneActivity;
import com.maxleapmobile.gitmaster.ui.activity.GeneActivity;

import java.util.ArrayList;

public class GeneAdapter extends RecyclerView.Adapter<GeneAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSkill;
        private TextView mLanguage;
        private RelativeLayout mModify;

        public ViewHolder(View view) {
            super(view);
            mSkill = (TextView) view.findViewById(R.id.gene_skill);
            mLanguage = (TextView) view.findViewById(R.id.gene_language);
            mModify = (RelativeLayout) view.findViewById(R.id.gene_modify);
        }

    }

    private Activity mContext;
    private ArrayList<Gene> mGeneList;

    public GeneAdapter(Activity context, ArrayList<Gene> list) {
        this.mContext = context;
        this.mGeneList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gene,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Gene item = mGeneList.get(position);
        holder.mSkill.setText(item.getSkill());
        holder.mLanguage.setText(item.getLanguage());

        holder.mModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddGeneActivity.class);
                intent.putExtra(AddGeneActivity.INTENT_KEY_TITLE,
                        mContext.getString(R.string.activity_add_edit_gene));
                intent.putExtra(AddGeneActivity.INTENT_KEY_LANGUAGE, item.getLanguage());
                intent.putExtra(AddGeneActivity.INTENT_KEY_SKILL, item.getSkill());
                intent.putExtra(AddGeneActivity.INTENT_KEY_ID, item.getObjectId());
                intent.putExtra(AddGeneActivity.INTENT_LIST, mGeneList);
                mContext.startActivityForResult(intent, GeneActivity.EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGeneList.size();
    }
}