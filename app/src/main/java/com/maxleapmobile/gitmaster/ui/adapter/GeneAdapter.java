/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.Gene;

import java.util.List;

public class GeneAdapter extends RecyclerView.Adapter<GeneAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSkill;
        private TextView mLanguage;
        private ImageView mModify;

        public ViewHolder(View view) {
            super(view);
            mSkill = (TextView) view.findViewById(R.id.gene_skill);
            mLanguage = (TextView) view.findViewById(R.id.gene_language);
            mModify = (ImageView) view.findViewById(R.id.gene_modify);
        }

    }

    private List<Gene> mGeneList;

    public GeneAdapter(List<Gene> list) {
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
        Gene item = mGeneList.get(position);
        holder.mSkill.setText(item.getSkill());
        holder.mLanguage.setText(item.getLanguage());
    }

    @Override
    public int getItemCount() {
        return mGeneList.size();
    }
}