/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLRelation;
import com.maxleap.exception.MLException;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.calllback.RecyclerItemClickListener;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.ui.adapter.GeneAdapter;
import com.maxleapmobile.gitmaster.ui.widget.HorizontalDividerItemDecoration;
import com.maxleapmobile.gitmaster.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GeneActivity extends BaseActivity {
    private static final int ADD_REQUEST_CODE = 100;
    public static final int EDIT_REQUEST_CODE = 101;
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mAddGene;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private GeneAdapter mGeneAdapter;
    private List<Gene> mGenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene);
        initViews();

        mGenes = new ArrayList<>();
        mGeneAdapter = new GeneAdapter(this, mGenes);
        mRecyclerView.setAdapter(mGeneAdapter);

        fetchGeneData();
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
        mProgressBar = (ProgressBar) findViewById(R.id.gene_progressbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.gene_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                        .color(R.color.grey)
                        .size(1)
                        .marginResId(R.dimen.gene_item_margin,
                                R.dimen.gene_item_margin).build()
        );
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplication(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                final Gene item = mGenes.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(GeneActivity.this,
                        R.style.AppCompatAlertDialogStyle);
                builder.setMessage(getString(R.string.dialog_delete_gene_message));
                builder.setPositiveButton(getString(R.string.dialog_sure),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mProgressBar.setVisibility(View.VISIBLE);
                                UserManager.getInstance().removeGene(item, new OperationCallback() {
                                    @Override
                                    public void success() {
                                        Logger.toast(getApplicationContext(),
                                                getString(R.string.toast_gene_delete_success));
                                        fetchGeneData();
                                    }

                                    @Override
                                    public void failed(String error) {
                                        Logger.toast(getApplicationContext(),
                                                getString(R.string.toast_gene_delete_failed));
                                    }
                                });
                            }
                        });
                builder.setNegativeButton(R.string.dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        }));
        mAddGene = (TextView) findViewById(R.id.gene_add);
        mAddGene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneActivity.this, AddGeneActivity.class);
                intent.putExtra(AddGeneActivity.INTENT_KEY_TITLE,
                        getString(R.string.activity_add_new_gene));
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });
    }

    private void fetchGeneData() {
        mProgressBar.setVisibility(View.VISIBLE);
        MLRelation mlRelation = UserManager.getInstance().getCurrentUser().getGenes();
        mlRelation.setTargetClass("Gene");
        MLQuery<MLObject> query = mlRelation.getQuery();
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                mProgressBar.setVisibility(View.GONE);
                if (e == null) {
                    mGenes.clear();
                    for (MLObject object : list) {
                        String language = (String) object.get("language");
                        String skill = (String) object.get("skill");
                        String id = object.getObjectId();
                        Gene gene = new Gene(id, language, skill);
                        mGenes.add(gene);
                        mGeneAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            String language = data.getStringExtra(AddGeneActivity.INTENT_KEY_LANGUAGE);
            String skill = data.getStringExtra(AddGeneActivity.INTENT_KEY_SKILL);
            Gene gene = new Gene(language, skill);
            if (!mGenes.contains(gene)) {
                UserManager.getInstance().addGene(new Gene(language, skill),
                        new OperationCallback() {
                    @Override
                    public void success() {
                        Logger.toast(getApplicationContext(),
                                getString(R.string.toast_save_gene_success));
                        fetchGeneData();
                    }

                    @Override
                    public void failed(String error) {
                        Logger.toast(getApplicationContext(),
                                getString(R.string.toast_save_gene_failed));
                    }
                });
            } else {
                Logger.toast(getApplicationContext(),
                        getString(R.string.toast_already_have_gene));
            }
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String language = data.getStringExtra(AddGeneActivity.INTENT_KEY_LANGUAGE);
            String skill = data.getStringExtra(AddGeneActivity.INTENT_KEY_SKILL);
            String id = data.getStringExtra(AddGeneActivity.INTENT_KEY_ID);
            Gene gene = new Gene(id, language, skill);
            if (!mGenes.contains(gene)) {
                UserManager.getInstance().updateGene(gene, new OperationCallback() {
                    @Override
                    public void success() {
                        Logger.toast(getApplicationContext(),
                                getString(R.string.toast_update_gene_success));
                        fetchGeneData();
                    }

                    @Override
                    public void failed(String error) {
                        Logger.toast(getApplicationContext(),
                                getString(R.string.toast_update_gene_failed));
                    }
                });
            } else {
                Logger.toast(getApplicationContext(),
                        getString(R.string.toast_already_have_gene));
            }
        }
    }
}