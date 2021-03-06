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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.SortEnumRepo;
import com.maxleapmobile.gitmaster.model.SortEnumUser;
import com.maxleapmobile.gitmaster.ui.adapter.SearchPagerAdapter;
import com.maxleapmobile.gitmaster.ui.fragment.RepoFragment;
import com.maxleapmobile.gitmaster.ui.fragment.UserFragment;
import com.maxleapmobile.gitmaster.ui.view.SlidingTabLayout;
import com.maxleapmobile.gitmaster.util.DialogUtil;

import java.util.List;

public class SearchActivity extends BaseActivity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private EditText mSearchEdit;
    private FragmentManager mFragmentManager;
    private SortEnumRepo mSortEnumRepo;
    private SortEnumUser mSortEnumUser;
    private int mPosition;
    private String mKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

    }

    private void init() {
        mSortEnumRepo = SortEnumRepo.DEFAULT;
        mSortEnumUser = SortEnumUser.DEFAULT;
        initToolBar();
        initUI();
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new SearchPagerAdapter(this, mFragmentManager));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchEdit = (EditText) findViewById(R.id.search);
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String keyWord = mSearchEdit.getText().toString();
                    if (!keyWord.isEmpty()) {
                        mKeyWord = keyWord;
                        hideSoftKeyBoard(mSearchEdit);
                        performSearch();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        List<Fragment> fragmentList = mFragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof RepoFragment) {
                RepoFragment repoFragment = (RepoFragment) fragment;
                repoFragment.searchRepoData(mKeyWord, 1, mSortEnumRepo);
            } else if (fragment instanceof UserFragment) {
                UserFragment userFragment = (UserFragment) fragment;
                userFragment.searchUserData(mKeyWord, 1, mSortEnumUser);
            }
        }
    }

    private void hideSoftKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void clickSortBy(View v) {
        if (mPosition == 0) {
            DialogUtil.showRepoSortByDialog(this, mSortEnumRepo, new DialogUtil.CheckListener() {
                @Override
                public void onCheck(int checkedID) {
                    switch (checkedID) {
                        case R.id.dialog_radio_1:
                            mSortEnumRepo = SortEnumRepo.DEFAULT;
                            break;
                        case R.id.dialog_radio_2:
                            mSortEnumRepo = SortEnumRepo.STARS;
                            break;
                        case R.id.dialog_radio_3:
                            mSortEnumRepo = SortEnumRepo.FORKS;
                            break;
                        case R.id.dialog_radio_4:
                            mSortEnumRepo = SortEnumRepo.UPDATED;
                            break;
                        default:
                            break;
                    }
                    if (mKeyWord != null) {
                        performSearch();
                    }
                }
            });
        } else {
            DialogUtil.showUserSortByDialog(this, mSortEnumUser, new DialogUtil.CheckListener() {
                @Override
                public void onCheck(int checkedID) {
                    switch (checkedID) {
                        case R.id.dialog_radio_1:
                            mSortEnumUser = SortEnumUser.DEFAULT;
                            break;
                        case R.id.dialog_radio_2:
                            mSortEnumUser = SortEnumUser.FOLLOWERS;
                            break;
                        case R.id.dialog_radio_3:
                            mSortEnumUser = SortEnumUser.RESPOSITORIES;
                            break;
                        case R.id.dialog_radio_4:
                            mSortEnumUser = SortEnumUser.JOINED;
                            break;
                        default:
                            break;
                    }
                    if (mKeyWord != null) {
                        performSearch();
                    }
                }
            });
        }

    }
}
