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
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.ui.fragment.RepoFragment;

public class ContainerActivity extends BaseActivity {
    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_USERNAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        String title = getIntent().getStringExtra(INTENT_KEY_TITLE);
        String username = getIntent().getStringExtra(INTENT_KEY_USERNAME);
        if(title == null || username == null){
            throw new IllegalStateException("You should send title and username data");
        }

        initToolbar(title);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            if(title.equals(getString(R.string.mine_label_repos))){
                fragment = RepoFragment.newInstance(RepoFragment.FLAG_USER_REPO, username);
            }else if(title.equals(getString(R.string.mine_label_stars))){
                fragment = RepoFragment.newInstance(RepoFragment.FLAG_USER_STAR, username);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
