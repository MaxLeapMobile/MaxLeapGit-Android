/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLRelation;
import com.maxleap.exception.MLException;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.ui.adapter.GeneAdapter;

import java.util.ArrayList;
import java.util.List;

public class GeneActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mAddGene;
    private RecyclerView mRecyclerView;
    private GeneAdapter mGeneAdapter;
    private List<Gene> mGenes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene);
        initViews();

        mGenes = new ArrayList<>();
        mGeneAdapter = new GeneAdapter(mGenes);
        mRecyclerView.setAdapter(mGeneAdapter);

        updateGene();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.gene_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.activity_gene_title);

        mRecyclerView = (RecyclerView) findViewById(R.id.gene_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    private void updateGene() {
        MLRelation mlRelation = UserManager.getInstance().getCurrentUser().getGenes();
        mlRelation.setTargetClass("Gene");
        MLQuery<MLObject> query = mlRelation.getQuery();
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                if (e == null) {
                    for (MLObject object : list) {
                        String language = (String) object.get("language");
                        String skill = (String) object.get("skill");
                        Gene gene = new Gene(language, skill);
                        mGenes.add(gene);
                        mGeneAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}