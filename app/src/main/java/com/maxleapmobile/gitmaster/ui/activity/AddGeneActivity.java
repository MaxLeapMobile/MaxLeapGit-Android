/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.model.Radio;
import com.maxleapmobile.gitmaster.util.DialogUtil;
import com.maxleapmobile.gitmaster.util.Logger;

import java.util.ArrayList;

public class AddGeneActivity extends BaseActivity implements View.OnClickListener {
    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_LANGUAGE = "language";
    public static final String INTENT_KEY_SKILL = "skill";
    public static final String INTENT_KEY_ID = "objectid";
    private String mTitle;
    private String mLanguage;
    private String mSkill;
    private String mObjectId;
    private TextView mLanguageView;
    private TextView mSkillView;
    private ArrayList<Radio> mLanguageList;
    private ArrayList<Radio> mSkillList;
    private ArrayList<Gene> mGenes;
    private Context mContext;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gene);
        mContext = getApplicationContext();
        init();
    }

    private void init() {
        Intent data = getIntent();
        mTitle = data.getStringExtra(INTENT_KEY_TITLE);
        mLanguage = data.getStringExtra(INTENT_KEY_LANGUAGE);
        mSkill = data.getStringExtra(INTENT_KEY_SKILL);
        mObjectId = data.getStringExtra(INTENT_KEY_ID);
        mGenes = (ArrayList<Gene>) data.getSerializableExtra("list");

        initToolbar();
        initUI();
    }

    private void initUI() {
        mLanguageView = (TextView) findViewById(R.id.gene_language);
        mSkillView = (TextView) findViewById(R.id.gene_skill);
        mLanguageView.setOnClickListener(this);
        mSkillView.setOnClickListener(this);
        if (mTitle != null && mTitle.equals(getString(R.string.activity_add_edit_gene))) {// TODO
            mLanguageView.setText(mLanguage);
            mSkillView.setText(mSkill);
            mLanguageView.setTextColor(Color.BLACK);
            mSkillView.setTextColor(Color.BLACK);
        }
        mLanguageList = getLanguageList();
        mProgressBar = (ProgressBar) findViewById(R.id.add_gene_progressbar);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(mTitle);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.check).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                if (mLanguage == null) {
                    Logger.toast(this, R.string.toast_gene_not_select_language);
                    return;
                }
                if (mSkill == null) {
                    Logger.toast(this, R.string.toast_gene_not_select_skill);
                    return;
                }
                saveGene(mObjectId, mLanguage, mSkill);
                break;
            case R.id.gene_language:
                DialogUtil.showGeneDialog(this, mLanguageList, new DialogUtil.CheckListener() {
                    @Override
                    public void onCheck(int checkedId) {
                        for (int i = 0; i < mLanguageList.size(); i++) {
                            if (checkedId == i) {
                                mLanguageList.get(i).setChecked(true);
                                mLanguage = mLanguageList.get(i).getTitle();
                                mLanguageView.setText(mLanguage);
                                mLanguageView.setTextColor(Color.BLACK);
                            } else {
                                mLanguageList.get(i).setChecked(false);
                            }
                        }
                        mSkill = null;
                        mSkillView.setText(R.string.activity_add_gene_skill);
                        mSkillView.setTextColor(getResources().getColor(R.color.color_text_gene));
                    }
                });
                break;
            case R.id.gene_skill:
                refreshSkillList();
                if (mSkillList.isEmpty()) {
                    Logger.toast(this, R.string.toast_gene_select);
                } else {
                    DialogUtil.showGeneDialog(this, mSkillList, new DialogUtil.CheckListener() {
                        @Override
                        public void onCheck(int checkedId) {
                            for (int i = 0; i < mSkillList.size(); i++) {
                                if (checkedId == i) {
                                    mSkillList.get(i).setChecked(true);
                                    mSkill = mSkillList.get(i).getTitle();
                                    mSkillView.setText(mSkill);
                                    mSkillView.setTextColor(Color.BLACK);
                                } else {
                                    mSkillList.get(i).setChecked(false);
                                }
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private ArrayList<Radio> getLanguageList() {
        ArrayList<Radio> radios = new ArrayList<>();
        String[] languages = getResources().getStringArray(R.array.language);
        for (String language : languages) {
            Radio radio = new Radio();
            radio.setTitle(language);
            if (mLanguage != null && mLanguage.equals(language)) {
                radio.setChecked(true);
            }
            radios.add(radio);
        }
        return radios;
    }


    private void refreshSkillList() {
        int position = -1;
        for (int i = 0; i < mLanguageList.size(); i++) {
            if (mLanguageList.get(i).isChecked()) {
                position = i;
            }
        }
        mSkillList = getSkillList(position);
    }

    private ArrayList<Radio> getSkillList(int languagePosition) {
        ArrayList<Radio> radios = new ArrayList<>();
        switch (languagePosition) {
            case 0:
                fillSkillRadios(radios, R.array.html);
                break;
            case 1:
                fillSkillRadios(radios, R.array.java);
                break;
            case 2:
                fillSkillRadios(radios, R.array.javascript);
                break;
            case 3:
                fillSkillRadios(radios, R.array.objective);
                break;
            case 4:
                fillSkillRadios(radios, R.array.php);
                break;
            case 5:
                fillSkillRadios(radios, R.array.python);
                break;
            case 6:
                fillSkillRadios(radios, R.array.swift);
                break;
            case 7:
                fillSkillRadios(radios, R.array.c11);
                break;
            default:
                break;

        }
        return radios;
    }

    private void fillSkillRadios(ArrayList<Radio> radios, int arrayResId) {
        String[] skills = getResources().getStringArray(arrayResId);
        for (String skill : skills) {
            Radio radio = new Radio();
            radio.setTitle(skill);
            if (mSkill != null && mSkill.equals(skill)) {
                radio.setChecked(true);
            }
            radios.add(radio);
        }
    }

    private void saveGene(String objectId, String language, String skill) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(objectId)) {
            Gene gene = new Gene();
            gene.setObjectId(objectId);
            gene.setLanguage(language);
            gene.setSkill(skill);
            if (mGenes.contains(gene)) {
                mProgressBar.setVisibility(View.GONE);
                Logger.toast(mContext, R.string.toast_already_have_gene);
                return;
            }
            UserManager.getInstance().updateGene(gene, new OperationCallback() {
                @Override
                public void success() {
                    mProgressBar.setVisibility(View.GONE);
                    Logger.toast(mContext,
                            getString(R.string.toast_update_gene_success));
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void failed(String error) {
                    mProgressBar.setVisibility(View.GONE);
                    Logger.toast(mContext,
                            getString(R.string.toast_update_gene_failed));
                }
            });
        } else {
            Gene gene = new Gene();
            gene.setLanguage(language);
            gene.setSkill(skill);
            if (mGenes.contains(gene)) {
                mProgressBar.setVisibility(View.GONE);
                Logger.toast(mContext, R.string.toast_already_have_gene);
                return;
            }
            UserManager.getInstance().addGene(gene, new OperationCallback() {
                @Override
                public void success() {
                    mProgressBar.setVisibility(View.GONE);
                    Logger.toast(mContext,
                            getString(R.string.toast_save_gene_success));
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void failed(String error) {
                    mProgressBar.setVisibility(View.GONE);
                    Logger.toast(mContext,
                            getString(R.string.toast_save_gene_failed));
                }
            });
        }
    }

}
